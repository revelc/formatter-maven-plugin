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

import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;
import org.w3c.css.sac.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * @author yoshiman
 *
 */
public class CssFormatter extends AbstractCacheableFormatter implements Formatter {
    private String filename;
    private CSSFormat format;

    @Override
    public boolean isInitialized() {
        return filename != null && format != null;
    }

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        if (cfg != null) {
            super.initCfg(cfg);
        }
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(filename)));
        } catch (IOException e) {
            this.log.error("error while reading properties file", e);
        }
        int indent = Integer.parseInt(properties.getProperty("indent", "2"));
        boolean rgbAsHex = Boolean.parseBoolean(properties.getProperty("rgbAsHex", Boolean.toString(true)));
        format = new CSSFormat();
        format.setPropertiesInSeparateLines(indent);
        format.setRgbAsHex(rgbAsHex);
    }

    @Override
    protected String doFormat(String code, LineEnding ending) throws IOException {

        InputSource source = new InputSource(new StringReader(code));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheetImpl sheet = (CSSStyleSheetImpl) parser.parseStyleSheet(source, null, null);
        String formattedCode = sheet.getCssText(format);

        if (code.equals(formattedCode)) {
            return null;
        }
        return formattedCode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
