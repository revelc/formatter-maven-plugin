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
 * The Class CssFormatter.
 */
public class CssFormatter extends AbstractCacheableFormatter implements Formatter {

    /** The formatter. */
    private CSSFormat formatter;

    /** The configuration options */
    private Map<String, String> options;

    @Override
    public void init(final Map<String, String> options, final ConfigurationSource cfg) {
        super.initCfg(cfg);

        final var indent = Integer.parseInt(options.getOrDefault("indent", "4"));
        final var rgbAsHex = Boolean.parseBoolean(options.getOrDefault("rgbAsHex", Boolean.TRUE.toString()));
        final var useSourceStringValues = Boolean
                .parseBoolean(options.getOrDefault("useSourceStringValues", Boolean.FALSE.toString()));
        this.formatter = new CSSFormat().setPropertiesInSeparateLines(indent).setRgbAsHex(rgbAsHex)
                .setUseSourceStringValues(useSourceStringValues);
        this.options = options;
    }

    @Override
    protected String doFormat(final String code, final LineEnding ending) throws IOException {

        final var source = new InputSource(new StringReader(code));
        final var parser = new CSSOMParser(new SACParserCSS3());
        final var sheet = (CSSStyleSheetImpl) parser.parseStyleSheet(source, null, null);
        var formattedCode = sheet.getCssText(this.formatter);

        // Patch converted 'tab' back to '\9' for IE 7,8, and 9 hack. Cssparser switches it to 'tab'.
        formattedCode = formattedCode.replace("\t;", "\\9;");

        // Adding new line at end of file when needed
        var lines = formattedCode.split(ending.getChars(), -1);
        if (!lines[lines.length - 1].equals(ending.getChars())) {
            formattedCode = formattedCode + ending.getChars();
        }

        if (code.equals(formattedCode)) {
            return null;
        }
        return formattedCode;
    }

    @Override
    public boolean isInitialized() {
        return this.formatter != null;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public Map<String, String> getOptions() {
        return options;
    }

}
