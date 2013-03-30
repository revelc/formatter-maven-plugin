package com.relativitas.maven.plugins.formatter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.google.common.base.Charsets;
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
				return "1.5";
			}

			public String getCompilerCompliance() {
				return "1.5";
			}

			public String getCompilerCodegenTargetPlatform() {
				return "1.5";
			}
		});
		Result r = formatter.formatFile(sourceFile, LineEnding.CRLF);
		assertEquals(Result.SUCCESS, r);

		byte[] sha1 = Files.getDigest(sourceFile,
				MessageDigest.getInstance("sha1"));
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < sha1.length; i++) {
			sb.append(Integer.toString((sha1[i] & 0xff) + 0x100, 16).substring(
					1));
		}

		assertEquals(expectedSha1, sb.toString());
	}

}
