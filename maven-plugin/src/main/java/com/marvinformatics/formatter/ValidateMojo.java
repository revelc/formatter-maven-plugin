package com.marvinformatics.formatter;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jface.text.BadLocationException;

/**
 * This mojo is very similar to Formatter mojo, but it is focused on CI servers.
 * 
 * If the code ain't formatted as expected this mojo will fail the build
 * 
 * @author marvin.froeder
 */
@Mojo(name = "validate", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = false)
public class ValidateMojo extends FormatterMojo {

	@Override
	protected void doFormatFile(File file, ResultCollector rc,
			Properties hashCache, String basedirPath, boolean dryRun)
			throws IOException, BadLocationException, MojoFailureException,
			MojoExecutionException {
		super.doFormatFile(file, rc, hashCache, basedirPath, true);

		if (rc.successCount != 0)
			throw new MojoFailureException("File '" + file
					+ "' format doesn't match!");
		if (rc.failCount != 0)
			throw new MojoExecutionException("Error formating '" + file + "' ");
	}

}
