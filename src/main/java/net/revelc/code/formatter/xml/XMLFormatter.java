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

import java.io.File;
import java.util.Map;

import net.revelc.code.formatter.*;
import net.revelc.code.formatter.jsoup.JsoupBasedFormatter;
import net.revelc.code.formatter.xml.eclipse.FormattingPreferences;
import net.revelc.code.formatter.xml.eclipse.XmlDocumentFormatter;

/**
 * @author yoshiman
 */
public class XMLFormatter implements Formatter {
    private final Formatter delegateFormatter;

    public XMLFormatter(Boolean useJsoupFormatting) {
        if (useJsoupFormatting)
            this.delegateFormatter = new JsoupDelegate();
        else
            this.delegateFormatter = new EclipseAntDelegate();

    }

    @Override
    public void init(Map<String, String> options, ConfigurationSource cfg) {
        delegateFormatter.init(options, cfg);
    }

    @Override
    public Result formatFile(File file, LineEnding ending, boolean dryRun) {
        return delegateFormatter.formatFile(file, ending, dryRun);
    }

    @Override
    public boolean isInitialized() {
        return delegateFormatter.isInitialized();
    }

    // Delegate inner classes
    protected class JsoupDelegate extends JsoupBasedFormatter {
        @Override
        public String doFormat(String code, LineEnding ending) {
            return super.doFormat(code, ending);
        }
    }

    protected class EclipseAntDelegate extends AbstractCacheableFormatter implements Formatter {
        XmlDocumentFormatter formatter;

        @Override
        public void init(Map<String, String> options, ConfigurationSource cfg) {
            super.initCfg(cfg);

            FormattingPreferences prefs = new FormattingPreferences();
            String maxLineLength = options.get("maxLineLength");
            String wrapLongLines = options.get("wrapLongLines");
            String tabInsteadOfSpaces = options.get("tabInsteadOfSpaces");
            String tabWidth = options.get("tabWidth");

            prefs.setMaxLineLength(maxLineLength != null ? Integer.valueOf(maxLineLength) : null);
            prefs.setTabWidth(tabWidth != null ? Integer.valueOf(tabWidth) : null);
            prefs.setWrapLongLines(wrapLongLines != null ? Boolean.valueOf(wrapLongLines) : null);
            prefs.setTabInsteadOfSpaces(tabInsteadOfSpaces != null ? Boolean.valueOf(tabInsteadOfSpaces) : null);

            this.formatter = new XmlDocumentFormatter(options.getOrDefault("lineending", System.lineSeparator()),
                    prefs);
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
}
