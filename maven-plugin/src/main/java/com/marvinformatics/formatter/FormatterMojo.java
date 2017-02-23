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

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.*;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.xml.sax.SAXException;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

/**
 * A Maven plugin mojo to format Java source code using the Eclipse code
 * formatter.
 * 
 * Mojo parameters allow customizing formatting by specifying the config XML
 * file, line endings, compiler version, and source code locations. Reformatting
 * source files is avoided using an md5 hash of the content, comparing to the
 * original hash to the hash after formatting and a cached hash.
 * 
 * @author jecki
 * @author Matt Blanchette
 * @author marvin.froeder
 */
@Mojo(name = "format", defaultPhase = LifecyclePhase.PROCESS_SOURCES, requiresProject = false)
public class FormatterMojo extends AbstractMojo implements ConfigurationSource {

	/**
	 * Project's source directory as specified in the POM.
	 */
	@Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
	private File sourceDirectory;

	/**
	 * Project's test source directory as specified in the POM.
	 */
	@Parameter(defaultValue = "${project.build.testSourceDirectory}", readonly = true, required = true)
	private File testSourceDirectory;

	/**
	 * Project's target directory as specified in the POM.
	 */
	@Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
	private File targetDirectory;

	/**
	 * Project's base directory.
	 */
	@Parameter(defaultValue = ".", property = "project.basedir", readonly = true, required = true)
	private File basedir;

	/**
	 * Location of the Java source files to format. Defaults to source main and
	 * test directories if not set. Deprecated in version 0.3. Reintroduced in
	 * 0.4.
	 * 
	 * @since 0.4
	 */
	@Parameter
	private File[] directories;

	/**
	 * List of fileset patterns for Java source locations to include in
	 * formatting. Patterns are relative to the project source and test source
	 * directories. When not specified, the default include is
	 * <code>**&#47;*.java</code>
	 * 
	 * @since 0.3
	 */
	@Parameter(property = "formatter.includes")
	private String[] includes;

	/**
	 * List of fileset patterns for Java source locations to exclude from
	 * formatting. Patterns are relative to the project source and test source
	 * directories. When not specified, there is no default exclude.
	 * 
	 * @since 0.3
	 */
	@Parameter
	private String[] excludes;

	/**
	 * Java compiler source version.
	 */
	@Parameter(defaultValue = "1.5", property = "maven.compiler.source", required = true)
	private String compilerSource;

	/**
	 * Java compiler compliance version.
	 */
	@Parameter(defaultValue = "1.5", property = "maven.compiler.source", required = true)
	private String compilerCompliance;

	/**
	 * Java compiler target version.
	 */
	@Parameter(defaultValue = "1.5", property = "maven.compiler.target", required = true)
	private String compilerTargetPlatform;

	/**
	 * The file encoding used to read and write source files. When not specified
	 * and sourceEncoding also not set, default is platform file encoding.
	 * 
	 * @since 0.3
	 */
	@Parameter(property = "project.build.sourceEncoding", required = true)
	private String encoding;

	/**
	 * Sets the line-ending of files after formatting. Valid values are:
	 * <ul>
	 * <li><b>"AUTO"</b> - Use line endings of current system</li>
	 * <li><b>"KEEP"</b> - Preserve line endings of files, default to AUTO if
	 * ambiguous</li>
	 * <li><b>"LF"</b> - Use Unix and Mac style line endings</li>
	 * <li><b>"CRLF"</b> - Use DOS and Windows style line endings</li>
	 * <li><b>"CR"</b> - Use early Mac style line endings</li>
	 * </ul>
	 * 
	 * @since 0.2.0
	 */
	@Parameter(defaultValue = "AUTO", property = "lineending", required = true)
	private LineEnding lineEnding;

	/**
	 * File or classpath location of an Eclipse code formatter configuration xml
	 * file to use in formatting.
	 */
	@Parameter(defaultValue = "src/config/eclipse/formatter/java.xml", property = "configfile", required = true)
	private String configFile;

	/**
	 * File or classpath location of an Eclipse code formatter configuration xml
	 * file to use in formatting.
	 */
	@Parameter(defaultValue = "src/config/eclipse/formatter/javascript.xml", property = "configjsfile", required = true)
	private String configJsFile;

	/**
	 * Whether the formatting is skipped.
	 *
	 * @since 0.5
	 */
	@Parameter(defaultValue = "false", alias = "skip", property = "formatter.skip")
	private Boolean skipFormatting;

	@Parameter(defaultValue = "false", property = "aggregator", required = true)
	protected boolean aggregator;

	@Parameter(defaultValue = "${project.executionRoot}", required = true)
	protected boolean executionRoot;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (aggregator && !executionRoot)
			return;

		if (skip()) {
			getLog().info("Formatting is skipped");
			return;
		}

		ResultCollector rc = new FormatterExecuter(this).execute();
		getLog().info("Successfully formatted: " + rc.successCount() + " file(s)");
		getLog().info("Fail to format        : " + rc.failCount() + " file(s)");
		getLog().info("Skipped               : " + rc.skippedCount() + " file(s)");
		getLog().info("Approximate time taken: " + rc.getWatch().elapsed(TimeUnit.SECONDS) + "s");
	}

	@Override
	public String getCompilerSources() {
		return compilerSource;
	}

	@Override
	public String getCompilerCompliance() {
		return compilerCompliance;
	}

	@Override
	public String getCompilerCodegenTargetPlatform() {
		return compilerTargetPlatform;
	}

	@Override
	public File getTargetDirectory() {
		return targetDirectory;
	}

	@Override
	public Charset getEncoding() {
		return Charset.forName(encoding);
	}

	@Override
	public LineEnding lineEnding() {
		return lineEnding;
	}

	@Override
	public boolean isDryRun() {
		return false;
	}

	public boolean skip() {
		return skipFormatting;
	}

	public String encoding() {
		return this.encoding;
	}

	public String javaConfig() {
		return configFile;
	}

	public String jsConfig() {
		return configJsFile;
	}

	public String[] excludes() {
		return this.excludes;
	}

	public List<File> directories() {
		if (directories == null)
			return Lists.newArrayList(sourceDirectory, testSourceDirectory);

		return Lists.newArrayList(this.directories);
	}

}
