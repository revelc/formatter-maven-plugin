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
 * The Class TimeUtilTest.
 */
class TimeUtilTest {

    /**
     * Test print duration.
     *
     */
    @Test
    void testPrintDuration() {
        Assertions.assertEquals("123ms", TimeUtil.printDuration(123));
        Assertions.assertEquals("1s", TimeUtil.printDuration(1000));
        Assertions.assertEquals("1s1ms", TimeUtil.printDuration(1001));
        Assertions.assertEquals("1s250ms", TimeUtil.printDuration(1250));
        Assertions.assertEquals("33s", TimeUtil.printDuration(33000));
        Assertions.assertEquals("33s1ms", TimeUtil.printDuration(33001));
        Assertions.assertEquals("33s444ms", TimeUtil.printDuration(33444));
        Assertions.assertEquals("1m0s", TimeUtil.printDuration(60000));
        Assertions.assertEquals("1m1s", TimeUtil.printDuration(61000));
        Assertions.assertEquals("1m1s1ms", TimeUtil.printDuration(61001));
        Assertions.assertEquals("1m1s2ms", TimeUtil.printDuration(61002));
        Assertions.assertEquals("30m55s123ms", TimeUtil.printDuration(1855123));
        Assertions.assertEquals("30m0s", TimeUtil.printDuration(5400000));
        Assertions.assertEquals("30m1s", TimeUtil.printDuration(5401000));
    }

}
