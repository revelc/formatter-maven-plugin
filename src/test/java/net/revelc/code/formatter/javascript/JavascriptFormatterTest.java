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
                : "1802e59ab000031790a0e5ec63f36e53cd62ad5ef67c671f685aa235e5ba2987f8b703a39a155dd543046f96fa62f6bec6767a10f47e6a92f16436b4e1fab0fb";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new JavascriptFormatter(), "AnyJS.js", expectedHash, lineEnding);
    }

    /**
     * Test is initialized.
     *
     */
    @Test
    void testIsInitialized() {
        final var jsFormatter = new JavascriptFormatter();
        Assertions.assertFalse(jsFormatter.isInitialized());
        jsFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(jsFormatter.isInitialized());
    }

}
