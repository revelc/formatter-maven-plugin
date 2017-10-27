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
                    "a96122af3d92a24300e252fd136b24b1a03814f4e8137411956d2305452c3c1fb1782958be591707adfaa26e9ed8e04b16fcf62c7a8f52b1e80f3d0e709b48ad");
        } else {
            doTestFormat(new HTMLFormatter(), "someFile.html",
                    "8e3d98ef0c4d4578ab4fc5e29ce2ac1b5fdfd12b3aff42ef6c8c838c4daf4ac77b473dadce9b53e13c928835533b0a057269bbd6dce8dc301cd108c8bda56d12");
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
