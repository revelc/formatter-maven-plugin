/*
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
package net.revelc.code.formatter.html;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.FormatCycle;

/**
 * @author yoshiman
 */
class HTMLFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "1cfe5e48635d8618be4d490a5e7f690ef8e1dfc7e24303457030e281068bbebac44b552ae52ac88f03bf10e72ed0582904d665afc54bade395fd3d183abe0cba",
                    FormatCycle.FIRST);
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "1cfe5e48635d8618be4d490a5e7f690ef8e1dfc7e24303457030e281068bbebac44b552ae52ac88f03bf10e72ed0582904d665afc54bade395fd3d183abe0cba",
                    FormatCycle.SECOND);
        } else {
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "57b5eae0562d6abc4d4e874b675c8351282b0c4797a19891c82bb5e1c50c5ede9bda6d1d9490a775e0d5f56f0521854d321de78782760d5fb8567680a25c307c",
                    FormatCycle.FIRST);
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "57b5eae0562d6abc4d4e874b675c8351282b0c4797a19891c82bb5e1c50c5ede9bda6d1d9490a775e0d5f56f0521854d321de78782760d5fb8567680a25c307c",
                    FormatCycle.SECOND);
        }
    }

    @Test
    void testIsIntialized() throws Exception {
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        assertFalse(htmlFormatter.isInitialized());
        htmlFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));
        assertTrue(htmlFormatter.isInitialized());
    }

}
