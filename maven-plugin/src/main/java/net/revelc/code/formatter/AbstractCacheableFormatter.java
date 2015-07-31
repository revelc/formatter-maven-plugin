/**
 * Copyright 2010-2014. All work is copyrighted to their respective
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
package net.revelc.code.formatter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

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

	public AbstractCacheableFormatter() {
		super();
	}

	protected abstract void init(Map<String, String> options,
			ConfigurationSource cfg);

	protected void initCfg(ConfigurationSource cfg) {
		this.log = cfg.getLog();
		this.encoding = cfg.getEncoding();
	}

	public Result formatFile(File file, LineEnding ending, boolean dryRun) {
		try {
			log.debug("Processing file: " + file);
			String code = FileUtils.fileRead(file, encoding.name());
			String formattedCode = doFormat(code, ending);

			if (formattedCode == null)
				formattedCode = fixLineEnding(code, ending);

			if (formattedCode == null) {
				log.debug("Equal code. Not writing result to file.");
				return Result.SKIPPED;
			}

			if (!dryRun)
				FileUtils.fileWrite(file, encoding.name(), formattedCode);

			return Result.SUCCESS;
		} catch (IOException e) {
			log.warn(e);
			return Result.FAIL;
		} catch (MalformedTreeException e) {
			log.warn(e);
			return Result.FAIL;
		} catch (BadLocationException e) {
			log.warn(e);
			return Result.FAIL;
		}
	}

	private String fixLineEnding(String code, LineEnding ending) {
		if (ending == LineEnding.KEEP)
			return null;

		LineEnding current = LineEnding.determineLineEnding(code);
		if (current == LineEnding.UNKNOW)
			return null;
		if (current == ending)
			return null;

		return code.replace(current.getChars(), ending.getChars());
	}

	protected abstract String doFormat(String code, LineEnding ending)
			throws IOException, BadLocationException;

}