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

import java.util.Map;

import com.marvinformatics.formatter.AbstractFormatterTest;
import com.marvinformatics.formatter.Formatter;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends AbstractFormatterTest {

	@Override
	public Formatter createFormatter() {
		return new JavaFormatter();
	}

	@Override
	public void tuneDefaultConfigs(Map<String, String> options) {
		options.put("org.eclipse.jdt.core.formatter.tabulation.char", "tab");
		options.put("org.eclipse.jdt.core.formatter.blank_lines_before_imports", "1");
		options.put("org.eclipse.jdt.core.formatter.blank_lines_after_imports", "1");
		options.put("org.eclipse.jdt.core.formatter.blank_lines_before_method", "1");
	}

	@Override
	public String fileUnderTest() {
		return "AnyClass.java";
	}

}
