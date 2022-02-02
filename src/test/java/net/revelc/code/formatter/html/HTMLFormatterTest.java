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
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        final var expectedHash = LineEnding.LF.isSystem()
                ? "b539f1c22c396a7057f2670245c2f05294f30692c9270c1e7ae453ded3a864c19eee51716b5b66115fbdae0456f85a7a4f67782e5f0d5b524fe45b8066cf8444"
                : "f0b9a1b262fae0ddc7de06a475c370a46364d3abf427840be9c78f7a2cc20b60fe4e73966df0852c9a4d118f9388e7ad67677a2aa421589a089279e04d4bcf4c";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.singlePassTest(new HTMLFormatter(), "someFile.html", expectedHash, lineEnding);
        // TODO: jsoup has further bugs to fix so this always fails currently
        // twoPassTest(emptyMap(), new HTMLFormatter(), "someFile.html", expectedHash, lineEnding);
    }

    /**
     * Test is intialized.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testIsIntialized() throws Exception {
        final var htmlFormatter = new HTMLFormatter();
        Assertions.assertFalse(htmlFormatter.isInitialized());
        htmlFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(htmlFormatter.isInitialized());
    }

}
