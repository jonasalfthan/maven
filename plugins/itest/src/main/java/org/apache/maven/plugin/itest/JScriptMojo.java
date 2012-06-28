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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.mozilla.javascript.tools.jsc.Main;

/**
 * @goal jscript
 * @phase compile
 */
public class JScriptMojo extends AbstractMojo {
    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin runs in asynchronous debug mode.
     * @parameter expression="${jscript.debug}" default-value="false"
     * @since 1.0-alpha-01
     * */
    private Boolean debug;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin runs in asynchronous mode.
     * This is applicable in an interactive mode using console.
     * @parameter expression="${jscript.asynchronous}" default-value="true"
     * @since 1.0-alpha-01
     */
    private Boolean asynchronous;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin uses a console in an interactive mode.
     * @parameter expression="${jscript.console}" default-value="false"
     * @since 1.0-alpha-01
     */
    private Boolean console;

    /**
     * As of plugin version 1.0-alpha-01, sets whether the plugin uses a verbose mode.
     * The script is printed.
     * @parameter expression="${jscript.console}" default-value="false"
     * @since 1.0-alpha-01
     */
    private Boolean verbose;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (debug.booleanValue())
            getLog().debug("rhino js " + new Main().processOptions(new String[]{"-version"})[0]);

        final StringBuffer args = new StringBuffer(debug.booleanValue() ? "-debug" : "");
    }
}
