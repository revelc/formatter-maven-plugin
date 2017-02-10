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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.*;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.xml.sax.SAXException;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.marvinformatics.formatter.java.JavaFormatter;
import com.marvinformatics.formatter.javascript.JavascriptFormatter;
import com.marvinformatics.formatter.model.ConfigReadException;
import com.marvinformatics.formatter.model.ConfigReader;
import com.marvinformatics.formatter.support.io.Resource;
import com.marvinformatics.formatter.support.io.Resource.UnknownResourceException;

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

	/**
	 * Execute.
	 *
	 * @throws MojoExecutionException
	 *             the mojo execution exception
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (this.skipFormatting) {
			getLog().info("Formatting is skipped");
			return;
		}

		Stopwatch watch = Stopwatch.createStarted();

		if (StringUtils.isEmpty(this.encoding)) {
			this.encoding = ReaderFactory.FILE_ENCODING;
			getLog().warn("File encoding has not been set, using platform encoding (" + this.encoding
					+ ") to format source files, i.e. build is platform dependent!");
		} else {
			try {
				"Test Encoding".getBytes(this.encoding);
			} catch (UnsupportedEncodingException e) {
				throw new MojoExecutionException("Encoding '" + this.encoding + "' is not supported");
			}
			getLog().info("Using '" + this.encoding + "' encoding to format source files.");
		}

		Stream<File> pathsToScan;
		if (this.directories != null)
			pathsToScan = Arrays.asList(this.directories).stream();
		else // Using defaults of source main and test dirs
			pathsToScan = Arrays.asList(this.sourceDirectory, this.testSourceDirectory).stream();

		ResultCollector rc = new ResultCollector();
		RecursiveWalk w = new RecursiveWalk(createJavaFormatter(), createJsFormatter(), rc,
				pathsToScan.filter(file -> isValidDirectory(file)).map(file -> file.toPath()), excludes());
		ForkJoinPool p = new ForkJoinPool(8);
		p.invoke(w);

		watch.stop();

		report(watch, rc);
	}

	private MatchPatterns excludes() {
		List<String> excludes;
		if (this.excludes != null)
			excludes = Lists.newArrayList(this.excludes);
		else
			excludes = Lists.newArrayList();
		excludes.addAll(Lists.newArrayList(AbstractScanner.DEFAULTEXCLUDES));

		return MatchPatterns.from(new StreamIterable<>(excludes.stream().map(pattern -> normalize(pattern))));
	}

	private String normalize(String pattern) {
		pattern = pattern.trim();

		if (pattern.startsWith(SelectorUtils.REGEX_HANDLER_PREFIX)) {
			if (File.separatorChar == '\\')
				pattern = StringUtils.replace(pattern, "/", "\\\\");
			else
				pattern = StringUtils.replace(pattern, "\\\\", "/");
		} else {
			pattern = pattern.replace(File.separatorChar == '/' ? '\\' : '/', File.separatorChar);

			if (pattern.endsWith(File.separator))
				pattern += "**";
		}

		return pattern;
	}

	protected void report(Stopwatch watch, ResultCollector rc) throws MojoFailureException, MojoExecutionException {
		Log log = getLog();
		log.info("Successfully formatted: " + rc.successCount() + " file(s)");
		log.info("Fail to format        : " + rc.failCount() + " file(s)");
		log.info("Skipped               : " + rc.skippedCount() + " file(s)");
		log.info("Approximate time taken: " + watch.elapsed(TimeUnit.SECONDS) + "s");
	}

	private ThreadLocal<JavaFormatter> createJavaFormatter() throws MojoExecutionException {
		Supplier<Map<String, String>> lazyConfig = () -> getFormattingOptions(configFile);
		return ThreadLocal.withInitial(() -> new JavaFormatter(lazyConfig.get(), FormatterMojo.this));
	}

	private ThreadLocal<JavascriptFormatter> createJsFormatter() throws MojoExecutionException {
		Supplier<Map<String, String>> lazyConfig = () -> getFormattingOptions(configJsFile);
		return ThreadLocal.withInitial(() -> new JavascriptFormatter(lazyConfig.get(), FormatterMojo.this));
	}

	private boolean isValidDirectory(File file) {
		return file != null && file.exists() && file.isDirectory();
	}

	/**
	 * Return the options to be passed when creating {@link CodeFormatter}
	 * instance.
	 *
	 * @return the formatting options
	 * @throws MojoExecutionException
	 *             the mojo execution exception
	 */
	private Map<String, String> getFormattingOptions(String cfgFile) {
		if (cfgFile == null)
			return new HashMap<String, String>();

		try {
			return getOptionsFromConfigFile(Resource.forPath(cfgFile));
		} catch (Resource.UnknownResourceException | MojoExecutionException e) {
			throw new IllegalStateException("Error loading Java config", e);
		}
	}

	/**
	 * Read config file and return the config as {@link Map}.
	 *
	 * @return the options from config file
	 * @throws MojoExecutionException
	 *             the mojo execution exception
	 */
	private Map<String, String> getOptionsFromConfigFile(Resource configFile) throws MojoExecutionException {

		try (InputStream configInput = configFile.asInputStream();) {
			ConfigReader configReader = new ConfigReader();
			return configReader.read(configInput);
		} catch (IOException | SAXException e) {
			throw new MojoExecutionException("Cannot parse config file [" + this.configFile + "]", e);
		} catch (ConfigReadException | UnknownResourceException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
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
}
