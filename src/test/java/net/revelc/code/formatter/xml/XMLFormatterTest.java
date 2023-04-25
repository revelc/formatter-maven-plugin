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
     *
     */
    @Test
    void testDoFormatFile() {
        // Since we set the line endings via options for xml, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside xml formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        final var expectedHash = LineEnding.LF.isSystem()
                ? "50bd28ef987519facbeffa1a5c3e2ef484adddfe192b44ee1ae2d27d2ca31697b64918a163e3ba4f5f46cfe33c733b7aeb836c01ae01e139ed0138a956300d7c"
                : "2237283dbc5f0fa0af5460f51407abf9732c1f7a785d3e60d0953addf41774805f36e6cfe1ebee8da7f09d62265a5ca623e99d473aaaf52676bc539e0b2cfb04";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new XMLFormatter(), "someFile.xml", expectedHash, lineEnding);
    }

    /**
     * Test is intialized.
     *
     */
    @Test
    void testIsIntialized() {
        final var xmlFormatter = new XMLFormatter();
        Assertions.assertFalse(xmlFormatter.isInitialized());
        xmlFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(xmlFormatter.isInitialized());
    }

}
