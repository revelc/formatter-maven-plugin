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
package com.marvinformatics.formatter.groovy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.eclipse.refactoring.formatter.DefaultGroovyFormatter;
import org.codehaus.groovy.eclipse.refactoring.formatter.FormatterPreferencesOnStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.util.ISafeRunnableRunner;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.xml.sax.SAXException;

import com.marvinformatics.formatter.Formatter;

public class GroovyFormatter implements Formatter {

	private static final List<Class<?>> RUNTIME_DEPENDENCIES = Arrays.asList(ISafeRunnableRunner.class,
			SAXException.class);

	private final DefaultGroovyFormatter formatter;

	public GroovyFormatter(Map<String, String> options) {
		RUNTIME_DEPENDENCIES.forEach(Class::getName);

		IDocument doc = new Document();
		PreferenceStore preferences = createPreferences(options);
		FormatterPreferencesOnStore preferencesStore = new FormatterPreferencesOnStore(preferences);
		formatter = new DefaultGroovyFormatter(TextSelection.emptySelection(), doc, preferencesStore, false);
	}

	public String format(String code) {
		TextEdit te = formatter.format();
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

	private static PreferenceStore createPreferences(final Map<String, String> options) {
		final PreferenceStore preferences = new PreferenceStore();
		options.forEach(preferences::putValue);
		return preferences;
	}

}
