package com.relativitas.maven.plugins.formatter.java;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import com.relativitas.maven.plugins.formatter.ConfigurationSource;
import com.relativitas.maven.plugins.formatter.LineEnding;
import com.relativitas.maven.plugins.formatter.Result;

/**
 * @author marvin.froeder
 */
public class JavaFormatter {

	private CodeFormatter formatter;

	private Log log;

	private Charset encoding;

	/**
	 * Initialize the {@link CodeFormatter} instance to be used by this
	 * component.
	 * 
	 * @param log
	 * @param targetDirectory
	 * @throws MojoExecutionException
	 */
	public void init(Map<String, String> options, ConfigurationSource cfg) {
		this.log = cfg.getLog();
		this.encoding = cfg.getEncoding();

		if (options.isEmpty()) {
			options.put(JavaCore.COMPILER_SOURCE, cfg.getCompilerSources());
			options.put(JavaCore.COMPILER_COMPLIANCE,
					cfg.getCompilerCompliance());
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
					cfg.getCompilerCodegenTargetPlatform());
		}

		formatter = ToolFactory.createCodeFormatter(options);
	}

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
	public Result doFormatFile(File file, LineEnding ending) {
		try {
			log.debug("Processing file: " + file);
			String code = FileUtils.fileRead(file, encoding.name());

			TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT,
					code, 0, code.length(), 0, ending.getChars());
			if (te == null) {
				log.debug("Code cannot be formatted. Possible cause is unmatched source/target/compliance version.");
				return Result.SKIPPED;
			}

			IDocument doc = new Document(code);
			te.apply(doc);
			String formattedCode = doc.get();

			if (code.equals(formattedCode)) {
				log.debug("Equal hash code. Not writing result to file.");
				return Result.SKIPPED;
			}

			FileUtils.fileWrite(file, encoding.name(), formattedCode);

			return Result.SUCCESS;
		} catch (IOException e) {
			log.warn(e);
			return Result.FAIL;
		} catch (MalformedTreeException e) {
			log.warn(e);
			return Result.FAIL;
		} catch (BadLocationException e) {
			log.warn(e);
			return Result.FAIL;
		}
	}
}
