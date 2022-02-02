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
                ? "ca14768f37dab92f65551a71fb3e4ce1304df2f9528ec24abeeb615aea2fc1b3f8910b823e24f4a391b58bddc555429761520f7cb43f0bccb51f04f04b789aab"
                : "563119044ad9aaa712080e7261653f883b4c10a3376a8546200db3ac984233fb82fa012be4e08277d11402098b43c1b28f216393647cc8cbb6213b0023433469";
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
