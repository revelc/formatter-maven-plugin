package com.relativitas.maven.plugins.formatter;

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

	public Result formatFile(File file, LineEnding ending) {
		try {
			log.debug("Processing file: " + file);
			String code = FileUtils.fileRead(file, encoding.name());
			String formattedCode = doFormat(code, ending);
			if (formattedCode == null) {
				log.debug("Equal code. Not writing result to file.");
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

	protected abstract String doFormat(String code, LineEnding ending)
			throws IOException, BadLocationException;

}