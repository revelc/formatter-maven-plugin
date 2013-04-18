package buildhelper002.main;

import java.io.IOException;

import buildhelper002.custom.CustomTreeClass;

public class MainTreeClass
{
    CustomTreeClass custom = new CustomTreeClass();
    
    public String read() throws IOException {
    	return custom.read();
    }
}
