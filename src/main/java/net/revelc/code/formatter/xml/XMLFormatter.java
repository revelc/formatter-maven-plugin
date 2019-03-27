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
package net.revelc.code.formatter.xml;

import java.util.Map;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;
import net.revelc.code.formatter.xml.lib.FormattingPreferences;
import net.revelc.code.formatter.xml.lib.XmlDocumentFormatter;

/**
 * @author yoshiman
 * @author jam01
 */
public class XMLFormatter extends AbstractCacheableFormatter implements Formatter {
    private XmlDocumentFormatter formatter;

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        super.initCfg(cfg);

        FormattingPreferences prefs = new FormattingPreferences();
        String maxLineLength = options.get("maxLineLength");
        String wrapLongLines = options.get("wrapLongLines");
        String tabInsteadOfSpaces = options.get("tabInsteadOfSpaces");
        String tabWidth = options.get("tabWidth");
        String splitMultiAttrs = options.get("splitMultiAttrs");

        prefs.setMaxLineLength(maxLineLength != null ? Integer.valueOf(maxLineLength) : null);
        prefs.setWrapLongLines(wrapLongLines != null ? Boolean.valueOf(wrapLongLines) : null);
        prefs.setTabInsteadOfSpaces(tabInsteadOfSpaces != null ? Boolean.valueOf(tabInsteadOfSpaces) : null);
        prefs.setTabWidth(tabWidth != null ? Integer.valueOf(tabWidth) : null);
        prefs.setSplitMultiAttrs(splitMultiAttrs != null ? Boolean.valueOf(splitMultiAttrs) : null);

        this.formatter = new XmlDocumentFormatter(options.getOrDefault("lineending", System.lineSeparator()), prefs);
    }

    @Override
    protected String doFormat(String code, LineEnding ending) {
        String formattedCode = formatter.format(code);

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
