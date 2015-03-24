package com.marvinformatics.formatter.javascript;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.jsdt.core.ToolFactory;
import org.eclipse.wst.jsdt.core.formatter.CodeFormatter;

import com.marvinformatics.formatter.AbstractCacheableFormatter;
import com.marvinformatics.formatter.ConfigurationSource;
import com.marvinformatics.formatter.Formatter;
import com.marvinformatics.formatter.LineEnding;

public class JavascriptFormatter extends AbstractCacheableFormatter
		implements
			Formatter {

	private CodeFormatter formatter;

	@Override
	public void init(Map<String, String> options, ConfigurationSource cfg) {
		super.initCfg(cfg);

		this.formatter = ToolFactory.createCodeFormatter(options);
	}

	@Override
	public String doFormat(String code, LineEnding ending) throws IOException,
			BadLocationException {
		TextEdit te = formatter.format(CodeFormatter.K_JAVASCRIPT_UNIT, code,
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
