/**
 * Copyright 2010-2017. All work is copyrighted to their respective
 * author(s), unless otherwise stated.
 *
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

import org.junit.Assert;
import org.junit.Test;

import net.revelc.code.formatter.AbstractFormatterTest;

/**
 * @author yoshiman
 */
public class HTMLFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        htmlFormatter.setFilename("src/test/resources/html.properties");
        doTestFormat(htmlFormatter, "someFile.html",
                "355c8710f25888c803d010fe5e534e5d0d5056954e3d71681a03de5c0334686267188dd43cca1ab5c5b3268f59f663a2f1814e234f31f1b6dc3c50f5879fe421");
    }

    @Test
    public void testIsIntialized() throws Exception {
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        Assert.assertFalse(htmlFormatter.isInitialized());
        final File targetDir = new File("target/testoutput");
        targetDir.mkdirs();
        htmlFormatter.setFilename("src/test/resources/html.properties");
        htmlFormatter.init(null, null);
        Assert.assertTrue(htmlFormatter.isInitialized());
    }

}
