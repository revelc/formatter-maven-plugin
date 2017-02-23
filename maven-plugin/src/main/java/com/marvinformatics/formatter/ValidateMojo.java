/**
 * Copyright (C) 2010 Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marvinformatics.formatter;

import java.util.concurrent.TimeUnit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

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
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (aggregator && !executionRoot)
			return;

		ResultCollector rc = new FormatterExecuter(this).execute();
		getLog().info("Successfully formatted: " + rc.successCount() + " file(s)");
		getLog().info("Fail to format        : " + rc.failCount() + " file(s)");
		getLog().info("Skipped               : " + rc.skippedCount() + " file(s)");
		getLog().info("Approximate time taken: " + rc.getWatch().elapsed(TimeUnit.SECONDS) + "s");

		if (rc.successCount() != 0)
			throw new MojoFailureException("Format doesn't match!");
		if (rc.failCount() != 0)
			throw new MojoExecutionException("Error formating files ");
	}

	@Override
	public boolean isDryRun() {
		return true;
	}

}
