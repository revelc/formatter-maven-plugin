package com.relativitas.maven.plugins.formatter;

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

/**
 * Test class for {@link ConfigReader}.
 * 
 * @author jecki
 */
public class ConfigReaderTest extends TestCase {

	/**
	 * Test successfully read a config file.
	 * 
	 * @throws Exception
	 */
	public void test_success_read_config() throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream in = cl.getResourceAsStream("sample-config.xml");
		ConfigReader configReader = new ConfigReader();
		Map config = configReader.read(in);
		assertNotNull(config);
		assertEquals(264, config.keySet().size());
		// test get one of the entry in the file
		assertEquals("true", config
				.get("org.eclipse.jdt.core.formatter.comment.format_html"));
	}

	/**
	 * Test reading an invalid config file.
	 * 
	 * @throws Exception
	 */
	public void test_read_invalid_config() throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream in = cl.getResourceAsStream("sample-invalid-config.xml");
		ConfigReader configReader = new ConfigReader();
		try {
			configReader.read(in);
			fail("Expected SAXException to be thrown");
		} catch (SAXException e) {
		}
	}

	/**
	 * Test reading an invalid config file.
	 * 
	 * @throws Exception
	 */
	public void test_read_invalid_config2() throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream in = cl.getResourceAsStream("sample-invalid-config2.xml");
		ConfigReader configReader = new ConfigReader();
		try {
			configReader.read(in);
			fail("Expected ConfigReadException to be thrown");
		} catch (ConfigReadException e) {
		}
	}

}
