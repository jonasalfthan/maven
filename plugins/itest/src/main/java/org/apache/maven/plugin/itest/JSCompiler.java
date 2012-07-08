/*
 * The copyright holders of this work license this file to You under
 * the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.  You may obtain a copy of
 * the License at
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.mozilla.javascript.tools.jsc.Main.main;

/**
 * @goal jscompiler
 * @phase compile
 */
public final class JSCompiler extends AbstractMojo {

    /**
     * @parameter expression="${jscompiler.source}"
     * @since 1.0-alpha-03
     */
    Source source;

    /**
     * The directory where compiled sources classes are placed.
     *
     * @parameter default-value="${project.build.directory}/script-classes"
     * @required
     * @readonly
     * @since 1.0-alpha-03
     */
    File outputContextDirectory;

    /**
     * @required
     */
    File sourceDirectory;

    /**
     * @readonly
     */
    private int packagePostfix;

    /**
     * @readonly
     */
    private String freePackage;

    /**
     * @readonly
     */
    private String pkg;

    /**
     * @readonly
     */
    private String clazz;

    public final String getCompiledPackage() {
        return pkg;
    }

    public final String getCompiledClass() {
        return clazz;
    }

    public JSCompiler() {
        initialize();
    }

    public final void initialize() {
        packagePostfix = 0;
        freePackage = null;
    }

    public final void setPackagePostfix(int packagePostfix) {
        if (packagePostfix < 0)
            throw new IllegalArgumentException("packagePostfix must not be negative");
        this.packagePostfix = packagePostfix;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (source.compileToClass != null
                && (source.compileToClass = source.compileToClass.trim()).length() != 0) {
            int lastDot = source.compileToClass.lastIndexOf('.');
            clazz = lastDot == -1 ? source.compileToClass : source.compileToClass.substring(1 + lastDot);
            pkg = lastDot > 1 ? source.compileToClass.substring(0, lastDot) : "";
        } else {
            pkg = findFreePackage();
            if (freePackage == null) freePackage = pkg;
            clazz = extractScriptFileNameWithoutExtension(sourceJSFile(source.file));
        }

        ArrayList<String> args = new ArrayList<String>();
        args.add("-d"); args.add(canonicalDirectory(outputContextDirectory));
        args.add("-package"); args.add(source.compileToClass == null ? pkg + ".$" + packagePostfix : pkg);
        args.add("-o"); args.add(clazz);
        if (source.extendClass != null
                && (source.extendClass = source.extendClass.trim()).length() != 0) {
            args.add("-extends");
            args.add(source.extendClass);
        }
        if (source.implementInterfaces != null
                && source.implementInterfaces.length != 0) {
            args.add("-implements");
            StringBuilder impl = new StringBuilder();
            for (String i : source.implementInterfaces)
                impl.append(i).append(',');
            impl.deleteCharAt(impl.length() - 1);
            args.add(impl.toString());
        }
        args.add(sourceCanonicalPath(sourceDirectory, source.file));
        main(args.toArray(new String[args.size()]));
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

    private static String extractScriptFileNameWithoutExtension(File source) throws MojoFailureException {
        String fileName = source.getName();
        if (!fileName.toLowerCase().endsWith(".js"))
            throw new MojoFailureException("source file " + source + " should nbe JavaScript *.js");
        return fileName.substring(0, fileName.length() - 3);
    }

    private static String findFreePackage() {
        int index = 0;
        String freePackage = "$";
        while (Package.getPackage(freePackage) != null)
            freePackage = "$" + index++;
        return freePackage;
    }

    private static String sourceCanonicalPath(File sourceBaseDir, String sourceFile) throws MojoFailureException, MojoExecutionException {
        try {
            return new File(sourceBaseDir, sourceFile).getCanonicalPath();
        } catch (IOException e) {
            throw new MojoExecutionException(e.getLocalizedMessage(), e);
        } catch (NullPointerException e) {
            throw new MojoFailureException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        } catch (SecurityException e) {
            throw new MojoFailureException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        }
    }

    private static String canonicalDirectory(File directory) throws MojoFailureException {
        try {
            return directory.getCanonicalPath();
        } catch (IOException e) {
            throw new MojoFailureException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
        }
    }
}
