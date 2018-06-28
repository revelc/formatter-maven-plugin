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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;

public class JavascriptFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        doTestFormat(new JavascriptFormatter(), "AnyJS.js",
                "33020bfa1ecebd935b6d6ba8e482bc14433ad52899ca63bd892fbb85d20e835ad183dba1e0a6203a72fbbb3d859b6f6872e320a8ea2fa93c9b2ca301ae7c6ec8");
    }

    @Test
    public void testIsIntialized() throws Exception {
        JavascriptFormatter jsFormatter = new JavascriptFormatter();
        assertFalse(jsFormatter.isInitialized());
        jsFormatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
        assertTrue(jsFormatter.isInitialized());
    }

}
