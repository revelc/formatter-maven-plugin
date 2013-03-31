package com.marvinformatics.formatter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

/**
 * @author marvin.froeder
 */
public abstract class AbstractCacheableFormatter {

	protected Log log;

	protected Charset encoding;

	public AbstractCacheableFormatter() {
		super();
	}

	protected abstract void init(Map<String, String> options,
			ConfigurationSource cfg);

	protected void initCfg(ConfigurationSource cfg) {
		this.log = cfg.getLog();
		this.encoding = cfg.getEncoding();
	}

	public Result formatFile(File file, LineEnding ending, boolean dryRun) {
		try {
			log.debug("Processing file: " + file);
			String code = FileUtils.fileRead(file, encoding.name());
			String formattedCode = doFormat(code, ending);

			if (formattedCode == null) {
				formattedCode = fixLineEnding(code, ending);
			}

			if (formattedCode == null) {
				log.debug("Equal code. Not writing result to file.");
				return Result.SKIPPED;
			}

			if (!dryRun) {
				FileUtils.fileWrite(file, encoding.name(), formattedCode);
			}

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

	private String fixLineEnding(String code, LineEnding ending) {
		if (ending == LineEnding.KEEP)
			return null;

		LineEnding current = LineEnding.determineLineEnding(code);
		if (current == ending)
			return null;
		return code.replace(current.getChars(), ending.getChars());
	}

	protected abstract String doFormat(String code, LineEnding ending)
			throws IOException, BadLocationException;

}