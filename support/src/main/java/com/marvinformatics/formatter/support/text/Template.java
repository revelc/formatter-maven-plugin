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
package com.marvinformatics.formatter.support.text;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

/**
 * Facade around the {@link java.lang.String#format(String, Object...)} method.
 */
public final class Template {

	/**
	 * Template context text.
	 */
	private String content;

	private Template(String content) {
		this.content = requireNonNull(content, "content shall not be null");
	}

	/**
	 * Formats the applied args into the template content.
	 * @param args Arguments to use as source data
	 * @return Formatted string
	 */
	public String format(String... args) {
		return String.format(content, (Object[]) args);
	}

	/**
	 * Formats the applied args into the template content.
	 * 
	 * The supplied locale is used to format the text.
	 * @param locale Locale to use for formatting
	 * @param args Arguments to use as source data
	 * @return Formatted string
	 */
	public String formatWithLocale(Locale locale, String... args) {
		return String.format(locale, content, (Object[]) args);
	}

	/**
	 * Provides a template string as a {@link Template} instance.
	 * @param content template content to wrap
	 * @return Template instance
	 */
	public static Template as(String content) {
		return new Template(content);
	}
}
