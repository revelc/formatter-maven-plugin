package com.marvinformatics.formatter.java;

import com.marvinformatics.formatter.AbstractFormatterTest;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends AbstractFormatterTest {

	public void testDoFormatFile() throws Exception {
		doTestFormat(new JavaFormatter(), "AnyClass.java",
				"c120a11572348caf71e2c050c6219880b0d59bf2");
	}

}
