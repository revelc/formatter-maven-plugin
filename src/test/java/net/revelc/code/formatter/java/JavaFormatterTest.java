/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.java;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JavaFormatterTest.
 */
class JavaFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFile() throws Exception {
        final var expectedHash = LineEnding.LF.isSystem()
                ? "5d41510e74b87c6b38c8e4692d53aa9de3d7a85d08c72697b77c48541534147a028e799289d49c05cc9a3cc601e64c86bb954bb62b03b7277616b71ecc5bd716"
                : "4c002ff0cb5f8455bc42d3457b1d3418fcf1fd742551aa2d37d665b30825bf210636cfb8c1fc04e04ed074324c0d22909053b7e805db42b05fb4ba68e0cf457a";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClass.java", expectedHash, lineEnding);
    }

    /**
     * Test do format file keep line feed LF.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFileKeepLineFeedLF() throws Exception {
        final var expectedHash = "2471c37dc976b24353bda569c9e9d702f8b376c2296c0acd1f9e2513b1d842d8a58af2167147b1f79805fa0aa0003480e95c51917510d3c738de486a3b760d4c";
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClassLF.java", expectedHash, LineEnding.KEEP);
    }

    /**
     * Test do format file keep line feed CR.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFileKeepLineFeedCR() throws Exception {
        final var expectedHash = "3ca0689406f695ab2b2f8ec07d4ddbec3fd50a5f777b41294747555ad4b3df1edecaf7ac754abc4c6a700aaa04e8fd59fd34300b3eba6173d0dab80bc981e1c4";
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClassCR.java", expectedHash, LineEnding.KEEP);
    }

    /**
     * Test do format file keep line feed CRLF.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFileKeepLineFeedCRLF() throws Exception {
        final var expectedHash = "4c002ff0cb5f8455bc42d3457b1d3418fcf1fd742551aa2d37d665b30825bf210636cfb8c1fc04e04ed074324c0d22909053b7e805db42b05fb4ba68e0cf457a";
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClassCRLF.java", expectedHash,
                LineEnding.KEEP);
    }

    /**
     * Test is intialized.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testIsIntialized() throws Exception {
        final var javaFormatter = new JavaFormatter();
        Assertions.assertFalse(javaFormatter.isInitialized());
        javaFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(javaFormatter.isInitialized());
    }

    /**
     * Test do format file with exclusions.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testDoFormatFileWithExclusions() throws Exception {
        final var formatter = new JavaFormatter();
        formatter.setExclusionPattern("\\b(from\\([^;]*\\.end[^;]*?\\));");
        final var expectedHash = "7d83d3145da6e03087b99387dbcd4f3fc1a6893b3133eaf068556ae52683e91a20aeab5c91b910d704b468fc5d9b2f6999d956a87b6bb01f13d072e323ed76d9";
        this.twoPassTest(Collections.emptyMap(), formatter, "AnyClassExclusionLF.java", expectedHash, LineEnding.KEEP);
    }

}
