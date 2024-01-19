/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
     */
    @Test
    void testDoFormatFile() {
        final var expectedHash = LineEnding.LF.isSystem()
                ? "08875f67a63beb7cd46dfdb84c2133c2361873492767559280b79ebc573d117b2f386fcdab0678f44c183ced319cee83c8636f26ce8d3297962a5e90869b2619"
                : "4c002ff0cb5f8455bc42d3457b1d3418fcf1fd742551aa2d37d665b30825bf210636cfb8c1fc04e04ed074324c0d22909053b7e805db42b05fb4ba68e0cf457a";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClass.java", expectedHash, lineEnding);
    }

    /**
     * Test do format file keep line feed LF.
     *
     */
    @Test
    void testDoFormatFileKeepLineFeedLF() {
        final var expectedHash = "08875f67a63beb7cd46dfdb84c2133c2361873492767559280b79ebc573d117b2f386fcdab0678f44c183ced319cee83c8636f26ce8d3297962a5e90869b2619";
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClassLF.java", expectedHash, LineEnding.KEEP);
    }

    /**
     * Test do format file keep line feed CR.
     *
     */
    @Test
    void testDoFormatFileKeepLineFeedCR() {
        final var expectedHash = "efe9e1dc801d95e273d8545f99f84f1efcb9afdeb15d7077d57b8c8e9efb01627eb0c1be8289e0c1f31d3def40eaa9970a27eb38eff7b57170e3490da7f677ce";
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClassCR.java", expectedHash, LineEnding.KEEP);
    }

    /**
     * Test do format file keep line feed CRLF.
     *
     */
    @Test
    void testDoFormatFileKeepLineFeedCRLF() {
        final var expectedHash = "736ee4fb681ff29733c7c3fe0b4f90190fcc60dd30f209c608cb2c3d370a9a7ffa8d8f005627ebfbe405d8063501d2a7b7b1919496ec346052e7eac4db81b8ef";
        this.twoPassTest(Collections.emptyMap(), new JavaFormatter(), "AnyClassCRLF.java", expectedHash,
                LineEnding.KEEP);
    }

    /**
     * Test is intialized.
     *
     */
    @Test
    void testIsIntialized() {
        final var javaFormatter = new JavaFormatter();
        Assertions.assertFalse(javaFormatter.isInitialized());
        javaFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(javaFormatter.isInitialized());
    }

    /**
     * Test do format file with exclusions.
     *
     */
    @Test
    void testDoFormatFileWithExclusions() {
        final var formatter = new JavaFormatter();
        formatter.setExclusionPattern("\\b(from\\([^;]*\\.end[^;]*?\\));");
        final var expectedHash = "4834e1dd55366930839f33be2808872f13f8901eebd1161b67c95ac8dbbb292c1f6a691cd686c56ba5c521e2d2bcc1c5c81195b2ceb2fa94bd412d17542187ce";
        this.twoPassTest(Collections.emptyMap(), formatter, "AnyClassExclusionLF.java", expectedHash, LineEnding.KEEP);
    }

}
