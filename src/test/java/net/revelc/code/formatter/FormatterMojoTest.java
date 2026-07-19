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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FormatterMojoTest {

    @Test
    void numberOfThreadsTest() {
        assertEquals(2, FormatterMojo.effectiveThreads("0.5C", 5));
        assertEquals(1, FormatterMojo.effectiveThreads("0.5C", 1));
        assertEquals(2, FormatterMojo.effectiveThreads("1C", 2));
        assertEquals(5, FormatterMojo.effectiveThreads("0", 5));
        assertEquals(3, FormatterMojo.effectiveThreads("3", 5));
        assertThrows(IllegalArgumentException.class, () -> FormatterMojo.effectiveThreads("", 5));
    }
}
