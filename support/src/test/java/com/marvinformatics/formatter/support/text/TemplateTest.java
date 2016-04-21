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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Locale;

import org.junit.Test;

/**
 * Test of the {@link Template} class.
 */
public class TemplateTest {

	/**
	 * Test method for {@link Template#format(java.lang.String[])}.
	 */
	@Test
	public final void testFormatStringArray() {
		Template specimen = Template.as("one: %s, two: %s");
		String result = specimen.format("alpha", "beta");

		assertEquals("one: alpha, two: beta", result);
	}

	/**
	 * Test method for {@link Template#format(Locale, java.lang.String[])}.
	 */
	@Test
	public final void testFormatWithLocaleStringArray() {
		Template specimen = Template.as("one: %s, two: %s");
		String result = specimen.formatWithLocale(Locale.US, "alpha", "beta");

		assertEquals("one: alpha, two: beta", result);
	}

	/**
	 * Test method for {@link Template#as(java.lang.String)}.
	 * <p />
	 * This test case assumes a valid template string.
	 */
	@Test
	public final void testCreateValidTemplate() {
		Template result = Template.as("one: %s, two: %s");

		assertNotNull(result);
	}

	/**
	 * Test method for {@link Template#as(java.lang.String)}.
	 * <p />
	 * This test case assumes a null template string.
	 */
	@Test(expected = NullPointerException.class)
	public final void testCreateNullTemplate() {
		Template.as(null);
	}
}
