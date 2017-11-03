/**
 * Copyright (C) 2010 Marvin Herman Froeder (marvin@marvinformatics.com)
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
package com.marvinformatics.formatter;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.logging.Log;

/**
 * @author marvin.froeder
 */
public abstract class AbstractCacheableFormatter {

	protected final Log log;

	protected final ConfigurationSource configurationSource;

	public AbstractCacheableFormatter(ConfigurationSource cfg) {
		this.configurationSource = cfg;
		this.log = cfg.getLog();
	}

	public Result formatFile(Path file) {
		try {
			log.debug("Processing file: " + file);
			String code = new String(Files.readAllBytes(file), configurationSource.getEncoding());
			String formattedCode = fixLineEnding(doFormat(code));

			if (code.equals(formattedCode)) {
				log.debug("Equal code. Not writing result to file.");
				return Result.SKIPPED;
			}

			if (!configurationSource.isDryRun())
				Files.write(file, formattedCode.getBytes(configurationSource.getEncoding()));

			return Result.SUCCESS;
		} catch (Exception e) {
			log.warn("Error formating: " + file.toAbsolutePath(), e);
			return Result.FAIL;
		}
	}

	private String fixLineEnding(String code) {
		return configurationSource.lineEnding().fix(code);
	}

	protected abstract String doFormat(String code);

}
