package com.marvinformatics.formatter;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.maven.plugin.logging.Log;

/**
 * @author marvin.froeder
 */
public interface ConfigurationSource {

	Log getLog();

	String getCompilerSources();

	String getCompilerCompliance();

	String getCompilerCodegenTargetPlatform();

	File getTargetDirectory();

	Charset getEncoding();

}
