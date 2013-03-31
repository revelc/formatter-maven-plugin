package com.marvinformatics.formatter.java;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import com.marvinformatics.formatter.AbstractCacheableFormatter;
import com.marvinformatics.formatter.ConfigurationSource;
import com.marvinformatics.formatter.Formatter;
import com.marvinformatics.formatter.LineEnding;

public class JavaFormatter extends AbstractCacheableFormatter implements
		Formatter {

	CodeFormatter formatter;

	@Override
	public void init(Map<String, String> options, ConfigurationSource cfg) {
		if (options.isEmpty()) {
			options.put(JavaCore.COMPILER_SOURCE, cfg.getCompilerSources());
			options.put(JavaCore.COMPILER_COMPLIANCE,
					cfg.getCompilerCompliance());
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
					cfg.getCompilerCodegenTargetPlatform());
		}

		super.initCfg(cfg);

		formatter = ToolFactory.createCodeFormatter(options);
	}

	@Override
	public String doFormat(String code, LineEnding ending) throws IOException,
			BadLocationException {
		TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT, code,
				0, code.length(), 0, ending.getChars());
		if (te == null) {
			log.debug("Code cannot be formatted. Possible cause "
					+ "is unmatched source/target/compliance version.");
			return null;
		}

		IDocument doc = new Document(code);
		te.apply(doc);
		String formattedCode = doc.get();

		if (code.equals(formattedCode)) {
			return null;
		}
		return formattedCode;
	}

}
