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
import org.codehaus.plexus.util.FileUtils;
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

    public Result formatFile(File file, LineEnding ending, boolean dryRun) {
        try {
            this.log.debug("Processing file: " + file + " with line ending: " + ending);
            String code = FileUtils.fileRead(file, this.encoding.name());
            String formattedCode = doFormat(code, ending);

            if (formattedCode == null) {
                this.log.debug("Nothing formatted. Try to fix line endings.");
                formattedCode = fixLineEnding(code, ending);
            }

            if (formattedCode == null) {
                this.log.debug("Equal code. Not writing result to file.");
                return Result.SKIPPED;
            }

            if (!dryRun) {
                FileUtils.fileWrite(file, this.encoding.name(), formattedCode);
            }

            // readme: Uncomment this when having build issues with hashCodes when nothing
            // changed. The issue is likely copyright dating issues.
            // this.log.debug("formatted code: " + formattedCode);
            return Result.SUCCESS;
        } catch (IOException | MalformedTreeException | BadLocationException e) {
            this.log.warn(e);
            return Result.FAIL;
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
