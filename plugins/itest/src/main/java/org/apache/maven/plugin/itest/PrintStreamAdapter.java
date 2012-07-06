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

import org.apache.maven.plugin.logging.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

final class PrintStreamAdapter extends PrintStream {
    static enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    private final ByteArrayOutputStream holder = new ByteArrayOutputStream();
    private final PrintStream p = new PrintStream(holder, true);
    private final Log logger;
    private final Level level;

    public PrintStreamAdapter(Log logger, Level level) {
        super(new ByteArrayOutputStream());
        if (logger == null | level == null)
            throw new NullPointerException();
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void flush() {
        p.flush();
        if (holder.size() == 0) return;
        switch (level) {
            case DEBUG:
                logger.debug(toString());
                break;
            case INFO:
                logger.info(toString());
                break;
            case WARN:
                logger.warn(toString());
                break;
            case ERROR:
                logger.error(toString());
                break;
            default: throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return holder.toString();
    }

    @Override
    public void close() {
        p.close();
    }

    @Override
    public boolean checkError() {
        return p.checkError();
    }

    @Override
    protected void setError() {}

    @Override
    protected void clearError() {}

    @Override
    public void write(int b) {
        p.write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        p.write(buf, off, len);
    }

    @Override
    public void print(boolean b) {
        p.print(b);
    }

    @Override
    public void print(char c) {
        p.print(c);
    }

    @Override
    public void print(int i) {
        p.print(i);
    }

    @Override
    public void print(long l) {
        p.print(l);
    }

    @Override
    public void print(float f) {
        p.print(f);
    }

    @Override
    public void print(double d) {
        p.print(d);
    }

    @Override
    public void print(char[] s) {
        p.print(s);
    }

    @Override
    public void print(String s) {
        p.print(s);
    }

    @Override
    public void print(Object obj) {
        p.print(obj);
    }

    @Override
    public void println() {
        p.println();
    }

    @Override
    public void println(boolean x) {
        p.println(x);
    }

    @Override
    public void println(char x) {
        p.println(x);
    }

    @Override
    public void println(int x) {
        p.println(x);
    }

    @Override
    public void println(long x) {
        p.println(x);
    }

    @Override
    public void println(float x) {
        p.println(x);
    }

    @Override
    public void println(double x) {
        p.println(x);
    }

    @Override
    public void println(char[] x) {
        p.println(x);
    }

    @Override
    public void println(String x) {
        p.println(x);
    }

    @Override
    public void println(Object x) {
        p.println(x);
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        return p.printf(format, args);
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        return p.printf(l, format, args);
    }

    @Override
    public PrintStream format(String format, Object... args) {
        return p.format(format, args);
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        return p.format(l, format, args);
    }

    @Override
    public PrintStream append(CharSequence csq) {
        return p.append(csq);
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
        return p.append(csq, start, end);
    }

    @Override
    public PrintStream append(char c) {
        return p.append(c);
    }

    @Override
    public void write(byte[] b) throws IOException {
        p.write(b);
    }
}
