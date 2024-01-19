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

package net.revelc.code.formatter.javascript;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JavascriptFormatterTest.
 */
class JavascriptFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     *
     */
    @Test
    void testDoFormatFile() {
        final var expectedHash = LineEnding.LF.isSystem()
                ? "01336f291b917436baa69b590806a4d0f2296d7118a524541bce7cc288e140d1a25a85bebfafc8f8eb253949df111a4eed04ca7790c0b9b0d7e83e8bec3e59d9"
                : "c9d6b3f17bb2503c794e7411e830b83957ecf94db1c330bad4871641e7537c86a96a69929f959fd79f7b96963fcb61e0c785c95badd100745d0285eece1c6f6d";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new JavascriptFormatter(), "AnyJS.js", expectedHash, lineEnding);
    }

    /**
     * Test is intialized.
     *
     */
    @Test
    void testIsIntialized() {
        final var jsFormatter = new JavascriptFormatter();
        Assertions.assertFalse(jsFormatter.isInitialized());
        jsFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(jsFormatter.isInitialized());
    }

}
