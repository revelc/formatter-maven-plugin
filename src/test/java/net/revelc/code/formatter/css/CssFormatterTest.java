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

package net.revelc.code.formatter.css;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class CssFormatterTest.
 */
class CssFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     */
    @Test
    void testDoFormatFile() {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        final var expectedHash = LineEnding.LF.isSystem()
                ? "6434062bd7499e707dea1ea17d301556712222b7671fae79ec20d906cda467a2b2210896a196dbaa9da7d221f04cab87a6b2e5538ca3c46fa7fdbedb46010a8c"
                : "488b10041890a552141edb844a7d98f04ec2f30291a774dcb7f5fedcaad87dac85d3d9ed43b02f4d8d266e96549acd234038cff6e16b32a57034609f16330c8b";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new CssFormatter(), "someFile.css", expectedHash, lineEnding);
    }

    /**
     * Test is initialized.
     */
    @Test
    void testIsInitialized() {
        final var cssFormatter = new CssFormatter();
        Assertions.assertFalse(cssFormatter.isInitialized());
        cssFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(cssFormatter.isInitialized());
    }

}
