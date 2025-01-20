/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.revelc.code.formatter;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.resource.ResourceManager;
import org.codehaus.plexus.resource.loader.FileResourceLoader;
import org.codehaus.plexus.resource.loader.ResourceNotFoundException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.xml.sax.SAXException;

import com.google.common.hash.Hashing;

import net.revelc.code.formatter.css.CssFormatter;
import net.revelc.code.formatter.html.HTMLFormatter;
import net.revelc.code.formatter.java.JavaFormatter;
import net.revelc.code.formatter.javascript.JavascriptFormatter;
import net.revelc.code.formatter.json.JsonFormatter;
import net.revelc.code.formatter.model.ConfigReadException;
import net.revelc.code.formatter.model.ConfigReader;
import net.revelc.code.formatter.xml.XMLFormatter;

/**
 * A Maven plugin mojo to format Java source code using the Eclipse code formatter.
 * <p>
 * Mojo parameters allow customizing formatting by specifying the config XML file, line endings, compiler version, and
 * source code locations. Reformatting source files is avoided using an sha512 hash of the content, comparing to the
 * original hash to the hash after formatting and a cached hash.
 */
@Mojo(name = FormatterMojo.FORMAT_MOJO_NAME, defaultPhase = LifecyclePhase.PROCESS_SOURCES, requiresProject = true, threadSafe = true)
public class FormatterMojo extends AbstractMojo implements ConfigurationSource {

    static final String FORMAT_MOJO_NAME = "format";

    /** The Constant CACHE_PROPERTIES_FILENAME. */
    private static final String CACHE_PROPERTIES_FILENAME = "formatter-maven-cache.properties";

    /** The Constant DEFAULT_INCLUDES. */
    private static final String[] DEFAULT_INCLUDES = { "**/*.css", "**/*.json", "**/*.html", "**/*.java", "**/*.js",
            "**/*.xml" };

    /** The Constant REMOVE_TRAILING_PATTERN. */
    private static final Pattern REMOVE_TRAILING_PATTERN = Pattern.compile("\\p{Blank}+$", Pattern.MULTILINE);

    /** The Constant FORMATTER_SKIPPED with preceding space. */
    private static final String FORMATTING_IS_SKIPPED = " formatting is skipped";

    /**
     * ResourceManager for retrieving the configFile resource.
     */
    @Inject
    private ResourceManager resourceManager;

    /**
     * The Maven project for retrieving the resources.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Project's source directory as specified in the POM.
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
    private File sourceDirectory;

    /**
     * Project's test source directory as specified in the POM.
     */
    @Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "testSourceDirectory", required = true)
    private File testSourceDirectory;

    /**
     * Project's target directory as specified in the POM.
     */
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private File targetDirectory;

    /**
     * Project's base directory as specified in the POM.
     */
    @Parameter(defaultValue = "${project.basedir}", property = "baseDirectory", readonly = true, required = true)
    private File basedir;

    /**
     * Projects cache directory.
     * <p>
     * This file is a hash cache of the files in the project source. It can be preserved in source code such that it
     * ensures builds are always fast by not unnecessarily writing files constantly. It can also be added to gitignore
     * in case startup is not necessary. It further can be redirected to another location.
     * <p>
     * When stored in the repository, the cache if run on cross platforms will display the files multiple times due to
     * line ending differences on the platform.
     * <p>
     * The cache itself has been part of formatter plugin for a long time but was hidden in target directory and did not
     * survive clean phase when it should. This is not intended to be clean in that way as one would want as close to a
     * no-op as possible when files are already all formatted and/or have not been otherwise touched. This is used based
     * off the files in the project so it is as much part of the source as any other file is.
     * <p>
     * The cache can become invalid for any number of reasons that this plugin can't reasonably detect automatically. If
     * you rely on the cache and make any changes to the project that could conceivably make the cache invalid, or if
     * you notice that files aren't being reformatted when they should, just delete the cache and it will be rebuilt.
     *
     * @since 2.12.1
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "formatter.cachedir")
    private File cachedir;

    /**
     * Location of the Java source files to format. Defaults to source main and test directories if not set. Deprecated
     * in version 0.3. Reintroduced in 0.4.
     *
     * @since 0.4
     */
    @Parameter
    private File[] directories;

    /**
     * List of fileset patterns for Java source locations to include in formatting. Patterns are relative to the project
     * source and test source directories. When not specified, the default include is <code>**&#47;*.java</code>
     *
     * @since 0.3
     */
    @Parameter(property = "formatter.includes")
    private String[] includes;

    /**
     * List of fileset patterns for Java source locations to exclude from formatting. Patterns are relative to the
     * project source and test source directories. When not specified, there is no default exclude.
     *
     * @since 0.3
     */
    @Parameter(property = "formatter.excludes")
    private String[] excludes;

    /**
     * Java compiler source version.
     */
    @Parameter(defaultValue = "1.8", property = "maven.compiler.source", required = true)
    private String compilerSource;

    /**
     * Java compiler compliance version.
     */
    @Parameter(defaultValue = "1.8", property = "maven.compiler.source", required = true)
    private String compilerCompliance;

    /**
     * Java compiler target version.
     */
    @Parameter(defaultValue = "1.8", property = "maven.compiler.target", required = true)
    private String compilerTargetPlatform;

    /**
     * The file encoding used to read and write source files. When not specified and sourceEncoding also not set,
     * default is platform file encoding.
     *
     * @since 0.3
     */
    @Parameter(property = "project.build.sourceEncoding", required = true)
    private String encoding;

    /**
     * Sets the line-ending of files after formatting. Valid values are:
     * <ul>
     * <li><b>"AUTO"</b> - Use line endings of current system</li>
     * <li><b>"KEEP"</b> - Preserve line endings of files, default to AUTO if ambiguous</li>
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
     * File or classpath location of an Eclipse code formatter configuration xml file to use in formatting.
     */
    @Parameter(defaultValue = "formatter-maven-plugin/eclipse/java.xml", property = "configfile", required = true)
    private String configFile;

    /**
     * File or classpath location of an Eclipse code formatter configuration xml file to use in formatting.
     */
    @Parameter(defaultValue = "formatter-maven-plugin/eclipse/javascript.xml", property = "configjsfile", required = true)
    private String configJsFile;

    /**
     * File or classpath location of a properties file to use in html formatting.
     */
    @Parameter(defaultValue = "formatter-maven-plugin/jsoup/html.properties", property = "confightmlfile", required = true)
    private String configHtmlFile;

    /**
     * File or classpath location of a properties file to use in xml formatting.
     */
    @Parameter(defaultValue = "formatter-maven-plugin/eclipse/xml.properties", property = "configxmlfile", required = true)
    private String configXmlFile;

    /**
     * File or classpath location of a properties file to use in json formatting.
     */
    @Parameter(defaultValue = "formatter-maven-plugin/jackson/json.properties", property = "configjsonfile", required = true)
    private String configJsonFile;

    /**
     * File or classpath location of a properties file to use in css formatting.
     */
    @Parameter(defaultValue = "formatter-maven-plugin/ph-css/css.properties", property = "configcssfile", required = true)
    private String configCssFile;

    /**
     * Whether the formatting cache is skipped.
     *
     * @since 2.23.0
     */
    @Parameter(defaultValue = "false", property = "formatter.cache.skip")
    private boolean skipFormattingCache;

    /**
     * Whether the java formatting is skipped.
     */
    @Parameter(defaultValue = "false", property = "formatter.java.skip")
    private boolean skipJavaFormatting;

    /**
     * Whether the javascript formatting is skipped.
     */
    @Parameter(defaultValue = "false", property = "formatter.js.skip")
    private boolean skipJsFormatting;

    /**
     * Whether the html formatting is skipped.
     */
    @Parameter(defaultValue = "false", property = "formatter.html.skip")
    private boolean skipHtmlFormatting;

    /**
     * Whether the xml formatting is skipped.
     */
    @Parameter(defaultValue = "false", property = "formatter.xml.skip")
    private boolean skipXmlFormatting;

    /**
     * Whether the json formatting is skipped.
     */
    @Parameter(defaultValue = "false", property = "formatter.json.skip")
    private boolean skipJsonFormatting;

    /**
     * Whether the css formatting is skipped.
     */
    @Parameter(defaultValue = "false", property = "formatter.css.skip")
    private boolean skipCssFormatting;

    /**
     * Whether the formatting is skipped.
     *
     * @since 0.5
     */
    @Parameter(defaultValue = "false", alias = "skip", property = "formatter.skip")
    private boolean skipFormatting;

    /**
     * Use eclipse defaults when set to true for java and javascript.
     */
    @Parameter(defaultValue = "false", property = "formatter.useEclipseDefaults")
    private boolean useEclipseDefaults;

    /**
     * A java regular expression pattern that can be used to exclude some portions of the java code from being
     * reformatted.
     * <p>
     * This can be useful when using DSL that embeds some kind of semantic hierarchy, where users can use various
     * indentation level to increase the readability of the code. Those semantics are ignored by the formatter, so this
     * regex pattern can be used to match certain portions of the code so that they will not be reformatted.
     * <p>
     * An example is the Apache Camel java DSL which can be used in the following way: <code><pre>
     * 	from("seda:a").routeId("a")
     * 			.log("routing at ${routeId}")
     * 			.multicast()
     * 				.to("seda:b")
     * 				.to("seda:c")
     * 			.end()
     * 			.log("End of routing");
     * </pre></code>
     * <p>
     * In the above example, the except can be skipped by the formatter by defining the following property in the
     * formatter xml configuration: <code><pre>
     *   &lt;javaExclusionPattern>\b(from\([^;]*\.end[^;]*?\)\));&lt;/javaExclusionPattern>
     * </pre></code>
     *
     * @since 2.13
     */
    @Parameter(property = "formatter.java.exclusion_pattern")
    private String javaExclusionPattern;

    /**
     * When set to true, remove trailing whitespace on all lines after the formatter has finished.
     * <p>
     * Default to 'true' since 2.18.0
     *
     * @since 2.17.0
     */
    @Parameter(defaultValue = "true", property = "formatter.removeTrailingWhitespace")
    private boolean removeTrailingWhitespace;

    /**
     * When set to true, the resources for the project are included in formatting. This includes both the main and test
     * resources.
     * <p>
     * The included/excluded patterns for this plugin are honored as well as the included/excluded patterns from the
     * resource itself.
     * </p>
     *
     * @since 2.22.0
     */
    @Parameter(defaultValue = "false", property = "formatter.includeResources")
    private boolean includeResources;

    /** The java formatter. */
    private final JavaFormatter javaFormatter = new JavaFormatter();

    /** The js formatter. */
    private final JavascriptFormatter jsFormatter = new JavascriptFormatter();

    /** The html formatter. */
    private final HTMLFormatter htmlFormatter = new HTMLFormatter();

    /** The xml formatter. */
    private final XMLFormatter xmlFormatter = new XMLFormatter();

    /** The json formatter. */
    private final JsonFormatter jsonFormatter = new JsonFormatter();

    /** The css formatter. */
    private final CssFormatter cssFormatter = new CssFormatter();

    /** The hash cache written. */
    private boolean hashCacheWritten;

    /**
     * Execute.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     *
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.skipFormatting) {
            this.getLog().info("Formatting is skipped");
            return;
        }

        final var startClock = System.nanoTime();

        if (this.encoding == null || this.encoding.length() == 0) {
            this.encoding = Charset.defaultCharset().displayName();
            this.getLog().warn("File encoding has not been set, using platform encoding (" + this.encoding
                    + ") to format source files, i.e. build is platform dependent!");
        } else {
            if (!Charset.isSupported(this.encoding)) {
                throw new MojoExecutionException("Encoding '" + this.encoding + "' is not supported");
            }
            this.getLog().debug("Using '" + this.encoding + "' encoding to format source files.");
        }

        final List<Path> files = new ArrayList<>();
        if (this.directories != null) {
            for (final File directory : this.directories) {
                if (directory.exists() && directory.isDirectory()) {
                    files.addAll(this.addCollectionFiles(directory.toPath()));
                }
            }
        } else {
            // Using defaults of source main and test dirs
            if (this.sourceDirectory != null && this.sourceDirectory.exists() && this.sourceDirectory.isDirectory()) {
                files.addAll(this.addCollectionFiles(this.sourceDirectory.toPath()));
            }
            if (this.testSourceDirectory != null && this.testSourceDirectory.exists()
                    && this.testSourceDirectory.isDirectory()) {
                files.addAll(this.addCollectionFiles(this.testSourceDirectory.toPath()));
            }
        }
        // If including resources in formatting, format both the main and test resources
        if (includeResources) {
            for (Resource resource : project.getResources()) {
                files.addAll(this.addCollectionFiles(resource));
            }
            for (Resource resource : project.getTestResources()) {
                files.addAll(this.addCollectionFiles(resource));
            }
        }

        this.logSkippedTypes();

        final var numberOfFiles = files.size();
        final var log = this.getLog();

        // reduce logging noise
        final var msg = "Number of files to be formatted: " + numberOfFiles;
        log.debug(msg);

        if (numberOfFiles == 0) {
            return;
        }

        this.createCodeFormatter();
        final var rc = new ResultCollector();
        final var hashCache = this.readFileHashCacheFile();

        final var basedirPath = this.getBasedirPath();
        for (final Path file : files) {
            if (Files.exists(file)) {
                if (Files.isWritable(file)) {
                    this.formatFile(file, rc, hashCache, basedirPath);
                } else {
                    rc.readOnlyCount++;
                    this.getLog().warn("File " + file + " is read only");
                }
            } else {
                rc.failCount++;
                this.getLog().error("File " + file + " does not exist");
            }
        }

        // Only store the cache if it changed during processing to avoid java properties timestamp writing for
        // those that want to save the cache
        if (this.hashCacheWritten) {
            this.storeFileHashCache(hashCache);
        }

        final var duration = NANOSECONDS.toMillis(System.nanoTime() - startClock);
        final var elapsed = TimeUtil.printDuration(duration);

        final var results = String.format(
                "Processed %d files in %s (Formatted: %d, Skipped: %d, Unchanged: %d, Failed: %d, Readonly: %d)",
                numberOfFiles, elapsed, rc.successCount, rc.skippedCount, rc.unchangedCount, rc.failCount,
                rc.readOnlyCount);

        if (rc.failCount > 0) {
            this.getLog().error(results);
        } else if (rc.readOnlyCount > 0) {
            this.getLog().warn(results);
        } else {
            this.getLog().info(results);
        }
    }

    /**
     * Add source files to the files list.
     *
     * @param newBasedir
     *            the new basedir
     *
     * @return the list
     */
    List<Path> addCollectionFiles(final Path newBasedir) {
        final var ds = new DirectoryScanner();
        ds.setBasedir(newBasedir.toFile());
        if (this.includes != null && this.includes.length > 0) {
            ds.setIncludes(this.includes);
        } else {
            ds.setIncludes(FormatterMojo.DEFAULT_INCLUDES);
        }

        ds.setExcludes(this.excludes);
        ds.addDefaultExcludes();
        ds.setCaseSensitive(false);
        ds.setFollowSymlinks(false);
        ds.scan();

        final List<Path> foundFiles = new ArrayList<>();
        for (final String filename : ds.getIncludedFiles()) {
            foundFiles.add(Path.of(newBasedir.toString(), filename));
        }
        return foundFiles;
    }

    /**
     * Add source files to the files list.
     *
     * @param resource
     *            the resource to add
     *
     * @return the list
     */
    private List<Path> addCollectionFiles(final Resource resource) {
        final var newBasedir = Path.of(resource.getDirectory());
        if (!Files.exists(newBasedir)) {
            final var log = getLog();
            if (log.isDebugEnabled()) {
                log.debug(String.format("Skipping non-existing directory %s", newBasedir));
            }
            return List.of();
        }
        final var ds = new DirectoryScanner();
        ds.setBasedir(newBasedir.toFile());

        // Check includes. We want to process both what the user includes from this plugin and what the resource itself
        // includes.
        final List<String> included = new ArrayList<>();
        if (this.includes != null && this.includes.length > 0) {
            Collections.addAll(included, this.includes);
        }
        if (resource.getIncludes().isEmpty()) {
            Collections.addAll(included, DEFAULT_INCLUDES);
        } else {
            included.addAll(resource.getIncludes());
        }
        ds.setIncludes(included.toArray(new String[0]));

        // Check excludes. We want to process both what the user exclude from this plugin and what the resource itself
        // excludes.
        final List<String> excluded = new ArrayList<>();
        if (this.excludes != null && this.excludes.length > 0) {
            Collections.addAll(excluded, this.excludes);
        }
        excluded.addAll(resource.getExcludes());
        if (!excluded.isEmpty()) {
            ds.setExcludes(excluded.toArray(new String[0]));
        }
        ds.addDefaultExcludes();
        ds.setCaseSensitive(false);
        ds.setFollowSymlinks(false);
        ds.scan();

        return Stream.of(ds.getIncludedFiles()).map(filename -> Path.of(newBasedir.toString(), filename)).toList();
    }

    /**
     * Gets the basedir path.
     *
     * @return the basedir path
     */
    private String getBasedirPath() {
        try {
            return this.basedir.getCanonicalPath();
        } catch (final IOException e) {
            this.getLog().debug("", e);
            return "";
        }
    }

    /**
     * Store file hash cache.
     *
     * @param props
     *            the props
     */
    private void storeFileHashCache(final Properties props) {
        final var cacheFile = Path.of(this.cachedir.getAbsolutePath(), CACHE_PROPERTIES_FILENAME);
        try (var sw = new StringWriter()) {
            props.store(sw, null);
            getLog().debug("Writing sorted files to cache without timestamp:\n\n" + props);
            Files.write(cacheFile, (Iterable<String>) sw.toString().lines().skip(1).sorted().toList(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (final IOException e) {
            this.getLog().warn("Cannot store file hash cache properties file", e);
        }
    }

    /**
     * Read file hash cache file.
     *
     * @return the properties
     */
    private Properties readFileHashCacheFile() {
        final var props = new Properties();
        final var log = this.getLog();
        if (!this.cachedir.exists()) {
            this.cachedir.mkdirs();
        } else if (!this.cachedir.isDirectory()) {
            log.warn("Something strange here as the '" + this.cachedir
                    + "' supposedly cache directory is not a directory.");
            return props;
        }

        final var cacheFile = Path.of(this.cachedir.toString(), FormatterMojo.CACHE_PROPERTIES_FILENAME);
        if (!Files.exists(cacheFile)) {
            return props;
        }

        try (var reader = Files.newBufferedReader(cacheFile)) {
            props.load(reader);
        } catch (final IOException e) {
            log.warn("Cannot load file hash cache properties file", e);
        }
        return props;
    }

    /**
     * Format file.
     *
     * @param file
     *            the file
     * @param rc
     *            the rc
     * @param hashCache
     *            the hash cache
     * @param basedirPath
     *            the basedir path
     *
     * @throws MojoFailureException
     *             the mojo failure exception
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    private void formatFile(final Path file, final ResultCollector rc, final Properties hashCache,
            final String basedirPath) throws MojoFailureException, MojoExecutionException {
        try {
            this.doFormatFile(file, rc, hashCache, basedirPath, false);
        } catch (IOException | MalformedTreeException | BadLocationException e) {
            rc.failCount++;
            this.getLog().error(e);
        }
    }

    /**
     * Format individual file.
     *
     * @param file
     *            the file
     * @param rc
     *            the rc
     * @param hashCache
     *            the hash cache
     * @param basedirPath
     *            the basedir path
     * @param dryRun
     *            the dry run
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws BadLocationException
     *             the bad location exception
     * @throws MojoFailureException
     *             the mojo failure exception
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    protected void doFormatFile(final Path file, final ResultCollector rc, final Properties hashCache,
            final String basedirPath, final boolean dryRun)
            throws IOException, BadLocationException, MojoFailureException, MojoExecutionException {
        final var log = this.getLog();
        log.debug("Processing file: " + file);
        final var fileName = file.toFile().getName();
        final var originalCode = this.readFileAsString(file);
        final var originalHash = this.calculateHash(fileName, originalCode);
        final var canonicalPath = file.toFile().getCanonicalPath();
        final var path = canonicalPath.substring(basedirPath.length());
        final var cachedHash = hashCache.getProperty(path);
        if (!skipFormattingCache && cachedHash != null && cachedHash.equals(originalHash)) {
            rc.skippedCount++;
            log.debug("Cache hit: file is already formatted.");
            return;
        }

        Result result = null;
        String formattedCode = null;
        if (fileName.endsWith(".java") && this.javaFormatter.isInitialized()) {
            if (this.skipJavaFormatting) {
                log.debug(Type.JAVA + FormatterMojo.FORMATTING_IS_SKIPPED);
                result = Result.SKIPPED;
            } else {
                formattedCode = this.javaFormatter.formatFile(file, originalCode, this.lineEnding);
            }
        } else if (fileName.endsWith(".js") && this.jsFormatter.isInitialized()) {
            if (this.skipJsFormatting) {
                log.debug(Type.JAVASCRIPT + FormatterMojo.FORMATTING_IS_SKIPPED);
                result = Result.SKIPPED;
            } else {
                formattedCode = this.jsFormatter.formatFile(file, originalCode, this.lineEnding);
            }
        } else if (fileName.endsWith(".html") && this.htmlFormatter.isInitialized()) {
            if (this.skipHtmlFormatting) {
                log.debug(Type.HTML + FormatterMojo.FORMATTING_IS_SKIPPED);
                result = Result.SKIPPED;
            } else {
                formattedCode = this.htmlFormatter.formatFile(file, originalCode, this.lineEnding);
            }
        } else if (fileName.endsWith(".xml") && this.xmlFormatter.isInitialized()) {
            if (this.skipXmlFormatting) {
                log.debug(Type.XML + FormatterMojo.FORMATTING_IS_SKIPPED);
                result = Result.SKIPPED;
            } else {
                formattedCode = this.xmlFormatter.formatFile(file, originalCode, this.lineEnding);
            }
        } else if (fileName.endsWith(".json") && this.jsonFormatter.isInitialized()) {
            if (this.skipJsonFormatting) {
                log.debug(Type.JSON + FormatterMojo.FORMATTING_IS_SKIPPED);
                result = Result.SKIPPED;
            } else {
                formattedCode = this.jsonFormatter.formatFile(file, originalCode, this.lineEnding);
            }
        } else if (fileName.endsWith(".css") && this.cssFormatter.isInitialized()) {
            if (this.skipCssFormatting) {
                log.debug(Type.CSS + FormatterMojo.FORMATTING_IS_SKIPPED);
                result = Result.SKIPPED;
            } else {
                formattedCode = this.cssFormatter.formatFile(file, originalCode, this.lineEnding);
            }
        } else {
            log.debug("No formatter found or initialization failed for file " + fileName);
            result = Result.SKIPPED;
        }

        // If not skipped, check formatting result
        if (!Result.SKIPPED.equals(result)) {
            if (formattedCode == null) {
                result = Result.FAIL;
            } else {
                // Process the source one more time and remove any trailing whitespace found
                if (this.removeTrailingWhitespace) {
                    formattedCode = FormatterMojo.REMOVE_TRAILING_PATTERN.matcher(formattedCode).replaceAll("");
                }
                result = originalCode.equals(formattedCode) ? Result.SKIPPED : Result.SUCCESS;
            }
        }

        // Process the result type
        if (Result.SKIPPED.equals(result)) {
            // Use unchanged count for files that either 'skipFormattingCache' or cache missed but flagged to skip
            // formatting.
            rc.unchangedCount++;
        } else if (Result.SUCCESS.equals(result)) {
            rc.successCount++;
        } else if (Result.FAIL.equals(result)) {
            rc.failCount++;
            return;
        }

        // Write the cache
        String formattedHash;
        if (Result.SKIPPED.equals(result)) {
            formattedHash = originalHash;
        } else {
            formattedHash = this.calculateHash(fileName, formattedCode);
        }
        hashCache.setProperty(path, formattedHash);
        this.hashCacheWritten = true;

        // If we had determined to skip write, do so now after cache was written
        if (Result.SKIPPED.equals(result)) {
            log.debug("File is already formatted. Writing to cache only.");
            return;
        }

        // As a safety check, if our hash matches, skip the write of file (should never occur)
        if (originalHash.equals(formattedHash)) {
            rc.skippedCount++;
            log.debug("Equal hash code. Not writing result to file.");
            return;
        }

        // Now write the file
        if (!dryRun) {
            this.writeStringToFile(formattedCode, file);
        }
    }

    /**
     * Calculate Hash.
     *
     * @param fileType
     *            the file type being processed
     * @param code
     *            the code file for generating the hash
     *
     * @return the calculated hash for the file
     */
    private String calculateHash(final String fileType, final String code) {
        // Include formatter options with each known type
        if (fileType.endsWith(".java")) {
            return this.sha512hash(code + this.javaFormatter.getOptions().hashCode());
        }

        if (fileType.endsWith(".js")) {
            return this.sha512hash(code + this.jsFormatter.getOptions().hashCode());
        }

        if (fileType.endsWith(".html")) {
            return this.sha512hash(code + this.htmlFormatter.getOptions().hashCode());
        }

        if (fileType.endsWith(".xml")) {
            return this.sha512hash(code + this.xmlFormatter.getOptions().hashCode());
        }

        if (fileType.endsWith(".json")) {
            return this.sha512hash(code + this.jsonFormatter.getOptions().hashCode());
        }

        if (fileType.endsWith(".css")) {
            return this.sha512hash(code + this.cssFormatter.getOptions().hashCode());
        }

        // Default to formatted hashing for unknown type
        return this.sha512hash(code);
    }

    /**
     * sha512hash.
     *
     * @param str
     *            the str
     *
     * @return the string
     */
    private String sha512hash(final String str) {
        return Hashing.sha512().hashBytes(str.getBytes(this.getEncoding())).toString();
    }

    /**
     * Read the given file and return the content as a string.
     *
     * @param file
     *            the file
     *
     * @return the string
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private String readFileAsString(final Path file) throws IOException {
        final var fileData = new StringBuilder(1000);
        try (var reader = Files.newBufferedReader(file, Charset.forName(this.encoding))) {
            var buffer = new char[1024];
            var numRead = 0;
            while ((numRead = reader.read(buffer)) != -1) {
                final var readData = String.valueOf(buffer, 0, numRead);
                fileData.append(readData);
                buffer = new char[1024];
            }
        }
        return fileData.toString();
    }

    /**
     * Write the given string to a file.
     *
     * @param str
     *            the str
     * @param file
     *            the file
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void writeStringToFile(final String str, final Path file) throws IOException {
        if (!Files.exists(file) && Files.isDirectory(file)) {
            return;
        }

        try (var buffer = Files.newBufferedWriter(file, Charset.forName(this.encoding))) {
            buffer.write(str);
        }
    }

    /**
     * Create a {@link CodeFormatter} instance to be used by this mojo.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    private void createCodeFormatter() throws MojoExecutionException {
        // Java Setup
        final var javaFormattingOptions = this.getFormattingOptions(this.configFile);
        if (javaFormattingOptions != null) {
            this.javaFormatter.init(javaFormattingOptions, this);
        }
        if (this.javaExclusionPattern != null) {
            this.javaFormatter.setExclusionPattern(this.javaExclusionPattern);
        }
        // Javascript Setup
        final var jsFormattingOptions = this.getFormattingOptions(this.configJsFile);
        if (jsFormattingOptions != null) {
            this.jsFormatter.init(jsFormattingOptions, this);
        }
        // Html Setup
        if (this.configHtmlFile != null) {
            this.htmlFormatter.init(this.getOptionsFromPropertiesFile(this.configHtmlFile), this);
        }
        // Xml Setup
        if (this.configXmlFile != null) {
            final var xmlFormattingOptions = this.getOptionsFromPropertiesFile(this.configXmlFile);
            if (this.lineEnding != LineEnding.KEEP) {
                xmlFormattingOptions.put("lineending", this.lineEnding.getChars());
            }
            this.xmlFormatter.init(xmlFormattingOptions, this);
        }
        // Json Setup
        if (this.configJsonFile != null) {
            final var jsonFormattingOptions = this.getOptionsFromPropertiesFile(this.configJsonFile);
            if (this.lineEnding != LineEnding.KEEP) {
                jsonFormattingOptions.put("lineending", this.lineEnding.getChars());
            }
            this.jsonFormatter.init(jsonFormattingOptions, this);
        }
        // Css Setup
        if (this.configCssFile != null) {
            this.cssFormatter.init(this.getOptionsFromPropertiesFile(this.configCssFile), this);
        }
        // stop the process if not config files where found
        if (javaFormattingOptions == null && jsFormattingOptions == null && this.configHtmlFile == null
                && this.configXmlFile == null && this.configJsonFile == null && this.configCssFile == null) {
            throw new MojoExecutionException(
                    "You must provide a Java, Javascript, HTML, XML, JSON, or CSS configuration file.");
        }
    }

    /**
     * Return the options to be passed when creating {@link CodeFormatter} instance.
     *
     * @param newConfigFile
     *            the new config file
     *
     * @return the formatting options or null if not config file found
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    private Map<String, String> getFormattingOptions(final String newConfigFile) throws MojoExecutionException {
        if (this.useEclipseDefaults) {
            this.getLog().info("Using Eclipse Defaults");
            // Use defaults only for formatting
            final Map<String, String> options = new HashMap<>();
            options.put(JavaCore.COMPILER_SOURCE, this.compilerSource);
            options.put(JavaCore.COMPILER_COMPLIANCE, this.compilerCompliance);
            options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, this.compilerTargetPlatform);
            return options;
        }

        return this.getOptionsFromConfigFile(newConfigFile);
    }

    /**
     * Read config file and return the config as {@link Map}.
     *
     * @param newConfigFile
     *            the new config file
     *
     * @return the options from config file
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    private Map<String, String> getOptionsFromConfigFile(final String newConfigFile) throws MojoExecutionException {

        this.getLog().debug("Using search path at: " + this.basedir.getAbsolutePath());
        this.resourceManager.addSearchPath(FileResourceLoader.ID, this.basedir.getAbsolutePath());

        try (var configInput = this.resourceManager.getResourceAsInputStream(newConfigFile)) {
            return new ConfigReader().read(configInput);
        } catch (final ResourceNotFoundException e) {
            throw new MojoExecutionException("Cannot find config file [" + newConfigFile + "]");
        } catch (final IOException e) {
            throw new MojoExecutionException("Cannot read config file [" + newConfigFile + "]", e);
        } catch (final SAXException e) {
            throw new MojoExecutionException("Cannot parse config file [" + newConfigFile + "]", e);
        } catch (final ConfigReadException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * Read properties file and return the properties as {@link Map}.
     *
     * @param newPropertiesFile
     *            the new properties file
     *
     * @return the options from properties file or null if not properties file found
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    private Map<String, String> getOptionsFromPropertiesFile(final String newPropertiesFile)
            throws MojoExecutionException {

        this.getLog().debug("Using search path at: " + this.basedir.getAbsolutePath());
        this.resourceManager.addSearchPath(FileResourceLoader.ID, this.basedir.getAbsolutePath());

        final var properties = new Properties();
        try {
            properties.load(this.resourceManager.getResourceAsInputStream(newPropertiesFile));
        } catch (final ResourceNotFoundException e) {
            this.getLog().debug("Property file [" + newPropertiesFile + "] cannot be found", e);
            return new HashMap<>();
        } catch (final IOException e) {
            throw new MojoExecutionException("Cannot read config file [" + newPropertiesFile + "]", e);
        }

        final Map<String, String> map = new HashMap<>();
        for (final String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

    /**
     * Log a message with the list of types that are skipped from formatting. No message is logged if no type is
     * skipped.
     */
    private void logSkippedTypes() {
        final var skippedTypes = this.getSkippedTypes();
        if (skippedTypes.isEmpty()) {
            return;
        }
        final var skippedTypesStr = skippedTypes.stream().map(Type::toString).collect(Collectors.joining(", "));
        this.getLog().debug("Formatting is skipped for types: " + skippedTypesStr);
    }

    /**
     * Get a list of types that are skipped from formatting.
     *
     * @return a new list of skipped types; empty list if none are skipped.
     */
    private List<Type> getSkippedTypes() {
        final List<Type> skippedTypes = new ArrayList<>();

        if (this.skipJavaFormatting) {
            skippedTypes.add(Type.JAVA);
        }
        if (this.skipJsFormatting) {
            skippedTypes.add(Type.JAVASCRIPT);
        }
        if (this.skipHtmlFormatting) {
            skippedTypes.add(Type.HTML);
        }
        if (this.skipXmlFormatting) {
            skippedTypes.add(Type.XML);
        }
        if (this.skipJsonFormatting) {
            skippedTypes.add(Type.JSON);
        }
        if (this.skipCssFormatting) {
            skippedTypes.add(Type.CSS);
        }

        return skippedTypes;
    }

    /**
     * The Class ResultCollector.
     */
    static class ResultCollector {

        /** The success count. */
        int successCount;

        /** The fail count. */
        int failCount;

        /** The skipped count is incremented for cached files that haven't changed since being cached. */
        int skippedCount;

        /**
         * Use unchanged count for files that either 'skipFormattingCache' or cache missed but flagged to skip
         * formatting.
         */
        int unchangedCount;

        /** The read only count. */
        int readOnlyCount;
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
    public Path getTargetDirectory() {
        return this.targetDirectory.toPath();
    }

    @Override
    public Charset getEncoding() {
        return Charset.forName(this.encoding);
    }
}
