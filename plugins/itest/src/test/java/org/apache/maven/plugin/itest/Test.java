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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

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

    private JScriptMojo getJScriptMojo(String pomXml) throws Exception {
        JScriptMojo mojo = (JScriptMojo) lookupMojo("jscript", new File(getBasedir(), pomXml));
        assertNotNull(mojo);
        setVariableValueToObject(mojo, "projectArtifact", new ArtifactStub());
        return mojo;
    }
}
