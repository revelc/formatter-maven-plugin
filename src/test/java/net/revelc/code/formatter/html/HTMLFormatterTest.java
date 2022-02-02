/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.html;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 */
class HTMLFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        String expectedHash = LineEnding.LF.isSystem()
                ? "2d1efbfc25e2afe53c395d288813e88171e2644694fefeda5c909b5d08f8623352dbf9695aed07870ad8c1d6adad00a3c03d49958eb3dbd993d01ea8bf79a455"
                : "dbda6b982bb743fd109f81faa3f62212d606cc6398b8f1da851f73f529f92a8f31af1f3e7bdd56fc3859bf7b80269654ec93717856c4a9e744c7eb86aefaf5d0";
        LineEnding lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        singlePassTest(new HTMLFormatter(), "someFile.html", expectedHash, lineEnding);
        // TODO: jsoup has further bugs to fix so this always fails currently
        // twoPassTest(emptyMap(), new HTMLFormatter(), "someFile.html", expectedHash, lineEnding);
    }

    @Test
    void testIsIntialized() throws Exception {
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        assertFalse(htmlFormatter.isInitialized());
        htmlFormatter.init(emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));
        assertTrue(htmlFormatter.isInitialized());
    }

}
