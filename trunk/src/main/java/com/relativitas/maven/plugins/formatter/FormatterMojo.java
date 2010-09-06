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
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import com.twmacinta.util.MD5;

/**
 * Java source code formatter plugin.
 * 
 * @goal format
 * @phase process-sources
 * 
 * @author jecki
 */
public class FormatterMojo extends AbstractMojo {
	private static final String CACHE_PROPERTIES_FILENAME = "maven-java-formatter-cache.properties";

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
	 * Location of the file.
	 * 
	 * @parameter
	 */
	private File[] directories;

	/**
	 * @parameter default-value="1.5"
	 */
	private String compilerSource;

	/**
	 * @parameter default-value="1.5"
	 */
	private String compilerCompliance;

	/**
	 * @parameter default-value="1.5"
	 */
	private String compilerTargetPlatform;

	/**
	 * @parameter
	 */
	private File configFile;

	private CodeFormatter formatter;

	/**
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		long startClock = System.currentTimeMillis();

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

		TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT, code,
				0, code.length(), 0, null);
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
		MD5 md5 = new MD5();
		md5.Update(str, null);
		return md5.asHex();
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
	 */
	private void createCodeFormatter() {
		Map options = getFormattingOptions();
		formatter = ToolFactory.createCodeFormatter(options);
	}

	/**
	 * Return the options to be passed when creating {@link CodeFormatter}
	 * instance.
	 * 
	 * @return
	 */
	private Map getFormattingOptions() {
		Map options = new HashMap();
		options.put(JavaCore.COMPILER_SOURCE, compilerSource);
		options.put(JavaCore.COMPILER_COMPLIANCE, compilerCompliance);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				compilerTargetPlatform);

		if (configFile != null) {
			Map config = getOptionsFromConfigFile();
			options.putAll(config);
		}

		return options;
	}

	/**
	 * Read config file and return the config as {@link Map}.
	 * 
	 * @return
	 */
	private Map getOptionsFromConfigFile() {
		Log log = getLog();
		if (!configFile.exists()) {
			log.error("Config file [" + configFile + "] does not exist");
		} else if (!configFile.isFile()) {
			log.error("Config file [" + configFile + "] is not a file");
		} else {
			FileReader reader;
			try {
				reader = new FileReader(configFile);
				ConfigReader configReader = new ConfigReader();
				return configReader.read(reader);
			} catch (FileNotFoundException e) {
				log.error("Cannot load config file [" + configFile + "]", e);
			}
		}
		return Collections.emptyMap();
	}

	private class ResultCollector {
		private int successCount;
		private int failCount;
		private int skippedCount;
	}
}
