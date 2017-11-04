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
import java.util.function.Function;

public class CacheableFormatter {

	private final Formatter formatter;
	private final ConfigurationSource context;

	public CacheableFormatter(ConfigurationSource cfg, Formatter formatter) {
		this.context = cfg;

		this.formatter = formatter;
	}

	public Result formatFile(Path file) {
		try {
			context.debug("Processing file: " + file);
			String code = new String(Files.readAllBytes(file), context.getEncoding());
			String formattedCode = fixLineEnding(formatter.format(code));

			if (code.equals(formattedCode)) {
				context.debug("Equal code. Not writing result to file.");
				return Result.SKIPPED;
			}

			if (!context.isDryRun())
				Files.write(file, formattedCode.getBytes(context.getEncoding()));

			return Result.SUCCESS;
		} catch (Exception e) {
			context.warn("Error formating: ", file.toFile().getAbsoluteFile(), e);
			return Result.FAIL;
		}
	}

	private String fixLineEnding(String code) {
		return context.lineEnding().fix(code);
	}

}
