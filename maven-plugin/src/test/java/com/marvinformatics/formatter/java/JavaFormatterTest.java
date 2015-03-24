package com.marvinformatics.formatter.java;

import com.marvinformatics.formatter.AbstractFormatterTest;

/**
 * @author marvin.froeder
 */
public class JavaFormatterTest extends AbstractFormatterTest {

	public void testDoFormatFile() throws Exception {
		doTestFormat(new JavaFormatter(), "AnyClass.java",
				"389cb11465d22459423a9ee1bf3628bc1124f533");
	}

}
