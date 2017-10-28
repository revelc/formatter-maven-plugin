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
package net.revelc.code.formatter.html;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.SystemUtil;

/**
 * @author yoshiman
 */
public class HTMLFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        // FIXME Handle linux vs windows since this formatter does not accept line endings
        if (SystemUtil.LINE_SEPARATOR.equals("\n")) {
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "4c67ab8f63ee0efcb384d4adfe280476caf0d26c5e8799cde868ca1d94d64ece75d7bc3d37c5e84b7ca8656e310ef84057e5068e0dec3bd22a741ca181835efc");
        } else {
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "c41dd597f53450a404550d45b574d4ffe04cae138a8d5b74351c40492da392b31398bb0176f8229dd815cef63109408b95a06e9707242346a718d205ef8fb2df");
        }
    }

    @Test
    public void testIsIntialized() throws Exception {
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        Assert.assertFalse(htmlFormatter.isInitialized());
        final File targetDir = new File("target/testoutput");
        targetDir.mkdirs();
        htmlFormatter.init(new HashMap<String, String>(), new AbstractFormatterTest.TestConfigurationSource(targetDir));
        Assert.assertTrue(htmlFormatter.isInitialized());
    }

}
