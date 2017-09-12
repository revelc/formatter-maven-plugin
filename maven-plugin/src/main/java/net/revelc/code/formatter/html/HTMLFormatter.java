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
package net.revelc.code.formatter.html;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.Entities.EscapeMode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * @author yoshiman
 *
 */
public class HTMLFormatter extends AbstractCacheableFormatter implements Formatter {

    private String filename;

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        if (cfg != null) {
            super.initCfg(cfg);
        }
    }

    @Override
    public String doFormat(String code, LineEnding ending) throws UnsupportedEncodingException {
        Document document = Jsoup.parse(code);
        try {
            Properties properties = new Properties();
            properties.load(Files.newInputStream(Paths.get(filename)));
            Charset charset = Charset.forName(properties.getProperty("charset", StandardCharsets.UTF_8.name()));
            EscapeMode escapeMode = EscapeMode.valueOf(properties.getProperty("escapeMode", EscapeMode.xhtml.name()));
            int indentAmount = Integer.parseInt(properties.getProperty("indentAmount", "1"));
            boolean outlineMode = Boolean.parseBoolean(properties.getProperty("outlineMode", Boolean.toString(true)));
            boolean pretty = Boolean.parseBoolean(properties.getProperty("pretty", Boolean.toString(true)));
            Syntax syntax = Syntax.html;
            document.outputSettings() //
                    .charset(charset) //
                    .escapeMode(escapeMode) //
                    .indentAmount(indentAmount) //
                    .outline(outlineMode) //
                    .prettyPrint(pretty) //
                    .syntax(syntax);

        } catch (IOException e) {
            this.log.error(e);
        }

        String formattedCode = document.outerHtml();
        if (code.equals(formattedCode)) {
            return null;
        }
        return formattedCode;
    }

    @Override
    public boolean isInitialized() {
        return filename != null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
