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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.java.JavaFormatter;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        doTestFormat(new JavaFormatter(), "AnyClass.java",
                "fe7bdeec160a33a744209602d1ae99f94bd8ff433dd3ab856bcf6857588170d5c69b027f15c72bd7a6c0ae6e3659a9ab62196fa198366ec0c0722286257cbdca");
    }

    @Test
    public void testIsIntialized() throws Exception {
        JavaFormatter javaFormatter = new JavaFormatter();
        assertFalse(javaFormatter.isInitialized());
        final File targetDir = new File("target/testoutput");
        targetDir.mkdirs();
        javaFormatter.init(new HashMap<String, String>(), new AbstractFormatterTest.TestConfigurationSource(targetDir));
        assertTrue(javaFormatter.isInitialized());
    }

}
