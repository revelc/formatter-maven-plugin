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

package net.revelc.code.formatter.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JsonFormatter.
 */
public class JsonFormatter extends AbstractCacheableFormatter implements Formatter {

    /** The formatter. */
    private ObjectMapper formatter;

    private static final Pattern ANY_EOL = Pattern.compile("\\R");

    /** The configuration options */
    private Map<String, String> options;

    @Override
    public void init(final Map<String, String> options, final ConfigurationSource cfg) {
        super.initCfg(cfg);

        final var indent = Integer.parseInt(options.getOrDefault("indent", "4"));
        final var lineEnding = options.getOrDefault("lineending", System.lineSeparator());
        final var spaceBeforeSeparator = Boolean.parseBoolean(options.getOrDefault("spaceBeforeSeparator", "true"));
        final var useAlphabeticalOrder = Boolean.parseBoolean(options.getOrDefault("alphabeticalOrder", "false"));

        // Setup a pretty printer with an indenter (indenter has 4 spaces in this case)
        final DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(" ".repeat(indent), lineEnding);
        final DefaultPrettyPrinter printer = new DefaultPrettyPrinter() {
            private static final long serialVersionUID = 1L;

            @Override
            public DefaultPrettyPrinter createInstance() {
                return new DefaultPrettyPrinter(this);
            }

            @Override
            public DefaultPrettyPrinter withSeparators(final Separators separators) {
                this._separators = separators;
                this._objectFieldValueSeparatorWithSpaces = (spaceBeforeSeparator ? " " : "")
                        + separators.getObjectFieldValueSeparator() + " ";
                return this;
            }
        };

        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);

        this.formatter = new ObjectMapper();
        this.formatter.setDefaultPrettyPrinter(printer);
        this.formatter.enable(SerializationFeature.INDENT_OUTPUT);
        this.formatter.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, useAlphabeticalOrder);
        this.options = options;
    }

    @Override
    protected String doFormat(final String code, final LineEnding ending) throws IOException {
        try (var stringWriter = new StringWriter()) {
            var jsonParser = this.formatter.createParser(code);
            final Iterator<Object> jsonObjectIterator = this.formatter.readValues(jsonParser, Object.class);
            while (jsonObjectIterator.hasNext()) {
                var jsonString = this.formatter.writer().writeValueAsString(jsonObjectIterator.next());
                stringWriter.write(ANY_EOL.matcher(jsonString.strip()).replaceAll(ending.getChars()));
                stringWriter.write(ending.getChars());
            }
            var formattedCode = stringWriter.toString();
            return code.equals(formattedCode) ? null : formattedCode;
        }
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
