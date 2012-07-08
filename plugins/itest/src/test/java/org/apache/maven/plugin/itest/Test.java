package org.apache.maven.plugin.itest;/*
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Test extends AbstractMojoTestCase {
    private void printTestName() {
        System.out.printf("Test: %s(%s)\n", getName(), getClass().getName());
    }

    public void testConstants() throws Exception {
        printTestName();
        JScriptMojo mojo = getJScriptMojo("src/test/resources/unit/jscript-basic-test/plugin-config.xml");
        assertTrue(mojo.debug);
        assertTrue(mojo.verbose);
        assertTrue(mojo.useConsole);
        assertTrue(mojo.asynchronous);
        assertTrue(mojo.useRhinoGlobalProperties);
        assertTrue(mojo.useConstants);
        assertEquals(new File(getBasedir(), "/src/test/scripts"), mojo.sourceDirectory);
        assertEquals(new File(getBasedir(), "/target/script-classes"), mojo.outputContextDirectory);
        Properties constants = new Properties() {{
            put("sourceDirectory", new File(getBasedir(), "/src/main/scripts"));
            put("system", java.lang.System.class);
            put("ten", java.math.BigInteger.TEN);
        }};
        setVariableValueToObject(mojo, "constants", constants);
        assertEquals(new HashMap() {{
            put("sourceDirectory", new File(getBasedir(), "/src/main/scripts"));
            put("system", java.lang.System.class);
            put("ten", java.math.BigInteger.TEN);
        }}, mojo.constants);
        mojo.execute();
        assertThat(mojo.out, is(PrintStreamAdapter.class));
        assertThat(mojo.err, is(PrintStreamAdapter.class));
        assertThat(mojo.out.toString(), is(constants.get("sourceDirectory") + "\r\nclass java.lang.System\r\n10\r\n"));
    }

    public void testProperties() throws Exception {
        printTestName();
        JScriptMojo mojo = getJScriptMojo("src/test/resources/unit/jscript-basic-test/plugin-config.xml");
        assertTrue(mojo.debug);
        assertTrue(mojo.verbose);
        assertTrue(mojo.useConsole);
        assertTrue(mojo.asynchronous);
        assertTrue(mojo.useRhinoGlobalProperties);
        assertTrue(mojo.useProperties);
        assertEquals(new File(getBasedir(), "/src/test/scripts"), mojo.sourceDirectory);
        assertEquals(new File(getBasedir(), "/target/script-classes"), mojo.outputContextDirectory);
        Properties properties = new Properties() {{
            put("sourceDirectory", new File(getBasedir(), "/src/main/scripts"));
            put("system", java.lang.System.class);
            put("ten", java.math.BigInteger.TEN);
        }};
        setVariableValueToObject(mojo, "properties", properties);
        assertEquals(new HashMap() {{
            put("sourceDirectory", new File(getBasedir(), "/src/main/scripts"));
            put("system", java.lang.System.class);
            put("ten", java.math.BigInteger.TEN);
        }}, mojo.properties);
        mojo.execute();
        assertThat(mojo.out, is(PrintStreamAdapter.class));
        assertThat(mojo.err, is(PrintStreamAdapter.class));
        assertThat(mojo.out.toString(), is(properties.get("sourceDirectory") + "\r\nclass java.lang.System\r\n10\r\n"));
    }

    public void testCompiler1() throws MojoExecutionException, MojoFailureException {
        JSCompiler compiler = new JSCompiler();
        compiler.outputContextDirectory = new File(getBasedir(), "/target/script-classes");
        compiler.sourceDirectory = new File(getBasedir(), "/src/test/scripts");
        compiler.source = new Source("testCompiler1.js");
        compiler.execute();
        File compiledFile = new File(getBasedir(), "/target/script-classes/$/$0/testCompiler1.class");
        assertTrue(compiledFile.exists());
        assertTrue(compiledFile.isFile());
    }

    public void testCompiler2() throws MojoExecutionException, MojoFailureException {
        JSCompiler compiler = new JSCompiler();
        compiler.outputContextDirectory = new File(getBasedir(), "/target/script-classes");
        compiler.sourceDirectory = new File(getBasedir(), "/src/test/scripts");
        compiler.source = new Source("testCompiler2.js", "custom.package.TestCompiler2");
        compiler.execute();
        File compiledFile = new File(getBasedir(), "/target/script-classes/custom/package/TestCompiler2.class");
        assertTrue(compiledFile.exists());
        assertTrue(compiledFile.isFile());
    }

    public void testCompiler2ex() throws MojoExecutionException, MojoFailureException {
        JSCompiler compiler = new JSCompiler();
        compiler.outputContextDirectory = new File(getBasedir(), "/target/script-classes");
        compiler.sourceDirectory = new File(getBasedir(), "/src/test/scripts");
        compiler.source = new Source("testCompiler2ex.js", "custom.package.TestCompiler2ex");
        compiler.source.extendClass = ClassToExtend.class.getName();
        compiler.source.implementInterfaces = new String[]
                {Runnable.class.getName(), Callable.class.getName()};
        compiler.execute();
        File compiledFile1 = new File(getBasedir(), "/target/script-classes/custom/package/TestCompiler2ex1.class");
        assertTrue(compiledFile1.exists());
        assertTrue(compiledFile1.isFile());
        File compiledFile0 = new File(getBasedir(), "/target/script-classes/custom/package/TestCompiler2ex.class");
        assertTrue(compiledFile0.exists());
        assertTrue(compiledFile0.isFile());
    }

    public void testCompiler3() throws MojoExecutionException, MojoFailureException {
        JSCompiler compiler = new JSCompiler();
        compiler.outputContextDirectory = new File(getBasedir(), "/target/script-classes");
        compiler.sourceDirectory = new File(getBasedir(), "/src/test/scripts");
        compiler.source = new Source("testCompiler3.js", "custom.package.TestCompiler3");
        compiler.source.extendClass = ClassToExtend.class.getName();
        compiler.source.implementInterfaces = new String[]
                {Runnable.class.getName(), Callable.class.getName()};
        compiler.execute();
        File compiledFile1 = new File(getBasedir(), "/target/script-classes/custom/package/TestCompiler31.class");
        assertTrue(compiledFile1.exists());
        assertTrue(compiledFile1.isFile());
        File compiledFile0 = new File(getBasedir(), "/target/script-classes/custom/package/TestCompiler3.class");
        assertTrue(compiledFile0.exists());
        assertTrue(compiledFile0.isFile());
    }

    private JScriptMojo getJScriptMojo(String pomXml) throws Exception {
        JScriptMojo mojo = (JScriptMojo) lookupMojo("jscript", new File(getBasedir(), pomXml));
        assertNotNull(mojo);
        setVariableValueToObject(mojo, "projectArtifact", new ArtifactStub());
        return mojo;
    }
}
