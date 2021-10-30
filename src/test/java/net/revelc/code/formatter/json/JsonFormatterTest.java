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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.FormatCycle;
import net.revelc.code.formatter.LineEnding;

/**
 * @author yoshiman
 */
class JsonFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        // Since we set the line endings via options for json, we cannot rely on CRLF inside doTestFormat.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new JsonFormatter(), "someFile.json",
                    "4f74200377cfd8a1ee31622ef212268ceb6db177c2bc39481828aba1581869f593ec059c987d18d02f77a134084e8ccf2d016fe28ecc2209d81ffabfc885fae3",
                    FormatCycle.FIRST);
            doTestFormat(new JsonFormatter(), "someFile.json",
                    "4f74200377cfd8a1ee31622ef212268ceb6db177c2bc39481828aba1581869f593ec059c987d18d02f77a134084e8ccf2d016fe28ecc2209d81ffabfc885fae3",
                    FormatCycle.SECOND);
        } else {
            doTestFormat(new JsonFormatter(), "someFile.json",
                    "c6e19e9d042d8d2045eb17d2966f105e6c538d5c05c614c556eb88dfb020645cb2d410cf059643a14ca193487b888e24194499ee8be2c337afdc89067a23e4cd",
                    FormatCycle.FIRST);
            doTestFormat(new JsonFormatter(), "someFile.json",
                    "c6e19e9d042d8d2045eb17d2966f105e6c538d5c05c614c556eb88dfb020645cb2d410cf059643a14ca193487b888e24194499ee8be2c337afdc89067a23e4cd",
                    FormatCycle.SECOND);
        }
    }

    @Test
    void testIsInitialized() {
        JsonFormatter jsonFormatter = new JsonFormatter();
        assertFalse(jsonFormatter.isInitialized());
        jsonFormatter.init(new HashMap<String, String>(),
                new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));
        assertTrue(jsonFormatter.isInitialized());
    }

    @Test
    void testDoFormatFileWithConfig() throws Exception {
        Map<String, String> jsonFormattingOptions = new HashMap<>();
        jsonFormattingOptions.put("indent", "2");
        jsonFormattingOptions.put("spaceBeforeSeparator", "false");
        jsonFormattingOptions.put("alphabeticalOrder", "true");

        // Since we set the line endings via options for json, we cannot rely on CRLF inside doTestFormat.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(jsonFormattingOptions, new JsonFormatter(), "someFile.json",
                    "2122f00ff5a3b4d3012d568b907deaecee248b5b1f8e3ebe213f6b7b3a628ad0c14d236e79789763d940f346c689694ac9854fe8fe7d935a50286e65c036a36d",
                    LineEnding.CRLF, FormatCycle.FIRST);
            doTestFormat(jsonFormattingOptions, new JsonFormatter(), "someFile.json",
                    "2122f00ff5a3b4d3012d568b907deaecee248b5b1f8e3ebe213f6b7b3a628ad0c14d236e79789763d940f346c689694ac9854fe8fe7d935a50286e65c036a36d",
                    LineEnding.CRLF, FormatCycle.SECOND);
        } else {
            doTestFormat(jsonFormattingOptions, new JsonFormatter(), "someFile.json",
                    "5d433f2700a2fdabfabdb309d5f807df91ad86f7a94658d4a3f2f3699ae78b2efb1de451c141f61905f1c814cd647f312ae9651454e65d124510be0573082e86",
                    LineEnding.CRLF, FormatCycle.FIRST);
            doTestFormat(jsonFormattingOptions, new JsonFormatter(), "someFile.json",
                    "5d433f2700a2fdabfabdb309d5f807df91ad86f7a94658d4a3f2f3699ae78b2efb1de451c141f61905f1c814cd647f312ae9651454e65d124510be0573082e86",
                    LineEnding.CRLF, FormatCycle.SECOND);
        }
    }

}
