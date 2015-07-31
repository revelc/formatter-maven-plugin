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
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;
import net.revelc.code.formatter.Result;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public abstract class AbstractFormatterTest extends TestCase {

	protected void doTestFormat(Formatter formatter, String fileUnderTest,
			String expectedSha1) throws IOException, NoSuchAlgorithmException {
		File originalSourceFile = new File("src/test/resources/", fileUnderTest);
		File sourceFile = new File("target/test-classes/", fileUnderTest);

		Files.copy(originalSourceFile, sourceFile);

		Map<String, String> options = new HashMap<String, String>();
		final File targetDir = new File("target/testoutput");
		targetDir.mkdirs();
		formatter.init(options, new ConfigurationSource() {

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
				return "1.8";
			}

			public String getCompilerCompliance() {
				return "1.8";
			}

			public String getCompilerCodegenTargetPlatform() {
				return "1.8";
			}
		});
		Result r = formatter.formatFile(sourceFile, LineEnding.CRLF, false);
		assertEquals(Result.SUCCESS, r);

		byte[] sha1 = Files.hash( sourceFile, Hashing.sha1()).asBytes();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < sha1.length; i++) {
			sb.append(Integer.toString((sha1[i] & 0xff) + 0x100, 16).substring(
					1));
		}

		assertEquals(expectedSha1, sb.toString());
	}

}
