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
package net.revelc.code.formatter.xml;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import net.revelc.code.formatter.AbstractFormatterTest;

/**
 * @author yoshiman
 */
public class XMLFormatterTest extends AbstractFormatterTest {

    @Test
    public void testDoFormatFile() throws Exception {
        XMLFormatter xmlFormatterFormatter = new XMLFormatter();
        xmlFormatterFormatter.setFilename("src/test/resources/xml.properties");
        doTestFormat(xmlFormatterFormatter, "someFile.xml",
                "7066679c7aa8e064c7a1f77e76285759c67d5884bef351cd0aa50e0245abc984612c832cbd4fdbe7a1b303e679a1207c42f5c9fb16094391fa0b7045662b2127");
    }

    @Test
    public void testIsIntialized() throws Exception {
        XMLFormatter xmlFormatter = new XMLFormatter();
        Assert.assertFalse(xmlFormatter.isInitialized());
        final File targetDir = new File("target/testoutput");
        targetDir.mkdirs();
        xmlFormatter.setFilename("src/test/resources/xml.properties");
        xmlFormatter.init(null, null);
        Assert.assertTrue(xmlFormatter.isInitialized());
    }

}
