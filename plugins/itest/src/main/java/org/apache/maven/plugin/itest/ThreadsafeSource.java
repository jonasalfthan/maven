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

final class ThreadsafeSource {
    final String importClasses[];
    final String importPackages[];
    final String extendClass;
    final String implementInterfaces[];
    final String securityManager;
    String file;
    final int startScriptByLine;
    final boolean skip;

    ThreadsafeSource(Source s) {
        this(s.importClasses, s.importPackages,
                s.extendClass, s.implementInterfaces,
                s.securityManager, s.file, s.startScriptByLine, s.skip);
    }

    ThreadsafeSource(String importClasses[], String importPackages[],
                     String extendClass, String implementInterfaces[],
                     String securityManager, String file, int startScriptByLine, boolean skip) {
        this.importClasses = importClasses;
        this.importPackages = importPackages;
        this.extendClass = extendClass;
        this.implementInterfaces = implementInterfaces;
        this.securityManager = securityManager;
        this.file = file;
        this.startScriptByLine = startScriptByLine;
        this.skip = skip;
    }
}
