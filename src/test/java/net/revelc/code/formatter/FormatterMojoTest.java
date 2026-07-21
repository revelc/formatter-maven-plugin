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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FormatterMojoTest {

    @Test
    void numberOfThreadsTest() {
        assertEquals(7, FormatterMojo.effectiveThreads("0.72C", 10), "expected rounding down");
        assertEquals(7, FormatterMojo.effectiveThreads("0.79C", 10), "expected rounding down");
        assertEquals(1, FormatterMojo.effectiveThreads("0.00001 C", 2), "expected at least 1 thread");
        assertEquals(2, FormatterMojo.effectiveThreads("1C", 2), "expected exact multiple");
        assertEquals(4, FormatterMojo.effectiveThreads("2C", 2), "expected exact multiple");
        assertEquals(5, FormatterMojo.effectiveThreads("2.5C", 2), "expected exact multiple");
        assertEquals(5, FormatterMojo.effectiveThreads("2.6C", 2), "expected rounded multiple");
        assertEquals(4, FormatterMojo.effectiveThreads("2.4C", 2), "expected rounded multiple");
        assertEquals(4, FormatterMojo.effectiveThreads("2.4c", 2), "expected case insensitivity");
        assertEquals(4, FormatterMojo.effectiveThreads(" 2.4  c ", 2), "expected spaces allowed");
        assertEquals(4, FormatterMojo.effectiveThreads(" 2.4  c ", 2), "expected spaces allowed");
        assertEquals(4, FormatterMojo.effectiveThreads("2.C", 2), "expected no decimal part allowed");
        assertEquals(4, FormatterMojo.effectiveThreads(".5 c", 8), "expected no whole part allowed");
        assertEquals(4, FormatterMojo.effectiveThreads(".0 c", 4), "expected num cores");
        assertEquals(4, FormatterMojo.effectiveThreads("0.0 c", 4), "expected num cores");
        assertEquals(4, FormatterMojo.effectiveThreads("000000.0000000000 c", 4), "expected num cores");
        assertEquals(4, FormatterMojo.effectiveThreads("0 c", 4), "expected num cores");

        assertEquals(5, FormatterMojo.effectiveThreads("0", 5), "expected num cores");
        assertEquals(3, FormatterMojo.effectiveThreads("3", 0), "expected specified count");
        assertEquals(8, FormatterMojo.effectiveThreads(" 8  ", 0), "expected spaces allowed");

        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads("", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads(" ", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads("-1", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads("1.", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads(".1", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads(".2.", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads("2 .3c", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads("-1.0c", 5));
    }
}
