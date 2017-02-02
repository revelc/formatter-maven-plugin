/**
 * Copyright (C) 2010 Marvin Herman Froeder (marvin@marvinformatics.com)
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

public class JavascriptFormatter extends AbstractCacheableFormatter implements Formatter {

	private CodeFormatter formatter;

	public JavascriptFormatter(Map<String, String> options, ConfigurationSource cfg) {
		super(cfg);

		this.formatter = ToolFactory.createCodeFormatter(options);
	}

	@Override
	public String doFormat(String code) throws IOException, BadLocationException {
		TextEdit te = formatter.format(CodeFormatter.K_JAVASCRIPT_UNIT, code, 0, code.length(), 0,
				configurationSource.lineEnding().getChars());
		if (te == null)
			throw new IllegalArgumentException(
					"Code cannot be formatted. Possible cause " + "is unmatched source/target/compliance version.");

		IDocument doc = new Document(code);
		te.apply(doc);
		return doc.get();
	}

}
