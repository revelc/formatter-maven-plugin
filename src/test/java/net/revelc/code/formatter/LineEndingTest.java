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

package net.revelc.code.formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link LineEnding}.
 */
class LineEndingTest {

    /**
     * Test successfully determining CRLF line ending.
     */
    @Test
    void test_success_read_line_endings_crlf() {
        final var fileData = "Test\r\nTest\r\nTest\r\n";
        final var lineEnd = LineEnding.determineLineEnding(fileData);
        Assertions.assertEquals(LineEnding.CRLF, lineEnd);
    }

    /**
     * Test successfully determining LF line ending.
     */
    @Test
    void test_success_read_line_endings_lf() {
        final var fileData = "Test\nTest\nTest\n";
        final var lineEnd = LineEnding.determineLineEnding(fileData);
        Assertions.assertEquals(LineEnding.LF, lineEnd);
    }

    /**
     * Test successfully determining CR line ending.
     */
    @Test
    void test_success_read_line_endings_cr() {
        final var fileData = "Test\rTest\rTest\r";
        final var lineEnd = LineEnding.determineLineEnding(fileData);
        Assertions.assertEquals(LineEnding.CR, lineEnd);
    }

    /**
     * Test successfully determining LF line ending with mixed endings.
     */
    @Test
    void test_success_read_line_endings_mixed_lf() {
        final var fileData = "Test\r\nTest\rTest\nTest\nTest\r\nTest\n";
        final var lineEnd = LineEnding.determineLineEnding(fileData);
        Assertions.assertEquals(LineEnding.LF, lineEnd);
    }

    /**
     * Test successfully determining AUTO line ending with mixed endings and no clear majority.
     */
    @Test
    void test_success_read_line_endings_mixed_auto() {
        final var fileData = "Test\r\nTest\r\nTest\nTest\nTest\r\nTest\nTest\r";
        final var lineEnd = LineEnding.determineLineEnding(fileData);
        Assertions.assertEquals(LineEnding.UNKNOWN, lineEnd);
    }

    /**
     * Test successfully determining AUTO line ending with no endings.
     */
    @Test
    void test_success_read_line_endings_none_auto() {
        final var fileData = "TestTestTestTest";
        final var lineEnd = LineEnding.determineLineEnding(fileData);
        Assertions.assertEquals(LineEnding.UNKNOWN, lineEnd);
    }

}
