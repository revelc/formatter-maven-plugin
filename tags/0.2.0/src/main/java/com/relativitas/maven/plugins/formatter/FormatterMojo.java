package com.relativitas.maven.plugins.formatter;

/*
 * Copyright 2010. All work is copyrighted to their respective author(s),
 * unless otherwise stated.
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.resource.ResourceManager;
import org.codehaus.plexus.resource.loader.FileResourceLoader;
import org.codehaus.plexus.resource.loader.ResourceNotFoundException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.xml.sax.SAXException;

/**
 * A Maven plugin mojo to format Java source code using the Eclipse code
 * formatter.
 * 
 * Mojo parameters allow customizing formatting by specifying the config XML
 * file, line endings, compiler version, and source code locations. Reformatting
 * source files is avoided using an md5 hash of the content, comparing to the
 * original hash to the hash after formatting and a cached hash.
 * 
 * @goal format
 * @phase process-sources
 * 
 * @author jecki
 * @author Matt Blanchette
 */
public class FormatterMojo extends AbstractMojo {
	private static final String CACHE_PROPERTIES_FILENAME = "maven-java-formatter-cache.properties";

	static final String LINE_ENDING_AUTO = "AUTO";
	static final String LINE_ENDING_KEEP = "KEEP";
	static final String LINE_ENDING_LF = "LF";
	static final String LINE_ENDING_CRLF = "CRLF";
	static final String LINE_ENDING_CR = "CR";

	static final String LINE_ENDING_LF_CHAR = "\n";
	static final String LINE_ENDING_CRLF_CHARS = "\r\n";
	static final String LINE_ENDING_CR_CHAR = "\r";

	/**
	 * ResourceManager for retrieving the configFile resource.
	 * 
	 * @component
	 * @required
	 * @readonly
	 */
	private ResourceManager resourceManager;

	/**
	 * Project's source directory as specified in the POM.
	 * 
	 * @parameter expression="${project.build.sourceDirectory}"
	 * @readonly
	 * @required
	 */
	private File sourceDirectory;

	/**
	 * Project's test source directory as specified in the POM.
	 * 
	 * @parameter expression="${project.build.testSourceDirectory}"
	 * @readonly
	 * @required
	 */
	private File testSourceDirectory;

	/**
	 * Project's target directory as specified in the POM.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @readonly
	 * @required
	 */
	private File targetDirectory;

	/**
	 * Project's base directory.
	 * 
	 * @parameter expression="${basedir}"
	 * @readonly
	 * @required
	 */
	private File basedir;

	/**
	 * Location of the Java source files to format.
	 * 
	 * @parameter
	 */
	private File[] directories;

	/**
	 * Java compiler source version.
	 * 
	 * @parameter default-value="1.5"
	 */
	private String compilerSource;

	/**
	 * Java compiler compliance version.
	 * 
	 * @parameter default-value="1.5"
	 */
	private String compilerCompliance;

	/**
	 * Java compiler target version.
	 * 
	 * @parameter default-value="1.5"
	 */
	private String compilerTargetPlatform;

	/**
	 * Sets the line-ending of files after formatting. Valid values are:
	 * <ul>
	 * <li><b>"AUTO"</b> - Use line endings of current system</li>
	 * <li><b>"KEEP"</b> - Preserve line endings of files, default to AUTO if
	 * ambiguous</li>
	 * <li><b>"LF"</b> - Use Unix and Mac style line endings</li>
	 * <li><b>"CRLF"</b> - Use DOS and Windows style line endings</li>
	 * <li><b>"CR"</b> - Use early Mac style line endings</li>
	 * </ul>
	 * 
	 * @parameter default-value="AUTO"
	 */
	private String lineEnding;

	/**
	 * File or classpath location of an Eclipse code formatter configuration xml
	 * file to use in formatting.
	 * 
	 * @parameter
	 */
	private String configFile;

	/**
	 * Sets whether compilerSource, compilerCompliance, and
	 * compilerTargetPlatform values are used instead of those defined in the
	 * configFile.
	 * 
	 * @parameter default-value="false"
	 */
	private Boolean overrideConfigCompilerVersion;

	private CodeFormatter formatter;

	/**
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		long startClock = System.currentTimeMillis();

		if (!LINE_ENDING_AUTO.equals(lineEnding)
				&& !LINE_ENDING_KEEP.equals(lineEnding)
				&& !LINE_ENDING_LF.equals(lineEnding)
				&& !LINE_ENDING_CRLF.equals(lineEnding)
				&& !LINE_ENDING_CR.equals(lineEnding)) {
			throw new MojoExecutionException(
					"Unknown value for lineEnding parameter");
		}

		if (directories == null) {
			directories = new File[] { sourceDirectory, testSourceDirectory };
		}

		List files = new ArrayList();
		probeFiles(directories, files);
		int numberOfFiles = files.size();
		Log log = getLog();
		log.info("Number of files to be formatted: " + numberOfFiles);

		createCodeFormatter();
		ResultCollector rc = new ResultCollector();
		Properties hashCache = readFileHashCacheFile();

		String basedirPath = getBasedirPath();
		for (int i = 0, n = files.size(); i < n; i++) {
			File file = (File) files.get(i);
			formatFile(file, rc, hashCache, basedirPath);
		}

		storeFileHashCache(hashCache);

		long endClock = System.currentTimeMillis();

		log.info("Successfully formatted: " + rc.successCount + " file(s)");
		log.info("Fail to format        : " + rc.failCount + " file(s)");
		log.info("Skipped               : " + rc.skippedCount + " file(s)");
		log.info("Approximate time taken: " + ((endClock - startClock) / 1000)
				+ "s");
	}

	private String getBasedirPath() {
		try {
			return basedir.getCanonicalPath();
		} catch (Exception e) {
			return "";
		}
	}

	private void storeFileHashCache(Properties props) {
		File cacheFile = new File(targetDirectory, CACHE_PROPERTIES_FILENAME);
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					cacheFile));
			props.store(out, null);
		} catch (FileNotFoundException e) {
			getLog().warn("Cannot store file hash cache properties file", e);
		} catch (IOException e) {
			getLog().warn("Cannot store file hash cache properties file", e);
		}
	}

	private Properties readFileHashCacheFile() {
		Properties props = new Properties();
		Log log = getLog();
		if (!targetDirectory.exists()) {
			targetDirectory.mkdirs();
		} else if (!targetDirectory.isDirectory()) {
			log.warn("Something strange here as the "
					+ "supposedly target directory is not a directory.");
			return props;
		}

		File cacheFile = new File(targetDirectory, CACHE_PROPERTIES_FILENAME);
		if (!cacheFile.exists()) {
			return props;
		}

		try {
			props.load(new BufferedInputStream(new FileInputStream(cacheFile)));
		} catch (FileNotFoundException e) {
			log.warn("Cannot load file hash cache properties file", e);
		} catch (IOException e) {
			log.warn("Cannot load file hash cache properties file", e);
		}
		return props;
	}

	/**
	 * @param file
	 * @param rc
	 * @param hashCache
	 * @param basedirPath
	 */
	private void formatFile(File file, ResultCollector rc,
			Properties hashCache, String basedirPath) {
		try {
			doFormatFile(file, rc, hashCache, basedirPath);
		} catch (IOException e) {
			rc.failCount++;
			getLog().warn(e);
		} catch (MalformedTreeException e) {
			rc.failCount++;
			getLog().warn(e);
		} catch (BadLocationException e) {
			rc.failCount++;
			getLog().warn(e);
		}
	}

	/**
	 * Format individual file.
	 * 
	 * @param file
	 * @param rc
	 * @param hashCache
	 * @param basedirPath
	 * @throws IOException
	 * @throws BadLocationException
	 */
	private void doFormatFile(File file, ResultCollector rc,
			Properties hashCache, String basedirPath) throws IOException,
			BadLocationException {
		Log log = getLog();
		log.debug("Processing file: " + file);
		String code = readFileAsString(file);
		String originalHash = md5hash(code);

		String canonicalPath = file.getCanonicalPath();
		String path = canonicalPath.substring(basedirPath.length());
		String cachedHash = hashCache.getProperty(path);
		if (cachedHash != null && cachedHash.equals(originalHash)) {
			rc.skippedCount++;
			log.debug("File is already formatted.");
			return;
		}

		String lineSeparator = getLineEnding(code);

		TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT, code,
				0, code.length(), 0, lineSeparator);
		if (te == null) {
			rc.skippedCount++;
			log.debug("Code cannot be formatted. Possible cause "
					+ "is unmatched source/target/compliance version.");
			return;
		}

		IDocument doc = new Document(code);
		te.apply(doc);
		String formattedCode = doc.get();
		String formattedHash = md5hash(formattedCode);
		hashCache.setProperty(path, formattedHash);

		if (originalHash.equals(formattedHash)) {
			rc.skippedCount++;
			log.debug("Equal hash code. Not writing result to file.");
			return;
		}

		writeStringToFile(formattedCode, file);
		rc.successCount++;
	}

	/**
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String md5hash(String str) throws UnsupportedEncodingException {
		return DigestUtils.md5Hex(str.getBytes("ISO8859_1"));
	}

	/**
	 * Read the given file and return the content as a string.
	 * 
	 * @param file
	 * @return
	 * @throws java.io.IOException
	 */
	private String readFileAsString(File file) throws java.io.IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} finally {
			closeReader(reader);
		}
		return fileData.toString();
	}

	/**
	 * Write the given string to a file.
	 * 
	 * @param str
	 * @param file
	 * @throws IOException
	 */
	private void writeStringToFile(String str, File file) throws IOException {
		if (!file.exists() && file.isDirectory()) {
			return;
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(str);
		} finally {
			closeWriter(bw);
		}
	}

	/**
	 * Quietly close a reader.
	 * 
	 * @param reader
	 */
	private void closeReader(Reader reader) {
		if (reader == null) {
			return;
		}

		try {
			reader.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Quietly close a writer.
	 * 
	 * @param writer
	 */
	private void closeWriter(Writer writer) {
		if (writer == null) {
			return;
		}

		try {
			writer.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Recursively probe for all files to be processed.
	 * 
	 * @param files
	 * @param result
	 */
	private void probeFiles(File[] files, List result) {
		for (int i = 0, n = files.length; i < n; i++) {
			File file = files[i];
			if (!file.exists()) {
				continue;
			}

			if (file.isDirectory()) {
				probeFiles(file.listFiles(), result);
			}
			if (file.isFile()) {
				if (file.getName().endsWith(".java")) {
					result.add(file);
				}
			}
		}
	}

	/**
	 * Create a {@link CodeFormatter} instance to be used by this mojo.
	 * 
	 * @throws MojoExecutionException
	 */
	private void createCodeFormatter() throws MojoExecutionException {
		Map options = getFormattingOptions();
		formatter = ToolFactory.createCodeFormatter(options);
	}

	/**
	 * Return the options to be passed when creating {@link CodeFormatter}
	 * instance.
	 * 
	 * @return
	 * @throws MojoExecutionException
	 */
	private Map getFormattingOptions() throws MojoExecutionException {
		Map options = new HashMap();
		options.put(JavaCore.COMPILER_SOURCE, compilerSource);
		options.put(JavaCore.COMPILER_COMPLIANCE, compilerCompliance);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				compilerTargetPlatform);

		if (configFile != null) {
			Map config = getOptionsFromConfigFile();
			if (Boolean.TRUE.equals(overrideConfigCompilerVersion)) {
				config.remove(JavaCore.COMPILER_SOURCE);
				config.remove(JavaCore.COMPILER_COMPLIANCE);
				config.remove(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM);
			}
			options.putAll(config);
		}

		return options;
	}

	/**
	 * Read config file and return the config as {@link Map}.
	 * 
	 * @return
	 * @throws MojoExecutionException
	 */
	private Map getOptionsFromConfigFile() throws MojoExecutionException {

		InputStream configInput = null;
		try {
			resourceManager.addSearchPath(FileResourceLoader.ID, basedir
					.getAbsolutePath());
			configInput = resourceManager.getResourceAsInputStream(configFile);
		} catch (ResourceNotFoundException e) {
			throw new MojoExecutionException("Config file [" + configFile
					+ "] cannot be found", e);
		}

		if (configInput == null) {
			throw new MojoExecutionException("Config file [" + configFile
					+ "] does not exist");
		} else {
			try {
				ConfigReader configReader = new ConfigReader();
				return configReader.read(configInput);
			} catch (IOException e) {
				throw new MojoExecutionException("Cannot read config file ["
						+ configFile + "]", e);
			} catch (SAXException e) {
				throw new MojoExecutionException("Cannot parse config file ["
						+ configFile + "]", e);
			} catch (ConfigReadException e) {
				throw new MojoExecutionException(e.getMessage(), e);
			} finally {
				if (configInput != null) {
					try {
						configInput.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * Returns the lineEnding parameter as characters when the value is known
	 * (LF, CRLF, CR) or can be determined from the file text (KEEP). Otherwise
	 * null is returned.
	 * 
	 * @return
	 */
	String getLineEnding(String fileDataString) {
		String lineEnd = null;
		if (LINE_ENDING_KEEP.equals(lineEnding)) {
			lineEnd = determineLineEnding(fileDataString);
		} else if (LINE_ENDING_LF.equals(lineEnding)) {
			lineEnd = LINE_ENDING_LF_CHAR;
		} else if (LINE_ENDING_CRLF.equals(lineEnding)) {
			lineEnd = LINE_ENDING_CRLF_CHARS;
		} else if (LINE_ENDING_CR.equals(lineEnding)) {
			lineEnd = LINE_ENDING_CR_CHAR;
		}
		return lineEnd;
	}

	/**
	 * Returns the most occurring line-ending characters in the file text or
	 * null if no line-ending occurs the most.
	 * 
	 * @return
	 */
	String determineLineEnding(String fileDataString) {
		int lfCount = 0;
		int crCount = 0;
		int crlfCount = 0;

		for (int i = 0; i < fileDataString.length(); i++) {
			char c = fileDataString.charAt(i);
			if (c == '\r') {
				if ((i + 1) < fileDataString.length()
						&& fileDataString.charAt(i + 1) == '\n') {
					crlfCount++;
					i++;
				} else {
					crCount++;
				}
			} else if (c == '\n') {
				lfCount++;
			}
		}
		if (lfCount > crCount && lfCount > crlfCount) {
			return LINE_ENDING_LF_CHAR;
		} else if (crlfCount > lfCount && crlfCount > crCount) {
			return LINE_ENDING_CRLF_CHARS;
		} else if (crCount > lfCount && crCount > crlfCount) {
			return LINE_ENDING_CR_CHAR;
		}
		return null;
	}

	private class ResultCollector {
		private int successCount;
		private int failCount;
		private int skippedCount;
	}
}
