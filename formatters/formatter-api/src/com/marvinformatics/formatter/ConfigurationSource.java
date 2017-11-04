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

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author marvin.froeder
 */
public interface ConfigurationSource {

	String getCompilerSources();

	String getCompilerCompliance();

	String getCompilerCodegenTargetPlatform();

	File getTargetDirectory();

	Charset getEncoding();

	LineEnding lineEnding();

	boolean isDryRun();

	void info(String message);

	void error(String message);

	void debug(String message);

	void warn(String message, File sourceFile, Exception e);

}
