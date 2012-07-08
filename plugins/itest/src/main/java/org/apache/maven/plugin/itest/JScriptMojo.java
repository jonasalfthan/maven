/**
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.maven.plugin.itest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJavaPackage;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;

import static java.lang.Class.forName;
import static java.lang.System.getSecurityManager;
import static java.lang.System.setSecurityManager;
import static java.lang.Thread.UncaughtExceptionHandler;
import static java.lang.Thread.currentThread;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.locks.LockSupport.park;
import static java.util.concurrent.locks.LockSupport.unpark;
import static org.apache.maven.plugin.itest.PrintStreamAdapter.Level.*;
import static org.mozilla.javascript.Context.*;
import static org.mozilla.javascript.ScriptableObject.putConstProperty;
import static org.mozilla.javascript.ScriptableObject.putProperty;

/**
 * @goal jscript
 * @phase compile
 */
public final class JScriptMojo extends AbstractMojo {

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin runs in debug mode.
     * @parameter expression="${jscript.debug}" default-value="false"
     * @since 1.0-alpha-01
     * */
    boolean debug;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin runs in asynchronous mode.
     * This is applicable in an interactive mode using console.
     * @parameter expression="${jscript.asynchronous}" default-value="true"
     * @since 1.0-alpha-01
     */
    boolean asynchronous;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin uses a console in an interactive mode.
     * @parameter expression="${jscript.useConsole}" default-value="false"
     * @since 1.0-alpha-01
     */
    boolean useConsole;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin uses a verbose mode.
     * The script is printed.
     * @parameter expression="${jscript.useConsole}" default-value="false"
     * @since 1.0-alpha-01
     */
    boolean verbose;

    volatile boolean useRhinoGlobalProperties;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin uses constants.
     * @parameter expression="${jscript.useConstants}" default-value="true"
     * @since 1.0-alpha-01
     */
    boolean useConstants;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin uses properties.
     * @parameter expression="${jscript.useProperties}" default-value="true"
     * @since 1.0-alpha-02
     */
    boolean useProperties;

    /**
     * JavaScript constants valid loaded in prior of starting scripts.
     * Accessible in all non-skipped source scripts.
     * The value of a property can be a string, a public class, and public static fields.
     * @parameter expression="${jscript.constants}"
     * @since 1.0-alpha-02
     * */
    Properties constants;

    /**
     * JavaScript properties valid loaded in prior of starting scripts.
     * Accessible in all non-skipped source scripts.
     * The value of a property can be a string, a public class, and public static fields.
     * @parameter expression="${jscript.properties}"
     * @since 1.0-alpha-02
     * */
    Properties properties;

    //najskor premenne, komplikovane rozpoznavanie public tried, fieldov, a public static metod a ak ani to, tak sa snazi ziskat js funkciu

    //premenne ako classpath -co sa da odvodit compile+runtime, a system JDK: ale otazne je aka forma groupId:artifactId:..., musi sa automaticky rozpoznavat

    /**
     * A set of file patterns to include in the zip.
     * @parameter alias="sources"
     * */
    Source[] sources;

    /**
     * Disables the plugin execution.
     *
     * @parameter expression="${jscript.skip}" default-value="false"
     * @since 1.0-alpha-01
     */
    boolean skip;

    /**
     * The directory where compiled sources classes are placed.
     *
     * @parameter default-value="${project.build.directory}/script-classes"
     * @required
     * @readonly
     * @since 1.0-alpha-01
     */
    File outputContextDirectory;

    /**
     * @parameter expression="${project.artifact}"
     * @required
     * @readonly
     * @since 1.0-alpha-01
     */
    Artifact projectArtifact;

    /**
     * This element specifies a relative path to
     * &lt;code&gt;${project.build.scriptSourceDirectory}&lt;/code&gt
     * containing the JavaScript sources.
     *
     * @parameter expression="${jscript.sourceDirectory}" default-value="${project.build.scriptSourceDirectory}"
     * @required
     * @readonly
     */
    File sourceDirectory;

    /**
     * Security manager for I/O operations: java.lang.SecurityManager#checkWrite(java.lang.String)
     * and java.lang.SecurityManager#checkRead(java.lang.String),
     * ClassLoader (SecurityManager#checkCreateClassLoader) and permissions
     * RuntimePermission("getClassLoader"),
     * Thread permissions RuntimePermission("modifyThread"),
     * SecurityManager#checkPackageAccess and SecurityManager#checkMemberAccess for fields.
     * @parameter expression="${jscript.securityManager}" default-value="${java.lang.SecurityManager}"
     */
    String securityManager;

    /**
     * A public class which extends from InputStream with default constructor accepted only if ${jscript.useRhinoGlobalProperties} is set to true.
     * @parameter expression="${jscript.inputStream}" default-value="java.lang.System.in"
     * @since 1.0-alpha-01
     */
    String inputStream;

    /**
     * A public class which extends from PrintStream with default constructor accepted only if ${jscript.useRhinoGlobalProperties} is set to true.
     * @parameter expression="${jscript.outputPrintStream}"
     * @since 1.0-alpha-01
     */
    String outputPrintStream;

    /**
     * A public class which extends from PrintStream with default constructor accepted only if ${jscript.useRhinoGlobalProperties} is set to true.
     * @parameter expression="${jscript.errorPrintStream}" default-value="java.lang.System.err"
     * @since 1.0-alpha-01
     */
    String errorPrintStream;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info(getClass().getSimpleName());

        compile();
        loadAndRun();

        /*System.out.println(getClass().getSimpleName());
        if (debug.booleanValue())
            getLog().debug("rhino js " + new Main().processOptions(new String[]{"-version"})[0]);

        final StringBuffer args = new StringBuffer(debug.booleanValue() ? "-debug" : "");*/
    }

    PrintStream out, err;

    private void compile() {}

    private void loadAndRun() throws MojoFailureException, MojoExecutionException {
        if ((!outputContextDirectory.exists() || !outputContextDirectory.isDirectory())
                && !outputContextDirectory.mkdir())
            throw new MojoExecutionException("configuration.outputContextDirectory: " +
                                                "directory could not be created in "
                                                + outputContextDirectory);

        if ((!sourceDirectory.exists() || !sourceDirectory.isDirectory())
                && !sourceDirectory.mkdir())
            throw new MojoExecutionException("configuration.sourceDirectory: " +
                                                "directory could not be created in "
                                                + sourceDirectory);

        final PrintStream outputPrintStream = out = createPrintStream(this.outputPrintStream, new PrintStreamAdapter(getLog(), debug ? DEBUG : INFO));
        final PrintStream errorPrintStream = err = createPrintStream(this.errorPrintStream, new PrintStreamAdapter(getLog(), ERROR));
        final InputStream inputStream = createInputStream(this.inputStream, System.in);

        final class ThrowableHolder { volatile Throwable throwable = null; }
        final ThrowableHolder throwableHolder = new ThrowableHolder();
        final Thread mavenThread = currentThread();
        final SecurityManager oldSecurityManager = getSecurityManager(),
                                newSecurityManager = securityManager();
        if (newSecurityManager != null)
            setSecurityManager(newSecurityManager);
        final boolean isDebug = debug;
        checkPropertiesAndConstants(useProperties ? properties : null, useConstants ? constants : null);
        useConstants &= constants != null;
        final Map constants
                = useConstants ? new ConcurrentSkipListMap(this.constants) : emptyMap();
        useProperties &= properties != null;
        final Map properties
                = useProperties ? new ConcurrentSkipListMap(this.properties) : emptyMap();
        final ThreadsafeSource[] sources
                = new ThreadsafeSource[this.sources == null ? 0 : this.sources.length];
        for (int i = 0; this.sources != null && i < this.sources.length; ++i)
            sources[i] = new ThreadsafeSource(this.sources[i]);
        try {
            URLClassLoader cl = new URLClassLoader(new URL[]{outputContextDirectory.toURL()}, mavenThread.getContextClassLoader());
            Thread t = new Thread() {
                public void run() {
                    final Context ctx = enter();
                    final ImporterTopLevel scope;
                    if (useRhinoGlobalProperties) {
                        Global g = new Global(ctx);
                        g.setOut(outputPrintStream);
                        g.setErr(errorPrintStream);
                        g.setIn(inputStream);
                        scope = g;
                    } else scope = new ImporterTopLevel(ctx);
                    try {
                        injectConstants(constants, scope);
                        injectProperties(properties, scope);
                        for (final ThreadsafeSource source : sources) {
                            importPackages(ctx, scope, source.importPackages);
                            importClasses(ctx, scope, source.importClasses);
                            if (source.file == null)
                                throw new MojoFailureException("null file in source");
                            evaluateReader(ctx, scope, source.file, source.startScriptByLine);
                        }
                        Executors.newCachedThreadPool();//for console
                    } catch (MojoFailureException e) {
                        throwableHolder.throwable = e;
                    } catch (MojoExecutionException e) {
                        throwableHolder.throwable = e;
                    } finally {
                        exit();
                    }
                    unpark(mavenThread);
                }
            };
            t.setContextClassLoader(cl);
            t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e)
                { throwableHolder.throwable = e; unpark(mavenThread); }
            });
            t.start();
            park();
            if (throwableHolder.throwable != null)
                throw throwableHolder.throwable;
        } catch (SecurityException e) {
            throw new MojoFailureException(e,
                    e.getLocalizedMessage(),
                    e.getLocalizedMessage()
                            + "\nUse SecurityManager in section configuration of plugin "
                            + projectArtifact.getGroupId() + ":" + projectArtifact.getArtifactId() + ":" + projectArtifact.getVersion() + "\n" +
                            "<configuration>\n\t</securityManager>\n</configuration>");
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Couldn't load compiled scripts.", e);
        } catch (Throwable e) {
            if (e instanceof MojoExecutionException) throw (MojoExecutionException) e;
            if (e instanceof MojoFailureException) throw (MojoFailureException) e;
            throw new MojoExecutionException(e.getLocalizedMessage(), e);
        } finally {
            if (oldSecurityManager != getSecurityManager())
                setSecurityManager(oldSecurityManager);
        }
    }

    private void run() {
        getLog().info("### running ###");
        ;
    }

    private SecurityManager securityManager() throws MojoFailureException {
        if (securityManager == null
                || (securityManager = securityManager.trim())
                    .equals("java.lang.SecurityManager"))
            return null;
        try {
            Class clazz = forName(securityManager, false, currentThread().getContextClassLoader());
            if (SecurityManager.class.isAssignableFrom(clazz))
                throw new MojoFailureException("the class " + securityManager
                                            + " must extend from java.lang.SecurityManager");
            return (SecurityManager) clazz.newInstance();
        } catch(MojoFailureException e) {
            throw e;
        } catch (Throwable e) {
            throw new MojoFailureException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        }
    }

    private static void injectConstants(Map constants, Scriptable scope) throws MojoExecutionException, MojoFailureException {
        for (Map.Entry entry : (Set<Map.Entry>) constants.entrySet()) {
            String key = (String) entry.getKey();
            Object value = resolvePropertyValue(entry.getValue());
            if (value == null) continue;
            putConstProperty(scope, key, javaToJS(value, scope));
        }
    }

    private static void injectProperties(Map properties, Scriptable scope) throws MojoExecutionException, MojoFailureException {
        for (Map.Entry entry : (Set<Map.Entry>) properties.entrySet()) {
            String key = (String) entry.getKey();
            Object value = resolvePropertyValue(entry.getValue());
            if (value == null) continue;
            putProperty(scope, key, javaToJS(value, scope));
        }
    }

    private static <T extends ImporterTopLevel> T importPackages(Context ctx, T scope, String[] packages) {
        NativeJavaPackage[] np = new NativeJavaPackage[packages.length];
        int i = 0;
        for (String pkg : packages)
            np[i++] = new NativeJavaPackage(pkg, currentThread().getContextClassLoader());
        scope.importPackage(ctx, scope, np, null);
        return scope;
    }

    private static <T extends ImporterTopLevel> T importClasses(Context ctx, T scope, String[] classes) {
        StringBuilder script = new StringBuilder();
        for (String clazz : classes)
            script.append("importClass(").append(clazz).append(");");
        ctx.evaluateString(scope, script.toString(), "", 0, null);
        return scope;
    }

    private static Object resolvePropertyValue(Object o) throws MojoExecutionException, MojoFailureException {
        if (o == null || !(o instanceof String)) return o;
        String s = (String) o;
        if (s.length() == 0) return "";
        try {
            ClassLoader cl = currentThread().getContextClassLoader();
            Class c = resolveAsClass(s, cl);
            if (c != null) return c;
            int dot = s.lastIndexOf('.');
            if (dot == -1 || dot == 0 || dot == s.length() - 1)
                return s;
            c = resolveAsClass(s.substring(0, dot), cl);
            if (c == null) return s;
            String fieldName = s.substring(dot + 1);
            Field field = c.getField(fieldName);
            return field.getModifiers() == Modifier.STATIC ? field.get(null) : s;
        } catch (NoSuchFieldException e) {
            return s;
        } catch (SecurityException e) {
            throw new MojoFailureException(e, "Provide RuntimePermission(\"getClassLoader\") " +
                    "and verify SecurityManager#checkPackageAccess and SecurityManager#checkMemberAccess for fields.",
                    e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            throw new MojoExecutionException(e.getLocalizedMessage(), e);
        } catch (ExceptionInInitializerError e) {
            throw new MojoExecutionException(e.getLocalizedMessage(), e);
        }
    }

    static Class resolveAsClass(String name, ClassLoader cl)
            throws MojoExecutionException, MojoFailureException {
        try {
            return forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (LinkageError e) {
            throw new MojoExecutionException(e.getLocalizedMessage(), e);
        }
    }

    private static Object createInstance(Object o) throws MojoFailureException {
        if (!(o instanceof Class)) return null;
        try {
            return ((Class) o).newInstance();
        } catch (ExceptionInInitializerError e) {
            throw new MojoFailureException(o + ": " + e.getLocalizedMessage());
        } catch (SecurityException e) {
            throw new MojoFailureException(e, "SecurityManager#checkMemberAccess failed to create " +
                    "an instance from class " + o, e.getLocalizedMessage());
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static PrintStream createPrintStream(String outputPrintStream, PrintStream fallback)
            throws MojoExecutionException, MojoFailureException {
        Object o = resolvePropertyValue(outputPrintStream);
        o = createInstance(o);
        return o instanceof PrintStream ? (PrintStream) o : fallback;
    }

    private static InputStream createInputStream(String inputStream, InputStream fallback)
            throws MojoExecutionException, MojoFailureException {
        Object o = resolvePropertyValue(inputStream);
        return o instanceof InputStream ? (InputStream) o : fallback;
    }

    private File sourceJSFile(String pathname) throws MojoFailureException {
        File sourceFile = new File(sourceDirectory, pathname);
        try {
            if (!sourceFile.exists() || !sourceFile.isFile())
                throw new MojoFailureException("source file " + sourceFile + " does not exist");
        } catch (SecurityException e) {
            throw new MojoFailureException(e, "java.lang.SecurityManager#checkRead failed to create " +
                    "file " + pathname + " in path " + sourceDirectory, e.getLocalizedMessage());
        }
        if (!sourceFile.getName().toLowerCase().endsWith(".js"))
            throw new MojoFailureException("source file " + sourceFile + " should nbe JavaScript *.js");
        return sourceFile;
    }

    private FileReader sourceJSFileReader(File source) throws MojoFailureException {
        try {
            return new FileReader(source);
        } catch (FileNotFoundException e) {
            throw new MojoFailureException("source file " + source + " does not exist");
        }
    }

    private Object evaluateReader(Context ctx, ImporterTopLevel scope, String source, int startScriptByLine) throws MojoFailureException {
        File sourceFile = sourceJSFile(source);
        try {
            String scriptName = extractScriptFileNameWithoutExtension(sourceFile);
            return ctx.evaluateReader(scope, sourceJSFileReader(sourceFile), scriptName, startScriptByLine, null);
        } catch (IOException e) {
            throw new MojoFailureException(e.getLocalizedMessage());
        }
    }

    private static String extractScriptFileNameWithoutExtension(File source) throws MojoFailureException {
        String fileName = source.getName();
        if (!fileName.toLowerCase().endsWith(".js"))
            throw new MojoFailureException("source file " + source + " should nbe JavaScript *.js");
        return fileName.substring(0, fileName.length() - 3);
    }

    private static void checkPropertiesAndConstants(Properties properties, Properties constants) throws MojoFailureException {
        if (properties == null | constants == null) return;
        HashSet p = new HashSet(properties.keySet());
        if (p.retainAll(constants.keySet()))
            throw new MojoFailureException("several properties overlap with constants: " + p);
    }

    private static Future executeAsynchronous(final ExecutorService executor, final ImporterTopLevel scope, final String command) {
        return executor.submit(new Callable() {
            public Object call() throws Exception {
                try {
                    return Context.enter().evaluateString(scope, command, "", 0, null);
                } finally {
                    Context.exit();
                }
            }
        });
    }
}
