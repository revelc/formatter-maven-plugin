/*
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
package net.revelc.code.formatter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.MalformedTreeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class FormatterTask extends Task implements ConfigurationSource {

    private Logger logger = LoggerFactory.getLogger(FormatterTask.class);

    private static final String FILE_S = " file(s)";

    /** The Constant CACHE_PROPERTIES_FILENAME. */
    private static final String CACHE_PROPERTIES_FILENAME = "formatter-maven-cache.properties";

    /** The Constant DEFAULT_INCLUDES. */
    private static final String[] DEFAULT_INCLUDES = new String[] { "**/*.java", "**/*.js", "**/*.html", "**/*.xml",
            "**/*.json", "**/*.css" };

    private List<FileSet> filesets = new ArrayList<>();

    /**
     * Project's target directory as specified in the POM.
     */
    private File targetDirectory;

    /**
     * Java compiler source version.
     */
    private String compilerSource = "1.6";

    /**
     * Java compiler compliance version.
     */
    private String compilerCompliance = "1.6";

    /**
     * Java compiler target version.
     */
    private String compilerTargetPlatform = "1.6";

    /**
     * The file encoding used to read and write source files. When not specified and sourceEncoding also not set,
     * default is platform file encoding.
     *
     * @since 0.3
     */
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
    private LineEnding lineEnding = LineEnding.AUTO;

    /**
     * File or classpath location of an Eclipse code formatter configuration xml file to use in formatting.
     */
    private String configFile;

    /**
     * File or classpath location of an Eclipse code formatter configuration xml file to use in formatting.
     */
    private String configJsFile;

    /**
     * File or classpath location of a properties file to use in html formatting.
     */
    private String configHtmlFile;

    /**
     * File or classpath location of a properties file to use in xml formatting.
     */
    private String configXmlFile;

    /**
     * File or classpath location of a properties file to use in json formatting.
     */
    private String configJsonFile;

    /**
     * File or classpath location of a properties file to use in css formatting.
     */
    private String configCssFile;

    /**
     * Whether the java formatting is skipped.
     */
    private Boolean skipJavaFormatting = false;

    /**
     * Whether the javascript formatting is skipped.
     */
    private Boolean skipJsFormatting = false;

    /**
     * Whether the html formatting is skipped.
     */
    private Boolean skipHtmlFormatting = false;

    /**
     * Whether the xml formatting is skipped.
     */
    private Boolean skipXmlFormatting = false;

    /**
     * Whether the json formatting is skipped.
     */
    private Boolean skipJsonFormatting = false;

    /**
     * Whether the css formatting is skipped.
     */

    private Boolean skipCssFormatting = false;

    private Boolean skipFormatting = false;

    /**
     * Use eclipse defaults when set to true for java and javascript.
     */
    private Boolean useEclipseDefaults = false;

    private JavaFormatter javaFormatter = new JavaFormatter();

    private JavascriptFormatter jsFormatter = new JavascriptFormatter();

    private HTMLFormatter htmlFormatter = new HTMLFormatter();

    private XMLFormatter xmlFormatter = new XMLFormatter();

    private JsonFormatter jsonFormatter = new JsonFormatter();

    private CssFormatter cssFormatter = new CssFormatter();

    /**
     * Execute.
     *
     */
    @Override
    public void execute() {
        if (this.skipFormatting) {
            logger.info("Formatting is skipped");
            return;
        }

        long startClock = System.currentTimeMillis();

        if (this.encoding == null || "".equals(this.encoding)) {
            this.encoding = System.getProperty("file.encoding");
            logger.warn("File encoding has not been set, using platform encoding (" + this.encoding
                    + ") to format source files, i.e. build is platform dependent!");
        } else {
            if (!Charset.isSupported(this.encoding)) {
                throw new RuntimeException("Encoding '" + this.encoding + "' is not supported");
            }
            logger.info("Using '" + this.encoding + "' encoding to format source files.");
        }

        createCodeFormatter();
        ResultCollector rc = new ResultCollector();
        Properties hashCache = readFileHashCacheFile();

        filesets.forEach(file -> this.formatFile(file, rc, hashCache, getBasedirPath()));

        storeFileHashCache(hashCache);

        long endClock = System.currentTimeMillis();

        logger.info("Successfully formatted:          " + rc.successCount + FILE_S);
        logger.info("Fail to format:                  " + rc.failCount + FILE_S);
        logger.info("Skipped:                         " + rc.skippedCount + FILE_S);
        logger.info("Read only skipped:               " + rc.readOnlyCount + FILE_S);
        logger.info("Approximate time taken:          " + ((endClock - startClock) / 1000) + "s");
    }

    /**
     * Gets the basedir path.
     *
     * @return the basedir path
     */
    private String getBasedirPath() {
        try {
            return this.getProject().getBaseDir().getCanonicalPath();
        } catch (IOException e) {
            logger.debug("", e);
            return "";
        }
    }

    /**
     * Store file hash cache.
     *
     * @param props
     *            the props
     */
    private void storeFileHashCache(Properties props) {
        File cacheFile = new File(this.targetDirectory, CACHE_PROPERTIES_FILENAME);
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(cacheFile))) {
            props.store(out, null);
        } catch (IOException e) {
            logger.warn("Cannot store file hash cache properties file", e);
        }
    }

    /**
     * Read file hash cache file.
     *
     * @return the properties
     */
    private Properties readFileHashCacheFile() {
        Properties props = new Properties();

        File targetDirectory = this.getTargetDirectory();

        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        } else if (!targetDirectory.isDirectory()) {
            logger.warn("Something strange here as the '" + targetDirectory.getPath()
                    + "' supposedly target directory is not a directory.");
            return props;
        }

        File cacheFile = new File(targetDirectory, CACHE_PROPERTIES_FILENAME);
        if (!cacheFile.exists()) {
            return props;
        }

        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(cacheFile))) {
            props.load(stream);
        } catch (IOException e) {
            logger.warn("Cannot load file hash cache properties file", e);
        }
        return props;
    }

    private void formatFile(FileSet fileset, ResultCollector rc, Properties hashCache, String basedirPath) {

        DirectoryScanner ds = fileset.getDirectoryScanner(this.getProject());
        File dir = ds.getBasedir();

        String[] filesInSet = ds.getIncludedFiles();

        for (String filename : filesInSet) {
            File file = new File(dir, filename);
            if (file.exists()) {
                if (file.canWrite()) {
                    formatFile(file, rc, hashCache, basedirPath);
                } else {
                    rc.readOnlyCount++;
                }
            } else {
                rc.failCount++;
            }
        }
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
     */
    private void formatFile(File file, ResultCollector rc, Properties hashCache, String basedirPath) {
        try {
            doFormatFile(file, rc, hashCache, basedirPath, false);
        } catch (IOException | MalformedTreeException e) {
            rc.failCount++;
            logger.warn("Could not format file", e);
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
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void doFormatFile(File file, ResultCollector rc, Properties hashCache, String basedirPath, boolean dryRun)
            throws IOException {
        logger.debug("Processing file: " + file);
        String code = readFileAsString(file);
        String originalHash = sha512hash(code);

        String canonicalPath = file.getCanonicalPath();
        String path = canonicalPath.substring(basedirPath.length());
        String cachedHash = hashCache.getProperty(path);
        if (cachedHash != null && cachedHash.equals(originalHash)) {
            rc.skippedCount++;
            logger.debug("File is already formatted.");
            return;
        }

        Result result;
        if (file.getName().endsWith(".java") && javaFormatter.isInitialized()) {
            if (skipJavaFormatting) {
                logger.info("Java formatting is skipped");
                result = Result.SKIPPED;
            } else {
                result = this.javaFormatter.formatFile(file, this.lineEnding, dryRun);
            }
        } else if (file.getName().endsWith(".js") && jsFormatter.isInitialized()) {
            if (skipJsFormatting) {
                logger.info("Javascript formatting is skipped");
                result = Result.SKIPPED;
            } else {
                result = this.jsFormatter.formatFile(file, this.lineEnding, dryRun);
            }
        } else if (file.getName().endsWith(".html") && htmlFormatter.isInitialized()) {
            if (skipHtmlFormatting) {
                logger.info("Html formatting is skipped");
                result = Result.SKIPPED;
            } else {
                result = this.htmlFormatter.formatFile(file, this.lineEnding, dryRun);
            }
        } else if (file.getName().endsWith(".xml") && xmlFormatter.isInitialized()) {
            if (skipXmlFormatting) {
                logger.info("Xml formatting is skipped");
                result = Result.SKIPPED;
            } else {
                result = this.xmlFormatter.formatFile(file, this.lineEnding, dryRun);
            }
        } else if (file.getName().endsWith(".json") && jsonFormatter.isInitialized()) {
            if (skipJsonFormatting) {
                logger.info("json formatting is skipped");
                result = Result.SKIPPED;
            } else {
                result = this.jsonFormatter.formatFile(file, this.lineEnding, dryRun);
            }
        } else if (file.getName().endsWith(".css") && cssFormatter.isInitialized()) {
            if (skipCssFormatting) {
                logger.info("css formatting is skipped");
                result = Result.SKIPPED;
            } else {
                result = this.cssFormatter.formatFile(file, this.lineEnding, dryRun);
            }
        } else {
            result = Result.SKIPPED;
        }

        switch (result) {
        case SKIPPED:
            rc.skippedCount++;
            return;
        case SUCCESS:
            rc.successCount++;
            break;
        case FAIL:
            rc.failCount++;
            return;
        default:
            break;
        }

        String formattedCode = readFileAsString(file);
        String formattedHash = sha512hash(formattedCode);
        hashCache.setProperty(path, formattedHash);

        if (originalHash.equals(formattedHash)) {
            rc.skippedCount++;
            logger.debug("Equal hash code. Not writing result to file.");
            return;
        }

        writeStringToFile(formattedCode, file);
    }

    /**
     * sha512hash.
     *
     * @param str
     *            the str
     * @return the string
     */
    private String sha512hash(String str) {
        return Hashing.sha512().hashBytes(str.getBytes(getEncoding())).toString();
    }

    /**
     * Read the given file and return the content as a string.
     *
     * @param file
     *            the file
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private String readFileAsString(File file) throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            char[] buf = new char[1024];
            int numRead;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
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
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void writeStringToFile(String str, File file) throws IOException {
        if (!file.exists() && file.isDirectory()) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(str);
        }
    }

    /**
     * Create a {@link CodeFormatter} instance to be used by this mojo.
     *
     */
    private void createCodeFormatter() {
        Map<String, String> javaFormattingOptions = resolveJavaFormattingOptions();
        Map<String, String> jsFormattingOptions = resolveJSFormattingOptions();
        Map<String, String> htmlFormattingOptions = resolveHtmlFormattingOptions();
        Map<String, String> xmlFormattingOptions = resolveXmlFormattingOptions();
        Map<String, String> cssFormattingOptions = resolveCSSFormattingOptions();

        if (configJsonFile != null) {
            Map<String, String> jsonFormattingOptions = getOptionsFromPropertiesFile(configJsonFile);
            jsonFormattingOptions.put("lineending", this.lineEnding.getChars());
            this.jsonFormatter.init(jsonFormattingOptions, this);
        }
        // stop the process if not config files where found
        if (javaFormattingOptions == null && jsFormattingOptions == null && htmlFormattingOptions == null
                && xmlFormattingOptions == null && cssFormattingOptions == null) {
            throw new RuntimeException(
                    "You must provide a Java, Javascript, HTML, XML, JSON, or CSS configuration file.");
        }
    }

    private Map<String, String> resolveCSSFormattingOptions() {
        return initializeFormatter(this.configCssFile, CssFormatter.DEFAULT_OPTION_FILE,
                this::getOptionsFromPropertiesFile, this.cssFormatter::init);
    }

    private Map<String, String> resolveXmlFormattingOptions() {
        return initializeFormatter(this.configXmlFile, XMLFormatter.DEFAULT_OPTION_FILE,
                this::getOptionsFromPropertiesFile, this.xmlFormatter::init);
    }

    private Map<String, String> resolveHtmlFormattingOptions() {
        return initializeFormatter(this.configHtmlFile, HTMLFormatter.DEFAULT_OPTION_FILE,
                this::getOptionsFromPropertiesFile, this.htmlFormatter::init);
    }

    private Map<String, String> resolveJSFormattingOptions() {
        return initializeFormatter(this.configJsFile, JavascriptFormatter.DEFAULT_OPTION_FILE,
                this::getOptionsFromPropertiesFile, this.jsFormatter::init);
    }

    private Map<String, String> resolveJavaFormattingOptions() {
        return initializeFormatter(this.configFile, JavaFormatter.DEFAULT_OPTION_FILE, this::getOptionsFromConfigFile,
                this.javaFormatter::init);
    }

    private Map<String, String> initializeFormatter(String configFile, String defaultOptionFile,
            Function<String, Map<String, String>> optionsReader,
            BiConsumer<Map<String, String>, ConfigurationSource> initializer) {
        Map<String, String> formattingOptions = optionsReader
                .apply(isEmpty(configFile) ? defaultOptionFile : configFile);

        if (formattingOptions != null) {
            initializer.accept(formattingOptions, this);
        }
        return formattingOptions;
    }

    private boolean isEmpty(String aString) {
        return aString == null || "".equals(aString);
    }

    /**
     * Read config file and return the config as {@link Map}.
     *
     * @return the options from config file or null if not config file found
     */
    private Map<String, String> getOptionsFromConfigFile(String newConfigFile) {

        try (InputStream configInput = loadResource(newConfigFile)) {
            return new ConfigReader().read(configInput);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Cannot read config file [" + newConfigFile + "]", e);
        } catch (ConfigReadException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Read properties file and return the properties as {@link Map}.
     *
     * @return the options from properties file or null if not properties file found
     */
    private Map<String, String> getOptionsFromPropertiesFile(String newPropertiesFile) {

        Properties properties = new Properties();

        try {
            properties.load(loadResource(newPropertiesFile));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config file [" + newPropertiesFile + "]", e);
        }

        final Map<String, String> map = new HashMap<>();
        for (final String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

    private InputStream loadResource(String resourceLocation) {
        try {
            return loadResourceFromProject(resourceLocation);
        } catch (IOException e) {
            return loadResourceFromClasspath(resourceLocation);
        }
    }

    private InputStream loadResourceFromClasspath(String resourceLocation) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceLocation);
    }

    private InputStream loadResourceFromProject(String resourceLocation) throws IOException {
        Resource resource = this.getProject().getResource(resourceLocation);
        return resource.getInputStream();
    }

    public void setFilesets(List<FileSet> filesets) {
        this.filesets = filesets;
    }

    public void addFileSet(FileSet fileset) {
        if (!filesets.contains(fileset)) {
            filesets.add(fileset);
        }
    }

    public void setCompilerSource(String compilerSource) {
        this.compilerSource = compilerSource;
    }

    public void setCompilerCompliance(String compilerCompliance) {
        this.compilerCompliance = compilerCompliance;
    }

    public void setCompilerTargetPlatform(String compilerTargetPlatform) {
        this.compilerTargetPlatform = compilerTargetPlatform;
    }

    public void setLineEnding(LineEnding lineEnding) {
        this.lineEnding = lineEnding;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public void setConfigJsFile(String configJsFile) {
        this.configJsFile = configJsFile;
    }

    public void setConfigHtmlFile(String configHtmlFile) {
        this.configHtmlFile = configHtmlFile;
    }

    public void setConfigXmlFile(String configXmlFile) {
        this.configXmlFile = configXmlFile;
    }

    public void setConfigJsonFile(String configJsonFile) {
        this.configJsonFile = configJsonFile;
    }

    public void setConfigCssFile(String configCssFile) {
        this.configCssFile = configCssFile;
    }

    public void setSkipJavaFormatting(Boolean skipJavaFormatting) {
        this.skipJavaFormatting = skipJavaFormatting;
    }

    public void setSkipJsFormatting(Boolean skipJsFormatting) {
        this.skipJsFormatting = skipJsFormatting;
    }

    public void setSkipHtmlFormatting(Boolean skipHtmlFormatting) {
        this.skipHtmlFormatting = skipHtmlFormatting;
    }

    public void setSkipXmlFormatting(Boolean skipXmlFormatting) {
        this.skipXmlFormatting = skipXmlFormatting;
    }

    public void setSkipJsonFormatting(Boolean skipJsonFormatting) {
        this.skipJsonFormatting = skipJsonFormatting;
    }

    public void setSkipCssFormatting(Boolean skipCssFormatting) {
        this.skipCssFormatting = skipCssFormatting;
    }

    public void setSkipFormatting(Boolean skipFormatting) {
        this.skipFormatting = skipFormatting;
    }

    public void setUseEclipseDefaults(Boolean useEclipseDefaults) {
        this.useEclipseDefaults = useEclipseDefaults;
    }

    class ResultCollector {

        int successCount;

        int failCount;

        int skippedCount;

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
    public File getTargetDirectory() {
        if (this.targetDirectory == null) {
            targetDirectory = this.getProject().getBaseDir();
        }
        return this.targetDirectory;
    }

    @Override
    public Charset getEncoding() {
        return Charset.forName(this.encoding);
    }

}
