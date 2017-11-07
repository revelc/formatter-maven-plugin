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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.AbstractScanner;
import org.codehaus.plexus.util.MatchPatterns;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.SelectorUtils;
import org.codehaus.plexus.util.StringUtils;
import org.xml.sax.SAXException;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.marvinformatics.formatter.java.JavaFormatter;
import com.marvinformatics.formatter.javascript.JavascriptFormatter;
import com.marvinformatics.formatter.model.ConfigReadException;
import com.marvinformatics.formatter.model.ConfigReader;
import com.marvinformatics.formatter.support.io.Resource;
import com.marvinformatics.formatter.support.io.Resource.UnknownResourceException;

public class FormatterExecuter {

	private final FormatterMojo config;

	public FormatterExecuter(FormatterMojo config) {
		this.config = config;
	}

	/**
	 * Execute.
	 *
	 * @return
	 *
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public ResultCollector execute() {
		Stopwatch watch = Stopwatch.createStarted();
		ResultCollector rc = new ResultCollector(watch);

		String encoding;
		if (StringUtils.isEmpty(config.encoding())) {
			encoding = ReaderFactory.FILE_ENCODING;
		} else {
			encoding = config.encoding();
			try {
				"Test Encoding".getBytes(encoding);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Encoding '" + encoding + "' is not supported", e);
			}
		}

		Stream<File> pathsToScan = config.directories().stream();

		RecursiveWalk w = new RecursiveWalk(createJavaFormatter(), createJsFormatter(), rc,
				pathsToScan.filter(file -> isValidDirectory(file)).map(file -> file.toPath()), excludes());
		ForkJoinPool p = new ForkJoinPool(8);
		p.invoke(w);

		watch.stop();

		return rc;
	}

	private MatchPatterns excludes() {
		final List<String> excludes;
		if (config.excludes() != null)
			excludes = Lists.newArrayList(config.excludes());
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

	private ThreadLocal<CacheableFormatter> createJavaFormatter() {
		Supplier<Map<String, String>> lazyConfig = () -> getFormattingOptions(config.javaConfig());
		return ThreadLocal.withInitial(() -> {
			return new CacheableFormatter(config, new JavaFormatter(
					lazyConfig.get(),
					config.getCompilerSources(),
					config.getCompilerCompliance(),
					config.getCompilerCodegenTargetPlatform(),
					config.lineEnding()));
		});
	}

	private ThreadLocal<CacheableFormatter> createJsFormatter() {
		Supplier<Map<String, String>> lazyConfig = () -> getFormattingOptions(config.jsConfig());
		return ThreadLocal.withInitial(() -> {
			return new CacheableFormatter(config, new JavascriptFormatter(
					lazyConfig.get(),
					config.lineEnding()));
		});
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

		Resource resource = Resource.forPath(cfgFile);
		if (resource.exists())
			//FIXME add a warning
			return new HashMap<String, String>();

		try {
			return getOptionsFromConfigFile(resource);
		} catch (MojoExecutionException e) {
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
			throw new IllegalArgumentException("Cannot parse config file [" + configFile + "]", e);
		} catch (ConfigReadException | UnknownResourceException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
