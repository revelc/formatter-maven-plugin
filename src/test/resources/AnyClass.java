package com.relativitas.maven.plugins.formatter;  import java.io.IOException; import java.io.InputStream; import java.util.List; import java.util.Map; import org.apache.commons.digester.Digester; import org.xml.sax.SAXException; import com.relativitas.maven.plugins.formatter.xml.Profiles; 



public class ConfigReader {

    
    
    
    
public Map<String, String> read( InputStream input ) throws IOException, SAXException, ConfigReadException { Digester digester = new Digester(); digester.addRuleSet( new RuleSet() ); Object result = digester.parse( input ); if ( result == null && !( result instanceof Profiles ) ) { throw new ConfigReadException( "No profiles found in config file" ); } Profiles profiles = (Profiles) result; List<Map<String, String>> list = profiles.getProfiles(); if ( list.size() == 0 ) { throw new ConfigReadException( "No profile in config file of kind: " + Profiles.PROFILE_KIND ); } return (Map<String, String>) list.get( 0 ); }




}

