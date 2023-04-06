/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.html;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class HTMLFormatter.
 */
public class HTMLFormatter extends AbstractCacheableFormatter implements Formatter {

    /** The Constant REMOVE_TRAILING_PATTERN. */
    private static final Pattern REMOVE_TRAILING_PATTERN = Pattern.compile("\\p{Blank}+$", Pattern.MULTILINE);

    /** The Constant RESET_LEADING_SPACES_PATTERN. */
    private static final Pattern RESET_LEADING_SPACES_PATTERN = Pattern.compile("^\\s+");

    /** The formatter. */
    private Document.OutputSettings formatter;

    @Override
    public void init(final Map<String, String> options, final ConfigurationSource cfg) {
        super.initCfg(cfg);

        this.formatter = new Document.OutputSettings();
        this.formatter.charset(Charset.forName(options.getOrDefault("charset", StandardCharsets.UTF_8.name())));
        this.formatter.escapeMode(
                Entities.EscapeMode.valueOf(options.getOrDefault("escapeMode", Entities.EscapeMode.xhtml.name())));
        this.formatter.indentAmount(Integer.parseInt(options.getOrDefault("indentAmount", "4")));
        this.formatter.maxPaddingWidth(Integer.parseInt(options.getOrDefault("maxPaddingWidth", "-1")));
        this.formatter.outline(Boolean.parseBoolean(options.getOrDefault("outlineMode", Boolean.TRUE.toString())));
        this.formatter.prettyPrint(Boolean.parseBoolean(options.getOrDefault("pretty", Boolean.TRUE.toString())));
        this.formatter.syntax(Document.OutputSettings.Syntax
                .valueOf(options.getOrDefault("syntax", Document.OutputSettings.Syntax.html.name())));
    }

    @Override
    public String doFormat(String code, final LineEnding ending) {
        Document document;
        if (this.formatter.syntax() != Document.OutputSettings.Syntax.html) {
            throw new IllegalArgumentException(this.formatter.syntax() + " is not allowed as syntax");
        }
        document = Jsoup.parse(code, "", Parser.htmlParser());
        document.outputSettings(this.formatter);

        // Perform Jsoup Pretty Format which does result in inconsistences handled after
        var formattedCode = document.outerHtml();

        // XXX: Trim trailing spaces inserted by jsoup. We do fix this during a full run
        // but unit tests are direct calls thus we are duplicating this until jsoup fixes bug.
        formattedCode = REMOVE_TRAILING_PATTERN.matcher(formattedCode).replaceAll("");

        // XXX: Jsoup results in mixed line ending content needing normalized until jsoup
        // provides line ending support. Internally Jsoup simply uses new line only and
        // mixture comes from lines that did not require additional formatting.
        String[] lines = formattedCode.split("\\r?\\n");
        formattedCode = String.join(ending.getChars(), lines);

        // XXX: Fixing jsoup counter issue when more than one character indentation until jsoup fixes bug.
        // This surfaces on some items such as <a>, <div>, <span> content with 1 extra character space.
        if (this.formatter.indentAmount() > 1) {
            int lineLength;
            int trimLineLength;
            int remainder;
            // This is normalized on line 82/83 and simply can use our line endings
            lines = formattedCode.split(ending.getChars());
            List<String> newLines = new ArrayList<>(lines.length);
            for (String line : lines) {
                lineLength = line.length();
                if (lineLength != 0) {
                    // Trim leading spaces to get trimmed length
                    trimLineLength = RESET_LEADING_SPACES_PATTERN.matcher(line).replaceAll("").length();
                    if (lineLength != trimLineLength) {
                        // Find remainder to correct formatted line
                        remainder = (lineLength - trimLineLength) % this.formatter.indentAmount();
                        if (remainder > 0) {
                            line = line.substring(remainder);
                        }
                    }
                }
                newLines.add(line);
            }
            formattedCode = String.join(ending.getChars(), newLines);
        }

        // XXX: Adding new line at end of file until jsoup fixes bug.
        // This is normalized on line 82/83 and simply can use our line endings
        lines = formattedCode.split(ending.getChars());
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

}
