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
import net.revelc.code.formatter.FormatCycle;

/**
 * @author yoshiman
 */
class CssFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new CssFormatter(), "someFile.css",
                    "72b2c33020774b407c2e49a849e47990941d3c80d982b1a4ef2e0ffed605b85e2680fca57cfdbc9d6cd2fc6fc0236dbeb915fd75f530689c7e90a3745316b6a3",
                    FormatCycle.FIRST);
            doTestFormat(new CssFormatter(), "someFile.css",
                    "72b2c33020774b407c2e49a849e47990941d3c80d982b1a4ef2e0ffed605b85e2680fca57cfdbc9d6cd2fc6fc0236dbeb915fd75f530689c7e90a3745316b6a3",
                    FormatCycle.SECOND);
        } else {
            doTestFormat(new CssFormatter(), "someFile.css",
                    "684255d79eb28c6f4cfa340b6930fe1cfd9de16a1c6abf5f54e8f6837694b599101ef247ed00b8aea5460aa64cda60b418cebefd8ea28d5e747ed9cf4c3a9274",
                    FormatCycle.FIRST);
            doTestFormat(new CssFormatter(), "someFile.css",
                    "684255d79eb28c6f4cfa340b6930fe1cfd9de16a1c6abf5f54e8f6837694b599101ef247ed00b8aea5460aa64cda60b418cebefd8ea28d5e747ed9cf4c3a9274",
                    FormatCycle.SECOND);
        }
    }

    @Test
    void testIsIntialized() throws Exception {
        CssFormatter cssFormatter = new CssFormatter();
        assertFalse(cssFormatter.isInitialized());
        cssFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));

        assertTrue(cssFormatter.isInitialized());
    }

}
