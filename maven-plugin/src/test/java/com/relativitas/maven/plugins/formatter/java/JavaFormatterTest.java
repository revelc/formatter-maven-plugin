package com.relativitas.maven.plugins.formatter.java;

import com.relativitas.maven.plugins.formatter.AbstractFormatterTest;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends AbstractFormatterTest {

	public void testDoFormatFile() throws Exception {
		doTestFormat(new JavaFormatter(), "AnyClass.java",
				"d9c0aec7b7f350f19089258ba86aeaf92f39669b");
	}

}
