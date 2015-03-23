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
	@Test(expected=NullPointerException.class)
	public final void testCreateNullTemplate() {
		Template.as(null);
	}
}
