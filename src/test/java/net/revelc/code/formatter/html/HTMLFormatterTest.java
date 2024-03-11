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
     *
     */
    @Test
    void testDoFormatFile() {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        final var expectedHash = LineEnding.LF.isSystem()
                ? "a329f9e337281208ee45cf69e5643101023a2074018765fa5e1c5ea084fc3621fc2ff0825ca3efda7d1b0e9a082ff33ea12d5980246673dd45f8ff1eb5cdfb83"
                : "04658cc66e75b262f8e2dc1a062aadecafbcd6b1e84e9c77e145cf83595025c42698a5f154f043fef1cf909bf96eadfd5f4ef576d5a6b60979a546e1a6678570";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new HTMLFormatter(), "someFile.html", expectedHash, lineEnding);
    }

    /**
     * Test is intialized.
     *
     */
    @Test
    void testIsIntialized() {
        final var htmlFormatter = new HTMLFormatter();
        Assertions.assertFalse(htmlFormatter.isInitialized());
        htmlFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(htmlFormatter.isInitialized());
    }

}
