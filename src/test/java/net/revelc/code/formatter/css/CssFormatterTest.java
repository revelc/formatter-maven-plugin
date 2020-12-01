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
                    "80042a8a00870195a43c6a80e8e0b2d13e3ac8fd3f52e10277f05a637af6bb5bdddcf5b939677be0b48b907a74861f3f3cd71be38edf4f740c7e9f31d79b1ee1");
        } else {
            doTestFormat(new CssFormatter(), "someFile.css",
                    "332e3475ccfcb029ba8db3fd1d9ebd7e44dad60d96866de50e0434c0075a41e138c3fc919410780e985c69e6975d86cf18c2048ab1311cb382218db82dfaa79e");
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
