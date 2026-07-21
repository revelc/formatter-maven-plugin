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

package net.revelc.code.formatter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import net.revelc.code.formatter.css.CssFormatter;
import net.revelc.code.formatter.html.HTMLFormatter;
import net.revelc.code.formatter.xml.XMLFormatter;

/**
 * The Class AbstractCacheableFormatter.
 */
public abstract class AbstractCacheableFormatter {

    /** The log. */
    protected Log log;

    /** The encoding. */
    protected Charset encoding;

    /**
     * Inits the AbstractCacheableFormatter.
     *
     * @param options
     *            the options
     * @param cfg
     *            the cfg
     */
    protected abstract void init(Map<String, String> options, ConfigurationSource cfg);

    /**
     * Inits the cfg.
     *
     * @param cfg
     *            the cfg
     */
    protected void initCfg(final ConfigurationSource cfg) {
        this.log = cfg.getLog();
        this.encoding = cfg.getEncoding();
    }

    /**
     * Format file.
     *
     * @param file
     *            the file
     * @param originalCode
     *            the original code
     * @param ending
     *            the ending
     *
     * @return the string
     */
    public String formatFile(final Path file, final String originalCode, final LineEnding ending) {
        try {
            this.log.debug("Processing file: " + file + " with line ending: " + ending);
            var formatterLineEnding = ending;
            // if the line ending is set as KEEP we have to determine the current line ending of the file
            // and let the formatter use this one. Otherwise it would likely fall back to current system line separator
            if (formatterLineEnding == LineEnding.KEEP) {
                formatterLineEnding = LineEnding.determineLineEnding(originalCode);
                this.log.debug("Determined line ending: " + formatterLineEnding + " to keep for file: " + file);
            }
            var formattedCode = this.doFormat(originalCode, formatterLineEnding);

            if (formattedCode == null) {
                this.log.debug("Nothing formatted. Try to fix line endings.");
                formattedCode = AbstractCacheableFormatter.fixLineEnding(originalCode, ending);
            } else if (this instanceof CssFormatter || this instanceof HTMLFormatter || this instanceof XMLFormatter) {
                this.log.debug("Formatted but line endings not supported by tooling. Try to fix line endings.");
                formattedCode = AbstractCacheableFormatter.fixLineEnding(formattedCode, ending);
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

    /**
     * Fix line ending.
     *
     * @param code
     *            the code
     * @param ending
     *            the ending
     *
     * @return the string
     */
    private static String fixLineEnding(String code, final LineEnding ending) {
        if (ending == LineEnding.KEEP) {
            return null;
        }

        // Normalize all line endings
        code = code.replace(LineEnding.CRLF.getChars(), LineEnding.LF.getChars());
        code = code.replace(LineEnding.CR.getChars(), LineEnding.LF.getChars());

        // Replace line endings with chosen style
        return code.replace(LineEnding.LF.getChars(), ending.getChars());
    }

    /**
     * Do format.
     * <p>
     * notice that when calling this function, {@code ending} here MUST equals lineending in config when
     * {@link #init(Map, ConfigurationSource)} init.
     *
     * @param code
     *            the code
     * @param ending
     *            the ending
     *
     * @return the string
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws BadLocationException
     *             the bad location exception
     */
    protected abstract String doFormat(String code, LineEnding ending) throws IOException, BadLocationException;

}
