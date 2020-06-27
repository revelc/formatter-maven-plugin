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
package net.revelc.code.formatter.css;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;

/**
 * @author yoshiman
 */
class CssFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new CssFormatter(), "someFile.css",
                    "969be4dfee223037c6cd6278637c7c7bc609c1cc9af8c11f15ceb572f97e4fc9a67fa1d0ed55344d86476c851733972fe3175de60835d2a67892176a2ebd1ad4");
        } else {
            doTestFormat(new CssFormatter(), "someFile.css",
                    "b86bd3a52ab3a7fdccccc8fed7cee820a781ce9770bbf90d6e1b2d9e6da5105200f41b77e7a1efbf8aa7113dcb6819dba96119dc238f2c6a59c3e75bb00ae1b1");
        }
    }

    @Test
    void testIsIntialized() throws Exception {
        CssFormatter cssFormatter = new CssFormatter();
        assertFalse(cssFormatter.isInitialized());
        cssFormatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));

        assertTrue(cssFormatter.isInitialized());
    }

}
