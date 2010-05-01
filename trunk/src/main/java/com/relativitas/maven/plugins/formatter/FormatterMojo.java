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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * Java source code formatter plugin.
 * 
 * @goal format
 * @phase compile
 * 
 * @author jecki
 */
public class FormatterMojo extends AbstractMojo {
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

	private CodeFormatter formatter;

	/**
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		if (directories == null) {
			directories = new File[] { sourceDirectory, testSourceDirectory };
		}

		List files = new ArrayList();
		probeFiles(directories, files);

		for (int i = 0, n = files.size(); i < n; i++) {
			File file = (File) files.get(i);
			formatFile(file);
		}
	}

	/**
	 * @param file
	 */
	private void formatFile(File file) {
		CodeFormatter formatter = getCodeFormatter();
		try {
			doFormatFile(file, formatter);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Format individual file.
	 * 
	 * @param file
	 * @param formatter
	 * @throws IOException
	 * @throws BadLocationException
	 */
	private void doFormatFile(File file, CodeFormatter formatter)
			throws IOException, BadLocationException {
		String code = readFileAsString(file);

		TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT, code,
				0, code.length(), 0, null);
		if (te == null) {
			// TODO code cannot be formatted
			return;
		}

		IDocument doc = new Document(code);
		te.apply(doc);
		writeStringToFile(doc.get(), file);
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
	 * Get {@link CodeFormatter} instance to be used by this mojo, or create one
	 * if it does not exist yet.
	 * 
	 * @return
	 */
	private CodeFormatter getCodeFormatter() {
		if (formatter == null) {
			Map options = getFormattingOptions();
			formatter = ToolFactory.createCodeFormatter(options);
		}
		return formatter;
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
		
		return options;
	}
}
