package com.relativitas.maven.plugins.formatter.java;

import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.relativitas.maven.plugins.formatter.ConfigurationSource;
import com.relativitas.maven.plugins.formatter.LineEnding;
import com.relativitas.maven.plugins.formatter.Result;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends TestCase {

	public void testDoFormatFile() throws Exception {
		File originalJavaFile = new File("src/test/resources/AnyClass.java");
		File javaFile = new File("target/test-classes/AnyClass.java");

		Files.copy(originalJavaFile, javaFile);

		JavaFormatter jf = new JavaFormatter();
		Map<String, String> options = new HashMap<String, String>();
		final File targetDir = new File("target/testoutput");
		targetDir.mkdirs();
		jf.init(options, new ConfigurationSource() {

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
		Result r = jf.doFormatFile(javaFile, LineEnding.CRLF);
		assertEquals(Result.SUCCESS, r);

		byte[] sha1 = Files.getDigest(javaFile,
				MessageDigest.getInstance("sha1"));
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < sha1.length; i++) {
			sb.append(Integer.toString((sha1[i] & 0xff) + 0x100, 16).substring(
					1));
		}

		assertEquals("d9c0aec7b7f350f19089258ba86aeaf92f39669b", sb.toString());
	}

}
