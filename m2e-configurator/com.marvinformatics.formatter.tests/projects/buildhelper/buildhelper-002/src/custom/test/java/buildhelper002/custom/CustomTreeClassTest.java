package buildhelper002.custom;

import java.io.InputStream;

import junit.framework.TestCase;
import buildhelper002.main.MainTreeClass;

public class CustomTreeClassTest extends TestCase {

	public void testRead() throws Exception {
		InputStream in = getClass().getResourceAsStream("customTreeTest.txt");
    	byte[] bytes =new byte[6];
    	in.read(bytes);
    	String expected = new String(bytes);
		String actual = new MainTreeClass().read();
		assertEquals(expected, actual);
	}
}
