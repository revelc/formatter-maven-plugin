/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.fileoperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.WriterFactory;

public class fileIO {

    /**
     * Read the given file and return the content as a string.
     *
     * @param file
     *            the file
     *
     * @return the string
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public String readFileAsString(final File file, String encoding) throws IOException {
        final var fileData = new StringBuilder(1000);
        try (var reader = new BufferedReader(ReaderFactory.newReader(file, encoding))) {
            var buf = new char[1024];
            var numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                final var readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
        }
        return fileData.toString();
    }

    /**
     * Write the given string to a file.
     *
     * @param str
     *            the str
     * @param file
     *            the file
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void writeStringToFile(final String str, final File file, String encoding) throws IOException {
        if (!file.exists() && file.isDirectory()) {
            return;
        }

        try (var bw = new BufferedWriter(WriterFactory.newWriter(file, encoding))) {
            bw.write(str);
        }
    }
}
