/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.revelc.code.formatter.xml;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class XMLFormatterTest.
 */
class XMLFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     */
    @Test
    void testDoFormatFile() {
        // Since we set the line endings via options for xml, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside xml formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        final var expectedHash = LineEnding.LF.isSystem()
                ? "a9c0300d0ab7290d26099d4fbdf353eadbed9cd0a78b3ec66985b6a7b710138f217578a1185d05d7ae80b84761a000113134bc7e5339e80ff1a17a468a38c1a5"
                : "281854ee2ca10debb947f9b35f3acf769a8f89a59c199fbff041a780c912c12ba6215130943439961d3aefbdf84008b7b1f5b11e4c4807ee2f48d0b879c038ee";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new XMLFormatter(), "someFile.xml", expectedHash, lineEnding);
    }

    /**
     * Test is initialized.
     */
    @Test
    void testIsInitialized() {
        final var xmlFormatter = new XMLFormatter();
        Assertions.assertFalse(xmlFormatter.isInitialized());
        xmlFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(xmlFormatter.isInitialized());
    }

}
