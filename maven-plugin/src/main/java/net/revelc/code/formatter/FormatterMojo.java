/**
 * Copyright 2010-2015. All work is copyrighted to their respective
 * author(s), unless otherwise stated.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.resource.ResourceManager;
import org.codehaus.plexus.resource.loader.FileResourceLoader;
import org.codehaus.plexus.resource.loader.ResourceNotFoundException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.WriterFactory;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.xml.sax.SAXException;

import com.google.common.hash.Hashing;

import net.revelc.code.formatter.java.JavaFormatter;
import net.revelc.code.formatter.javascript.JavascriptFormatter;
import net.revelc.code.formatter.model.ConfigReadException;
import net.revelc.code.formatter.model.ConfigReader;
import net.revelc.code.formatter.support.io.Resource;

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

	/** The Constant CACHE_PROPERTIES_FILENAME. */
	private static final String CACHE_PROPERTIES_FILENAME = "maven-java-formatter-cache.properties";
	/** The Constant DEFAULT_INCLUDES. */
	private static final String[] DEFAULT_INCLUDES = new String[]{"**/*.java","**/*.js"};

	/**
	 * ResourceManager for retrieving the configFile resource.
	 */
	@Component(role=ResourceManager.class)
	private ResourceManager resourceManager;

	/**
	 * Project's source directory as specified in the POM.
	 */
	@Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true, required = true)
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

	private JavaFormatter javaFormatter = new JavaFormatter();
	private JavascriptFormatter jsFormatter = new JavascriptFormatter();


	/**
	 * Execute.
	 *
	 * @throws MojoExecutionException the mojo execution exception
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (this.skipFormatting) {
			getLog().info("Formatting is skipped");
			return;
		}

		long startClock = System.currentTimeMillis();

		if (StringUtils.isEmpty(this.encoding)) {
			this.encoding = ReaderFactory.FILE_ENCODING;
			getLog().warn(
					"File encoding has not been set, using platform encoding ("
							+ this.encoding
							+ ") to format source files, i.e. build is platform dependent!");
		} else {
			try {
				"Test Encoding".getBytes(this.encoding);
			} catch (UnsupportedEncodingException e) {
				throw new MojoExecutionException("Encoding '" + this.encoding
						+ "' is not supported");
			}
			getLog().info(
					"Using '" + this.encoding + "' encoding to format source files.");
		}

		
		List<File> files = new ArrayList<>();
		try {
			if (this.directories != null) {
				for (File directory : this.directories) {
					if (directory.exists() && directory.isDirectory()) {
						files.addAll(addCollectionFiles(directory));
					}
				}
			} else { // Using defaults of source main and test dirs
				if (this.sourceDirectory != null && this.sourceDirectory.exists()
						&& this.sourceDirectory.isDirectory()) {
				files.addAll(addCollectionFiles(this.sourceDirectory));
				}
				if (this.testSourceDirectory != null && this.testSourceDirectory.exists()
						&& this.testSourceDirectory.isDirectory()) {
				files.addAll(addCollectionFiles(this.testSourceDirectory));
				}
			}
		} catch (IOException e) {
			throw new MojoExecutionException(
					"Unable to find files using includes/excludes", e);
		}

		int numberOfFiles = files.size();
		Log log = getLog();
		log.info("Number of files to be formatted: " + numberOfFiles);

		if (numberOfFiles > 0) {
			createCodeFormatter();
			ResultCollector rc = new ResultCollector();
			Properties hashCache = readFileHashCacheFile();

			String basedirPath = getBasedirPath();
			for (int i = 0, n = files.size(); i < n; i++) {
				File file = files.get(i);
				if(file.exists()) {
					formatFile(file, rc, hashCache, basedirPath);
				}
			}

			storeFileHashCache(hashCache);

			long endClock = System.currentTimeMillis();

			log.info("Successfully formatted: " + rc.successCount + " file(s)");
			log.info("Fail to format		: " + rc.failCount + " file(s)");
			log.info("Skipped				: " + rc.skippedCount + " file(s)");
			log.info("Approximate time taken: "
					+ ((endClock - startClock) / 1000) + "s");
		}
	}


	/**
	 * Add source files to the files list.
	 *
	 * @param files the files
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	List<File> addCollectionFiles(File newBasedir) throws IOException {
		final DirectoryScanner ds = new DirectoryScanner();
		ds.setBasedir(newBasedir);
		if (this.includes != null && this.includes.length > 0) {
			ds.setIncludes(this.includes);
		} else {
			ds.setIncludes(DEFAULT_INCLUDES);
		}

		ds.setExcludes( this.excludes );
		ds.addDefaultExcludes();
		ds.setCaseSensitive( false );
		ds.setFollowSymlinks( false );
		ds.scan();

		List<File> foundFiles = new ArrayList<>();
		for (String filename : ds.getIncludedFiles()) {
			foundFiles.add(new File(newBasedir, filename));
		}
		return foundFiles;
	}

	/**
	 * Gets the basedir path.
	
	 * @return the basedir path
	 */
	private String getBasedirPath() {
		try {
			return this.basedir.getCanonicalPath();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Store file hash cache.
	 *
	 * @param props the props
	 */
	private void storeFileHashCache(Properties props) {
		File cacheFile = new File(this.targetDirectory, CACHE_PROPERTIES_FILENAME);
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(cacheFile))) {
			props.store(out, null);
		} catch (FileNotFoundException e) {
			getLog().warn("Cannot store file hash cache properties file", e);
		} catch (IOException e) {
			getLog().warn("Cannot store file hash cache properties file", e);
		}
	}

	/**
	 * Read file hash cache file.
	 *
	 * @return the properties
	 */
	private Properties readFileHashCacheFile() {
		Properties props = new Properties();
		Log log = getLog();
		if (!this.targetDirectory.exists()) {
			this.targetDirectory.mkdirs();
		} else if (!this.targetDirectory.isDirectory()) {
			log.warn("Something strange here as the "
					+ "supposedly target directory is not a directory.");
			return props;
		}

		File cacheFile = new File(this.targetDirectory, CACHE_PROPERTIES_FILENAME);
		if (!cacheFile.exists()) {
			return props;
		}

		try (final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(cacheFile))) {
			props.load(stream);
		} catch (FileNotFoundException e) {
			log.warn("Cannot load file hash cache properties file", e);
		} catch (IOException e) {
			log.warn("Cannot load file hash cache properties file", e);
		}
		return props;
	}

	/**
	 * Format file.
	 *
	 * @param file the file
	 * @param rc the rc
	 * @param hashCache the hash cache
	 * @param basedirPath the basedir path
	 * @throws MojoFailureException
	 * @throws MojoExecutionException
	 */
	private void formatFile(File file, ResultCollector rc,
			Properties hashCache, String basedirPath)
			throws MojoFailureException, MojoExecutionException {
		try {
			doFormatFile(file, rc, hashCache, basedirPath, false);
		} catch (IOException e) {
			rc.failCount++;
			getLog().warn(e);
		} catch (MalformedTreeException e) {
			rc.failCount++;
			getLog().warn(e);
		} catch (BadLocationException e) {
			rc.failCount++;
			getLog().warn(e);
		}
	}

	/**
	 * Format individual file.
	 *
	 * @param file the file
	 * @param rc the rc
	 * @param hashCache the hash cache
	 * @param basedirPath the basedir path
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws BadLocationException the bad location exception
	 * @throws MojoFailureException
	 * @throws MojoExecutionException
	 */
	protected void doFormatFile(File file, ResultCollector rc,
			Properties hashCache, String basedirPath, boolean dryRun)
			throws IOException, BadLocationException, MojoFailureException,
			MojoExecutionException  {
		Log log = getLog();
		log.debug("Processing file: " + file);
		String code = readFileAsString(file);
		String originalHash = md5hash(code);

		String canonicalPath = file.getCanonicalPath();
		String path = canonicalPath.substring(basedirPath.length());
		String cachedHash = hashCache.getProperty(path);
		if (cachedHash != null && cachedHash.equals(originalHash)) {
			rc.skippedCount++;
			log.debug("File is already formatted.");
			return;
		}

		Result r;
		if (file.getName().endsWith(".java")) {
			r = this.javaFormatter.formatFile(file, this.lineEnding, dryRun);
		} else {
			r = this.jsFormatter.formatFile(file, this.lineEnding, dryRun);
		}

		switch (r) {
			case SKIPPED :
				rc.skippedCount++;
				break;
			case SUCCESS :
				rc.successCount++;
				break;
			case FAIL :
				rc.failCount++;
				break;
			default:
				break;
		}

		String formattedCode = readFileAsString(file);
		String formattedHash = md5hash(formattedCode);
		hashCache.setProperty(path, formattedHash);

		if (originalHash.equals(formattedHash)) {
			rc.skippedCount++;
			log.debug("Equal hash code. Not writing result to file.");
			return;
		}

		writeStringToFile(formattedCode, file);
	}

	/**
	 * Md5hash.
	 *
	 * @param str the str
	 * @return the string
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private String md5hash(String str) throws UnsupportedEncodingException {
		return Hashing.md5().hashBytes(str.getBytes(this.encoding)).toString();
	}

	/**
	 * Read the given file and return the content as a string.
	 *
	 * @param file the file
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String readFileAsString(File file) throws java.io.IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(ReaderFactory.newReader(file, this.encoding));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} finally {
			IOUtil.close(reader);
		}
		return fileData.toString();
	}

	/**
	 * Write the given string to a file.
	 *
	 * @param str the str
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeStringToFile(String str, File file) throws IOException {
		if (!file.exists() && file.isDirectory()) {
			return;
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(WriterFactory.newWriter(file, this.encoding));
			bw.write(str);
		} finally {
			IOUtil.close(bw);
		}
	}

	/**
	 * Create a {@link CodeFormatter} instance to be used by this mojo.
	 *
	 * @throws MojoExecutionException the mojo execution exception
	 */
	private void createCodeFormatter() throws MojoExecutionException {
		Resource configFileResource = null;
		Resource configJsFileResource = null;

		try {
			if (this.configFile != null) {
				configFileResource = Resource.forPath(this.configFile);
			}
		} catch (Resource.UnknownResourceException e) {
			throw new MojoExecutionException("Error loading Java config", e);
		}

		try {
			if (this.configJsFile != null) {
				configJsFileResource = Resource.forPath(this.configJsFile);
			}
		} catch (Resource.UnknownResourceException e) {
			throw new MojoExecutionException("Error loading JS config", e);
		}

		this.javaFormatter.init(getFormattingOptions(configFileResource), this);
		this.jsFormatter.init(getFormattingOptions(configJsFileResource), this);
	}

	/**
	 * Return the options to be passed when creating {@link CodeFormatter}
	 * instance.
	 *
	 * @return the formatting options
	 * @throws MojoExecutionException the mojo execution exception
	 */
	private Map<String, String> getFormattingOptions(Resource newConfigFile)
			throws MojoExecutionException {
		if (newConfigFile != null)	{
			return getOptionsFromConfigFile(newConfigFile);
		}

		Map<String, String> options = new HashMap<>();
		options.put(JavaCore.COMPILER_SOURCE, this.compilerSource);
		options.put(JavaCore.COMPILER_COMPLIANCE, this.compilerCompliance);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				this.compilerTargetPlatform);

		return options;
	}

	/**
	 * Read config file and return the config as {@link Map}.
	 *
	 * @return the options from config file
	 * @throws MojoExecutionException the mojo execution exception
	 */
	private Map<String, String> getOptionsFromConfigFile(Resource configFile)
			throws MojoExecutionException {

		InputStream configInput = null;

		try {
			this.resourceManager.addSearchPath(FileResourceLoader.ID,
							this.basedir.getAbsolutePath());
			configInput = this.resourceManager.getResourceAsInputStream(this.configFile);
		} catch (ResourceNotFoundException e) {
			throw new MojoExecutionException("Config file [" + this.configFile
					+ "] cannot be found", e);
		}
	
		if (configInput == null) {
			throw new MojoExecutionException("Config file [" + this.configFile
					+ "] does not exist");
		}
		try {
			
			ConfigReader configReader = new ConfigReader();
			return configReader.read(configInput);
		} catch (IOException e) {
			throw new MojoExecutionException("Cannot read config file ["
					+ this.configFile + "]", e);
		} catch (SAXException e) {
			throw new MojoExecutionException("Cannot parse config file ["
					+ this.configFile + "]", e);
		} catch (ConfigReadException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} finally {
			IOUtil.close(configInput);
		}
	}

	class ResultCollector {
		int successCount;

		int failCount;

		int skippedCount;
	}

	@Override
	public String getCompilerSources() {
		return this.compilerSource;
	}

	@Override
	public String getCompilerCompliance() {
		return this.compilerCompliance;
	}

	@Override
	public String getCompilerCodegenTargetPlatform() {
		return this.compilerTargetPlatform;
	}

	@Override
	public File getTargetDirectory() {
		return this.targetDirectory;
	}

	@Override
	public Charset getEncoding() {
		return Charset.forName(this.encoding);
	}
}
