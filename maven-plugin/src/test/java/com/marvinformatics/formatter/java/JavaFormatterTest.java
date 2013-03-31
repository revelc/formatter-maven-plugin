package com.marvinformatics.formatter.java;

import com.marvinformatics.formatter.AbstractFormatterTest;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends AbstractFormatterTest {

	public void testDoFormatFile() throws Exception {
		doTestFormat(new JavaFormatter(), "AnyClass.java",
				"0cc3cb63da5943c9b2ceede6231ac3b93a83c363");
	}

}
