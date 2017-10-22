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
package net.revelc.code.formatter.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

import java.io.IOException;
import java.util.Map;

/**
 * @author yoshiman
 *
 */
public class JsonFormatter extends AbstractCacheableFormatter implements Formatter {

    private Gson formatter;

    private JsonParser jsonParser;

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        super.initCfg(cfg);

        boolean printPrinting = Boolean.parseBoolean(options.getOrDefault("prettyPrinting", Boolean.TRUE.toString()));

        if (printPrinting) {
            formatter = new GsonBuilder().setPrettyPrinting().create();
        } else {
            formatter = new GsonBuilder().create();
        }
        jsonParser = new JsonParser();
    }

    @Override
    protected String doFormat(String code, LineEnding ending) throws IOException {
        String formattedCode = formatter.toJson(jsonParser.parse(code));
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
