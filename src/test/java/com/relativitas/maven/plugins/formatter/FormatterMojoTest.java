package com.relativitas.maven.plugins.formatter;

import junit.framework.TestCase;

/**
 * Test class for {@link FormatterMojo}.
 * 
 * @author matt.blanchette
 */
public class FormatterMojoTest extends TestCase {

	/**
	 * Test successfully determining CRLF line ending.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_line_endings_crlf() throws Exception {
		String fileData = "Test\r\nTest\r\nTest\r\n";
		FormatterMojo mojo = new FormatterMojo();
		String lineEnd = mojo.determineLineEnding(fileData);
		assertEquals(FormatterMojo.LINE_ENDING_CRLF_CHARS, lineEnd);
	}

	/**
	 * Test successfully determining LF line ending.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_line_endings_lf() throws Exception {
		String fileData = "Test\nTest\nTest\n";
		FormatterMojo mojo = new FormatterMojo();
		String lineEnd = mojo.determineLineEnding(fileData);
		assertEquals(FormatterMojo.LINE_ENDING_LF_CHAR, lineEnd);
	}

	/**
	 * Test successfully determining CR line ending.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_line_endings_cr() throws Exception {
		String fileData = "Test\rTest\rTest\r";
		FormatterMojo mojo = new FormatterMojo();
		String lineEnd = mojo.determineLineEnding(fileData);
		assertEquals(FormatterMojo.LINE_ENDING_CR_CHAR, lineEnd);
	}

	/**
	 * Test successfully determining LF line ending with mixed endings.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_line_endings_mixed_lf() throws Exception {
		String fileData = "Test\r\nTest\rTest\nTest\nTest\r\nTest\n";
		FormatterMojo mojo = new FormatterMojo();
		String lineEnd = mojo.determineLineEnding(fileData);
		assertEquals(FormatterMojo.LINE_ENDING_LF_CHAR, lineEnd);
	}

	/**
	 * Test successfully determining AUTO line ending with mixed endings and no
	 * clear majority.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_line_endings_mixed_auto() throws Exception {
		String fileData = "Test\r\nTest\r\nTest\nTest\nTest\r\nTest\nTest\r";
		FormatterMojo mojo = new FormatterMojo();
		String lineEnd = mojo.determineLineEnding(fileData);
		assertNull("Not AUTO line ending", lineEnd);
	}

	/**
	 * Test successfully determining AUTO line ending with no endings.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_line_endings_none_auto() throws Exception {
		String fileData = "TestTestTestTest";
		FormatterMojo mojo = new FormatterMojo();
		String lineEnd = mojo.determineLineEnding(fileData);
		assertNull("Not AUTO line ending", lineEnd);
	}

}
