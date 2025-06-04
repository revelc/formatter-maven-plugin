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
                ? "343f90ca4725e168d75afbfd5634923611aaeab7429d95b470d0cbe70591fbbacd7ad62f33579a87d00ca26ddf2f2fb411b05d76369b7a30711a1b2fec0c26f5"
                : "d1b98127424f3afecb2df34859b8cf504a65010726eae7ef7dbb7bfcd9e23995bfc5320636fc3e5b36d4cde379d45c4383d21061531ffd4f65f85c600afe35e2";
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
