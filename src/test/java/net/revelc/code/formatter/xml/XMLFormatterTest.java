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
     *
     */
    @Test
    void testDoFormatFile() {
        // Since we set the line endings via options for xml, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside xml formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        final var expectedHash = LineEnding.LF.isSystem()
                ? "c3b2ca12cf14ad635031fb19d7418631921cdb61b4ffab06c647f0e7ba01e08c3e1e4a254a58dd4d8b385c21a084e16678f1e1f5787cec622c6b5dcdfad1b99a"
                : "e39c21600cbd20712d91f279ed5d1384c4bf9191fe1e371d51d3af58c297b059a81c86c0ebde63b791b907aeb42f203fa20c69bcaf6d1970b195cd4ab559fb35";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new XMLFormatter(), "someFile.xml", expectedHash, lineEnding);
    }

    /**
     * Test is initialized.
     *
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
