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
package net.revelc.code.formatter.xml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;

/**
 * @author yoshiman
 */
public class XMLFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        if (System.lineSeparator().equals("\n")) {
            doTestFormat(new XMLFormatter(), "someFile.xml",
                    "ecf687f06e4ada957478267eaf9b3f90461ad2520af37e304400c75e48b3b4daa3e0be60145b76061c496a19df1ce1aa064abc91224d79a725e5cefd12367401");
        } else {
            doTestFormat(new XMLFormatter(), "someFile.xml",
                    "8fb712bdc9068f9f8501f555c9dad182176bc66959812f2977d43f4794bb42b13d160bdc3aeb7f68e8a6c7654f2997cd90e41b006900748a6f0ffb8c0cf5077b");
        }
    }

    @Test
    public void testIsIntialized() throws Exception {
        XMLFormatter xmlFormatter = new XMLFormatter();
        assertFalse(xmlFormatter.isInitialized());
        xmlFormatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
        assertTrue(xmlFormatter.isInitialized());
    }

}
