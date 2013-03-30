package com.relativitas.maven.plugins.formatter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;

/**
 * @author marvin.froeder
 */
public interface Formatter {

	/**
	 * Initialize the {@link CodeFormatter} instance to be used by this component.
	 * 
	 * @param log
	 * @param targetDirectory
	 * @throws MojoExecutionException
	 */
	public abstract void init(Map<String, String> options,
			ConfigurationSource cfg);

	/**
	 * Format individual file.
	 * 
	 * @param file
	 * @param rc
	 * @param hashCache
	 * @param basedirPath
	 * @return
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public abstract Result formatFile(File file, LineEnding ending);

}