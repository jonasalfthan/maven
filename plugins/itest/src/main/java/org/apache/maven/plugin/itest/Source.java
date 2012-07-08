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

public class Source {
    String importClasses[] = new String[0];
    String importPackages[] = new String[0];
    String extendClass;
    String implementInterfaces[] = new String[0];
    String securityManager;
    String file;
    int startScriptByLine;
    boolean skip;

    /**
     * As of plugin version 1.0-alpha-03, sets class in order to compile from the JavaScript source.
     * @parameter expression="${jscompiler.compileToClass}" default-value=""
     * @since 1.0-alpha-03
     * */
    String compileToClass;

    public Source() {
        this(null);
    }

    public Source(String file) {
        this(file, null);
    }

    public Source(String file, String compileToClass) {
        this.file = file;
        this.compileToClass = compileToClass;
    }
}
