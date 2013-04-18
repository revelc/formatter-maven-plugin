package buildhelper002.custom;

import java.io.IOException;
import java.io.InputStream;

public class CustomTreeClass 
{
	public String read() throws IOException {
    	InputStream in = getClass().getResourceAsStream("customTree.txt");
    	byte[] bytes =new byte[6];
    	in.read(bytes);
    	return new String(bytes);
	}
}
