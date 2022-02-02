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
package net.revelc.code.formatter.jsoup;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.parser.Parser;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JsoupBasedFormatter.
 */
public abstract class JsoupBasedFormatter extends AbstractCacheableFormatter implements Formatter {

    /** The formatter. */
    private OutputSettings formatter;

    @Override
    public void init(final Map<String, String> options, final ConfigurationSource cfg) {
        super.initCfg(cfg);

        formatter = new OutputSettings();
        formatter.charset(Charset.forName(options.getOrDefault("charset", StandardCharsets.UTF_8.name())));
        formatter.escapeMode(EscapeMode.valueOf(options.getOrDefault("escapeMode", EscapeMode.xhtml.name())));
        formatter.indentAmount(Integer.parseInt(options.getOrDefault("indentAmount", "4")));
        formatter.maxPaddingWidth(Integer.parseInt(options.getOrDefault("maxPaddingWidth", "-1")));
        formatter.outline(Boolean.parseBoolean(options.getOrDefault("outlineMode", Boolean.TRUE.toString())));
        formatter.prettyPrint(Boolean.parseBoolean(options.getOrDefault("pretty", Boolean.TRUE.toString())));
        formatter.syntax(Syntax.valueOf(options.getOrDefault("syntax", Syntax.html.name())));
    }

    @Override
    public String doFormat(final String code, final LineEnding ending) {
        Document document;
        if (formatter.syntax() == Syntax.html) {
            document = Jsoup.parse(code, "", Parser.htmlParser());
        } else {
            throw new IllegalArgumentException(formatter.syntax() + " is not allowed as syntax");
        }
        document.outputSettings(formatter);

        String formattedCode = document.outerHtml();
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
