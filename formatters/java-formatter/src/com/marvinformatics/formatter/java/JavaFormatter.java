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
package com.marvinformatics.formatter.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import com.marvinformatics.formatter.Formatter;
import com.marvinformatics.formatter.LineEnding;

public class JavaFormatter implements Formatter {

	private final CodeFormatter formatter;
	private final LineEnding lineEnding;

	public JavaFormatter(
			Map<String, String> options,
			String compilerSources,
			String compilerCompliance,
			String compilerCodegenTargetPlatform,
			LineEnding lineEnding) {

		options.put(JavaCore.COMPILER_SOURCE, compilerSources);
		options.put(JavaCore.COMPILER_COMPLIANCE, compilerCompliance);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, compilerCodegenTargetPlatform);

		formatter = ToolFactory.createCodeFormatter(new HashMap<>(options));
		this.lineEnding = lineEnding;
	}

	public String format(String code) {
		TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0,
				lineEnding.getChars());
		if (te == null)
			throw new IllegalArgumentException(
					"Code cannot be formatted. Possible cause " + "is unmatched source/target/compliance version.");

		IDocument doc = new Document(code);
		try {
			te.apply(doc);
		} catch (MalformedTreeException | BadLocationException e) {
			throw new IllegalStateException("Code cannot be formatted. original code:\n" + code);
		}
		return doc.get();
	}

}
