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
package net.revelc.code.formatter.java;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * @author marvin.froeder
 */
class JavaFormatterTest extends AbstractFormatterTest {

    @Test
    void testDoFormatFile() throws Exception {
        String expectedHash = LineEnding.LF.isSystem()
                ? "5d41510e74b87c6b38c8e4692d53aa9de3d7a85d08c72697b77c48541534147a028e799289d49c05cc9a3cc601e64c86bb954bb62b03b7277616b71ecc5bd716"
                : "fe7bdeec160a33a744209602d1ae99f94bd8ff433dd3ab856bcf6857588170d5c69b027f15c72bd7a6c0ae6e3659a9ab62196fa198366ec0c0722286257cbdca";
        LineEnding lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        twoPassTest(emptyMap(), new JavaFormatter(), "AnyClass.java", expectedHash, lineEnding);
    }

    @Test
    void testDoFormatFileKeepLineFeedLF() throws Exception {
        String expectedHash = "5d41510e74b87c6b38c8e4692d53aa9de3d7a85d08c72697b77c48541534147a028e799289d49c05cc9a3cc601e64c86bb954bb62b03b7277616b71ecc5bd716";
        twoPassTest(emptyMap(), new JavaFormatter(), "AnyClassLF.java", expectedHash, LineEnding.KEEP);
    }

    @Test
    void testDoFormatFileKeepLineFeedCR() throws Exception {
        String expectedHash = "cf44c525667d8c49c80d390215e4d1995c10e8966583da0920e3917837188e5b95159f9dc7b4ae2559fbfa4550cbbaca166edc8991907d5fd4bbc74a1402e97e";
        twoPassTest(emptyMap(), new JavaFormatter(), "AnyClassCR.java", expectedHash, LineEnding.KEEP);
    }

    @Test
    void testDoFormatFileKeepLineFeedCRLF() throws Exception {
        String expectedHash = "fe7bdeec160a33a744209602d1ae99f94bd8ff433dd3ab856bcf6857588170d5c69b027f15c72bd7a6c0ae6e3659a9ab62196fa198366ec0c0722286257cbdca";
        twoPassTest(emptyMap(), new JavaFormatter(), "AnyClassCRLF.java", expectedHash, LineEnding.KEEP);
    }

    @Test
    void testIsIntialized() throws Exception {
        JavaFormatter javaFormatter = new JavaFormatter();
        assertFalse(javaFormatter.isInitialized());
        javaFormatter.init(emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_PRIMARY_DIR));
        assertTrue(javaFormatter.isInitialized());
    }

    @Test
    void testDoFormatFileWithExclusions() throws Exception {
        JavaFormatter formatter = new JavaFormatter();
        formatter.setExclusionPattern("\\b(from\\([^;]*\\.end[^;]*?\\));");
        String expectedHash = "ea4580e667895a179a2baccd4822077e87caa62f2ebb2db0409407de48890b06fa1f7a070db617a4ab156a4e9223d5f2aa99a69209e1f0bdb263a0af7359d43e";
        twoPassTest(emptyMap(), formatter, "AnyClassExclusionLF.java", expectedHash, LineEnding.KEEP);
    }

}
