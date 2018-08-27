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

/**
 * @author yoshiman
 */
public class JsonFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        // Since we set the line endings via options for json, we cannot rely on CRLF inside doTestFormat.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new JsonFormatter(), "someFile.json",
                    "9f167e5af2e0b93ab68c5d72fcdee5a4cfa532fd1d673b3c4ff15cb95fcc5f6a17261076a4b9d58261f2887c9d4a87a4125330d5481499246dc5a0154ab476c8");
        } else {
            doTestFormat(new JsonFormatter(), "someFile.json",
                    "ce94186dfe66fe2813fab37d2f4f9eb6e4ca21ee6351051cd971652798b6760be75de0c7ff92913f55378003ffb72fc3ee5289aa213773144a643de891e3cb3a");
        }
    }

    @Test
    public void testIsInitialized() {
        JsonFormatter jsonFormatter = new JsonFormatter();
        assertFalse(jsonFormatter.isInitialized());
        jsonFormatter.init(new HashMap<String, String>(),
                new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
        assertTrue(jsonFormatter.isInitialized());
    }

    @Test
    public void testDoFormatFileWithConfig() throws Exception {
        Map<String, String> jsonFormattingOptions = new HashMap<>();
        jsonFormattingOptions.put("indent", "2");
        jsonFormattingOptions.put("spaceBeforeSeparator", "false");

        // Since we set the line endings via options for json, we cannot rely on CRLF inside doTestFormat.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(jsonFormattingOptions, new JsonFormatter(), "someFile.json",
                    "478edd57b917235d00f16611505060460758e7e0f4b53938941226dca183d09be7e946d9a14dbac492a200592d5a6fa5f463e60fd1c3d3dbf05c08c3c869a36b");
        } else {
            doTestFormat(jsonFormattingOptions, new JsonFormatter(), "someFile.json",
                    "2f894c97a22f1313fc7eb55a1bab5c1e8353c11c65bf979abe40347307f80e0dd745a3ab1caa39450dcd0701ccdb3bc1b0bb3afd80557bae33e4e4e7015b52a2");
        }
    }

}
