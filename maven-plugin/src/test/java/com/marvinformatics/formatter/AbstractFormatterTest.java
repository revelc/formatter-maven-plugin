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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public abstract class AbstractFormatterTest {

	private Formatter formatter;

	@Before
	public void setup() {
		Map<String, String> options = new HashMap<String, String>();
		tuneDefaultConfigs(options);
		final File targetDir = new File("target/testoutput");
		targetDir.mkdirs();
		this.formatter = createFormatter(options, new ConfigurationSource() {

			public File getTargetDirectory() {
				return targetDir;
			}

			public Log getLog() {
				return new SystemStreamLog();
			}

			public Charset getEncoding() {
				return Charsets.UTF_8;
			}

			public String getCompilerSources() {
				return "9";
			}

			public String getCompilerCompliance() {
				return "9";
			}

			public String getCompilerCodegenTargetPlatform() {
				return "9";
			}

			@Override
			public LineEnding lineEnding() {
				return LineEnding.LF;
			}

			@Override
			public boolean isDryRun() {
				return false;
			}
		});
	}

	public void tuneDefaultConfigs(Map<String, String> options) {
	}

	public abstract Formatter createFormatter(Map<String, String> options, ConfigurationSource configurationSource);

	@Test
	public void doTestFormat() throws IOException, NoSuchAlgorithmException {
		File originalSourceFile = new File("src/test/resources/", fileUnderTest());
		File sourceFile = createUnformatedFile(originalSourceFile);

		Result r = formatter.formatFile(sourceFile.toPath());
		assertEquals(Result.SUCCESS, r);

		String originalContent = Files.toString(originalSourceFile, Charsets.UTF_8);
		String formattedContent = Files.toString(sourceFile, Charsets.UTF_8);

		String msg = "Files: \n-" + originalSourceFile.getAbsolutePath() + "\n-" + sourceFile.getAbsolutePath();
		assertEquals(msg, originalContent, formattedContent);

		String expectedSha1 = Files.hash(originalSourceFile, Hashing.sha1()).toString();
		String sha1 = Files.hash(sourceFile, Hashing.sha1()).toString();

		assertEquals(msg, expectedSha1, sha1);
	}

	public abstract String fileUnderTest();

	private File createUnformatedFile(File originalSourceFile) throws IOException {
		File unformatedFile = new File("target/test-classes/", fileUnderTest());

		Files.copy(originalSourceFile, unformatedFile);

		List<String> content = Files.readLines(unformatedFile, Charsets.UTF_8);

		StringBuilder messedContent = new StringBuilder();
		for (String line : content)
			if (line.contains("*"))
				// headers dont reformat that well
				messedContent.append(line).append("\n");
			else
				messedContent.append(randomSeparator()).append(line.trim()).append(randomSeparator());

		Files.write(messedContent, unformatedFile, Charsets.UTF_8);

		return unformatedFile;
	}

	private StringBuilder randomSeparator() {
		Random random = new Random();
		int spaces = random.nextInt(10);

		StringBuilder separator = new StringBuilder();
		for (int i = 0; i < spaces; i++)
			separator.append(' ');

		return separator;
	}

}
