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
package net.revelc.code.formatter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

/**
 * @author marvin.froeder
 */
public abstract class AbstractCacheableFormatter {

    protected Log log;

    protected Charset encoding;

    protected abstract void init(Map<String, String> options, ConfigurationSource cfg);

    protected void initCfg(ConfigurationSource cfg) {
        this.log = cfg.getLog();
        this.encoding = cfg.getEncoding();
    }

    public String formatFile(File file, String originalCode, LineEnding ending) {
        try {
            this.log.debug("Processing file: " + file + " with line ending: " + ending);
            LineEnding formatterLineEnding = ending;
            // if the line ending is set as KEEP we have to determine the current line ending of the file
            // and let the formatter use this one. Otherwise it would likely fall back to current system line separator
            if (formatterLineEnding == LineEnding.KEEP) {
                formatterLineEnding = LineEnding.determineLineEnding(originalCode);
                this.log.debug("Determined line ending: " + formatterLineEnding + " to keep for file: " + file);
            }
            String formattedCode = doFormat(originalCode, formatterLineEnding);

            if (formattedCode == null) {
                this.log.debug("Nothing formatted. Try to fix line endings.");
                formattedCode = fixLineEnding(originalCode, ending);
            }

            if (formattedCode == null) {
                this.log.debug("Equal code. Not writing result to file.");
                return originalCode;
            }

            this.log.debug("Line endings fixed");

            return formattedCode;
        } catch (IOException | MalformedTreeException | BadLocationException e) {
            this.log.warn(e);
            return null;
        }
    }

    private static String fixLineEnding(String code, LineEnding ending) {
        if (ending == LineEnding.KEEP) {
            return null;
        }

        LineEnding current = LineEnding.determineLineEnding(code);
        if (current == LineEnding.UNKNOWN) {
            return null;
        }
        if (current == ending) {
            return null;
        }
        if (ending == LineEnding.AUTO && Objects.equals(current.getChars(), ending.getChars())) {
            return null;
        }

        return code.replace(current.getChars(), ending.getChars());
    }

    protected abstract String doFormat(String code, LineEnding ending) throws IOException, BadLocationException;

}
