package com.marvinformatics.formatter.javascript;

import com.marvinformatics.formatter.AbstractFormatterTest;

public class JavascriptFormatterTest extends AbstractFormatterTest {

	public void testDoFormatFile() throws Exception {
		doTestFormat(new JavascriptFormatter(), "AnyJS.js",
				"d2414ec114d13731a9e3e93643e39598e01e16c5");
	}

}
