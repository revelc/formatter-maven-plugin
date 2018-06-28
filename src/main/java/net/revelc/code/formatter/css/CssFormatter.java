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
package net.revelc.code.formatter.css;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.w3c.css.sac.InputSource;

import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

/**
 * @author yoshiman
 *
 */
public class CssFormatter extends AbstractCacheableFormatter implements Formatter {

    private CSSFormat formatter;

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        super.initCfg(cfg);

        int indent = Integer.parseInt(options.getOrDefault("indent", "4"));
        boolean rgbAsHex = Boolean.parseBoolean(options.getOrDefault("rgbAsHex", Boolean.TRUE.toString()));
        formatter = new CSSFormat().setPropertiesInSeparateLines(indent).setRgbAsHex(rgbAsHex);
    }

    @Override
    protected String doFormat(String code, LineEnding ending) throws IOException {

        InputSource source = new InputSource(new StringReader(code));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheetImpl sheet = (CSSStyleSheetImpl) parser.parseStyleSheet(source, null, null);
        String formattedCode = sheet.getCssText(formatter);

        if (code.equals(formattedCode)) {
            return null;
        }
        return formattedCode;
    }

    @Override
    public boolean isInitialized() {
        return formatter != null;
    }

}
