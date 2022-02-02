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
package net.revelc.code.formatter.javascript;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JavascriptFormatterTest.
 */
class JavascriptFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFile() throws Exception {
        String expectedHash = LineEnding.LF.isSystem()
                ? "d2a196d7aaddc3285b783d929965c884d1c1cacf15e777f0a7a7315355822b51f903fdb5b47f7f6c79ffc35e2f2dee58b605578a8b18afb91cdfed4624f03d7d"
                : "33020bfa1ecebd935b6d6ba8e482bc14433ad52899ca63bd892fbb85d20e835ad183dba1e0a6203a72fbbb3d859b6f6872e320a8ea2fa93c9b2ca301ae7c6ec8";
        LineEnding lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        twoPassTest(emptyMap(), new JavascriptFormatter(), "AnyJS.js", expectedHash, lineEnding);
    }

    /**
     * Test is intialized.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testIsIntialized() throws Exception {
        JavascriptFormatter jsFormatter = new JavascriptFormatter();
        assertFalse(jsFormatter.isInitialized());
        jsFormatter.init(emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));
        assertTrue(jsFormatter.isInitialized());
    }

}
