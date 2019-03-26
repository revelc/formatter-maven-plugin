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
 * @author jam01
 */
public class XMLFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        doTestFormat(new XMLFormatter(), "someFile.xml",
                "a5bfe48d45504b624d5f610bcbb935b117c8190de7c27957a8ba3658df6f3879682c485d77378443399a5d092899105988386c56b14308ac21faf02a82bfdffb");
    }

    @Test
    public void testIsIntialized() throws Exception {
        XMLFormatter xmlFormatter = new XMLFormatter();
        assertFalse(xmlFormatter.isInitialized());
        xmlFormatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
        assertTrue(xmlFormatter.isInitialized());
    }

}
