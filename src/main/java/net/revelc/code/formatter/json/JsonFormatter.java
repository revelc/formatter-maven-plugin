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
package net.revelc.code.formatter.json;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

/**
 * @author yoshiman
 *
 */
public class JsonFormatter extends AbstractCacheableFormatter implements Formatter {

    private ObjectMapper formatter;

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        super.initCfg(cfg);

        int indent = Integer.parseInt(options.getOrDefault("indent", "4"));
        String lineEnding = options.getOrDefault("lineending", System.lineSeparator());
        boolean spaceBeforeSeparator = Boolean.parseBoolean(options.getOrDefault("spaceBeforeSeparator", "true"));

        formatter = new ObjectMapper();

        // Setup a pretty printer with an indenter (indenter has 4 spaces in this case)
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(Strings.repeat(" ", indent), lineEnding);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter() {
            private static final long serialVersionUID = 1L;

            @Override
            public DefaultPrettyPrinter createInstance() {
                return new DefaultPrettyPrinter(this);
            }

            @Override
            public DefaultPrettyPrinter withSeparators(Separators separators) {
                this._separators = separators;
                this._objectFieldValueSeparatorWithSpaces = (spaceBeforeSeparator ? " " : "")
                        + separators.getObjectFieldValueSeparator() + " ";
                return this;
            }
        };

        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
        formatter.setDefaultPrettyPrinter(printer);
        formatter.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected String doFormat(String code, LineEnding ending) throws IOException {
        // note: line ending set in init for this usecase
        Object json = formatter.readValue(code, Object.class);
        String formattedCode = formatter.writer().writeValueAsString(json);
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
