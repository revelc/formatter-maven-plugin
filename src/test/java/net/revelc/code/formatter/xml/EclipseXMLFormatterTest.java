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
package net.revelc.code.formatter.xml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;

/**
 * @author yoshiman
 */
public class EclipseXMLFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        // Since we set the line endings via options for the formatter, we cannot rely on CRLF inside doTestFormat.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new XMLFormatter(false), "someFile.xml",
                    "30e68ab990fab71d9f413e50486cfc389c2a92465a837ce5dad9b1b6f2f61443f733bb9ba0b1d657dd4d85933009cf36a2f168771b12dd2e92ea69b79a99f478");
        }
    }

    @Test
    public void testIsIntialized() throws Exception {
        XMLFormatter xmlFormatter = new XMLFormatter(false);
        assertFalse(xmlFormatter.isInitialized());
        xmlFormatter.init(Collections.emptyMap(), new TestConfigurationSource(TEST_OUTPUT_DIR));
        assertTrue(xmlFormatter.isInitialized());
    }

}
