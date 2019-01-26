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

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author marvin.froeder
 */
public abstract class AbstractCacheableFormatter
{
    protected Logger logger = LoggerFactory.getLogger(AbstractCacheableFormatter.class);

    protected Charset encoding;

    protected abstract void init(Map<String, String> options, ConfigurationSource cfg);

    protected void initCfg(ConfigurationSource cfg)
    {
        this.encoding = cfg.getEncoding();
    }

    public Result formatFile(File file, LineEnding ending, boolean dryRun)
    {
        try
        {
            this.logger.debug("Processing file: " + file + " with line ending: " + ending);
            String code = fileRead(file, this.encoding.name());
            String formattedCode = doFormat(code, ending);

            if (formattedCode == null)
            {
                this.logger.debug("Nothing formatted. Try to fix line endings.");
                formattedCode = fixLineEnding(code, ending);
            }

            if (formattedCode == null)
            {
                this.logger.debug("Equal code. Not writing result to file.");
                return Result.SKIPPED;
            }

            if (!dryRun)
            {
                fileWrite(file, this.encoding.name(), formattedCode);
            }

            // readme: Uncomment this when having build issues with hashCodes when nothing
            // changed. The issue is likely copyright dating issues.
            // this.logger.debug("formatted code: " + formattedCode);
            return Result.SUCCESS;
        }
        catch (IOException | MalformedTreeException | BadLocationException e)
        {
            this.logger.warn("Could not format the file", e);
            return Result.FAIL;
        }
    }

    private static String fixLineEnding(String code, LineEnding ending)
    {
        if (ending == LineEnding.KEEP)
        {
            return null;
        }

        LineEnding current = LineEnding.determineLineEnding(code);
        if (current == LineEnding.UNKNOWN)
        {
            return null;
        }
        if (current == ending)
        {
            return null;
        }
        if (ending == LineEnding.AUTO && Objects.equals(current.getChars(), ending.getChars()))
        {
            return null;
        }

        return code.replace(current.getChars(), ending.getChars());
    }

    public String fileRead(File file, String encoding) throws IOException
    {
        StringBuilder buf = new StringBuilder();
        InputStreamReader reader = null;

        try
        {
            if (encoding != null)
            {
                reader = new InputStreamReader(new FileInputStream(file), encoding);
            }
            else
            {
                reader = new InputStreamReader(new FileInputStream(file));
            }

            char[] b = new char[512];

            int count;
            while ((count = reader.read(b)) >= 0)
            {
                buf.append(b, 0, count);
            }

            reader.close();
            reader = null;
            return buf.toString();
        }
        finally
        {
            close(reader);
        }
    }

    private void close(InputStreamReader reader)
    {
        if (reader != null)
        {
            try
            {
                reader.close();
            }
            catch (IOException var2)
            {
                ;
            }

        }
    }

    public static void fileWrite(File file, String encoding, String data) throws IOException
    {
        OutputStreamWriter writer = null;

        try
        {
            OutputStream out = new FileOutputStream(file);
            if (encoding != null)
            {
                writer = new OutputStreamWriter(out, encoding);
            }
            else
            {
                writer = new OutputStreamWriter(out);
            }

            writer.write(data);
            writer.close();
            writer = null;
        }
        finally
        {
            close(writer);
        }

    }

    public static void close(Writer writer)
    {
        if (writer != null)
        {
            try
            {
                writer.close();
            }
            catch (IOException var2)
            {
                ;
            }

        }
    }

    protected abstract String doFormat(String code, LineEnding ending) throws IOException, BadLocationException;

}
