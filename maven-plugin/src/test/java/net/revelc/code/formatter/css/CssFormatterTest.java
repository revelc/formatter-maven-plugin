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
package net.revelc.code.formatter.css;

import net.revelc.code.formatter.AbstractFormatterTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author yoshiman
 */
public class CssFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        CssFormatter cssFormatter = new CssFormatter();
        cssFormatter.setFilename("src/test/resources/css.properties");
        doTestFormat(cssFormatter, "someFile.css",
                "2e3a00647508e528051f607b29e7e2dc5dc7ba12c1b75b746f490676f51ca27c1c0f26c233b94804d7656434d65138249530bf0969cff4a7e9c2baef0f3c9294");
    }

    @Test
    public void testIsIntialized() throws Exception {
        CssFormatter cssFormatter = new CssFormatter();
        Assert.assertFalse(cssFormatter.isInitialized());
        final File targetDir = new File("target/testoutput");
        targetDir.mkdirs();
        cssFormatter.setFilename("src/test/resources/css.properties");
        cssFormatter.init(null, null);

        Assert.assertTrue(cssFormatter.isInitialized());
    }

}
