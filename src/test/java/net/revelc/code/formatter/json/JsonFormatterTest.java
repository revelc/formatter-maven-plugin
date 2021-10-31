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
package net.revelc.code.formatter.json;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * @author yoshiman
 */
class JsonFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        // Since we set the line endings via options for json, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        String expectedHash = LineEnding.LF.isSystem()
                ? "1c8b8931b79a7dfaa4d2ab1986ebfe5967716b63877aa0311091214bf870f5480469a80e920fc825a98ad265f252e94e1ca4b94a55a279d0d2d302a20dcb4fa3"
                : "c6e19e9d042d8d2045eb17d2966f105e6c538d5c05c614c556eb88dfb020645cb2d410cf059643a14ca193487b888e24194499ee8be2c337afdc89067a23e4cd";
        LineEnding lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        twoPassTest(emptyMap(), new JsonFormatter(), "someFile.json", expectedHash, lineEnding);
    }

    @Test
    void testIsInitialized() {
        JsonFormatter jsonFormatter = new JsonFormatter();
        assertFalse(jsonFormatter.isInitialized());
        jsonFormatter.init(emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));
        assertTrue(jsonFormatter.isInitialized());
    }

    @Test
    void testDoFormatFileWithConfig() throws Exception {
        Map<String, String> jsonFormattingOptions = new HashMap<>();
        jsonFormattingOptions.put("indent", "2");
        jsonFormattingOptions.put("spaceBeforeSeparator", "false");
        jsonFormattingOptions.put("alphabeticalOrder", "true");

        // Since we set the line endings via options for json, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        String expectedHash = LineEnding.LF.isSystem()
                ? "0ca303fef968b92f3f798ff1615cd6c501ea3b754fd18f54932fd07c1dce86d2df9845817b8f521a2254c98c6e0d35b0bced3ea12113e961d3789111868897d7"
                : "5d433f2700a2fdabfabdb309d5f807df91ad86f7a94658d4a3f2f3699ae78b2efb1de451c141f61905f1c814cd647f312ae9651454e65d124510be0573082e86";
        LineEnding lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        twoPassTest(jsonFormattingOptions, new JsonFormatter(), "someFile.json", expectedHash, lineEnding);
    }

}
