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

package net.revelc.code.formatter.html;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class HTMLFormatterTest.
 */
class HTMLFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     */
    @Test
    void testDoFormatFile() {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        final var expectedHash = LineEnding.LF.isSystem()
                ? "cf502240752d8f1785e327ecc81da0e8d528d4ba05fe8b8b5c175572a3b04355e47c715909134a3817d532137bbee1cee8b7012e2864a82e0ff454ce27dfbe8d"
                : "428169b59607e874cc2dfdc98df19d2a309990eeae2ece04b8ac039c2e48ff94444dd3331271be75505bbb170fd6734ba56d231e3ef0f149351b9b0abb7495dc";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new HTMLFormatter(), "someFile.html", expectedHash, lineEnding);
    }

    /**
     * Test is initialized.
     */
    @Test
    void testIsInitialized() {
        final var htmlFormatter = new HTMLFormatter();
        Assertions.assertFalse(htmlFormatter.isInitialized());
        htmlFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(htmlFormatter.isInitialized());
    }

}
