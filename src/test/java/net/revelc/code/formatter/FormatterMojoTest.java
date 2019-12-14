package net.revelc.code.formatter;

import com.google.common.io.Files;
import net.revelc.code.formatter.java.JavaFormatter;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jface.text.BadLocationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static net.revelc.code.formatter.AbstractFormatterTest.TEST_OUTPUT_DIR;
import static org.junit.jupiter.api.Assertions.*;

class FormatterMojoTest {

    private static final String PREVIOUS_HASH = "_PREVIOUS_HASH_";

    final FormatterMojo mojo = new FormatterMojo();
    final FormatterMojo.ResultCollector rc = new FormatterMojo.ResultCollector();
    final Properties cache = new Properties();
    final File original = new File("src/test/resources/", "AnyClass.java");
    final File sourceFile = new File(TEST_OUTPUT_DIR, "AnyClass.java");
    String basedir;
    String cacheKey;

    @BeforeEach
    public void setup() throws IOException {
        basedir = TEST_OUTPUT_DIR.getCanonicalPath();
        cacheKey = sourceFile.getCanonicalPath().substring(basedir.length());
        mojo.useEclipseDefaults = true;
        mojo.encoding = "UTF-8";
        mojo.skipJavaFormatting = false;
        mojo.lineEnding = LineEnding.AUTO;
        mojo.javaFormatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
    }

    /**
     * Format a known file that changed.
     * The new hash should be recorded.
     */
    @Test
    public void format_known_dirty_java_file() throws Exception {
        File testFile = dirtyFile();
        String filePath = testFile.getAbsolutePath();
        cache.put(cacheKey, PREVIOUS_HASH);

        mojo.doFormatFile(testFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        String expectedHash = mojo.sha512hash(FileUtils.fileRead(testFile));
        assertEquals(cachedHash, expectedHash);
    }

    /**
     * Skip a known file that did not change.
     * The existing hash should remain the same.
     */
    @Test
    public void skip_known_unchanged_java_file() throws Exception {
        File testFile = cleanFile();
        String filePath = testFile.getAbsolutePath();
        String unchangedHash = mojo.sha512hash(FileUtils.fileRead(filePath));
        cache.put(cacheKey, unchangedHash);

        mojo.doFormatFile(testFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        assertEquals(rc.skippedCount, 1);
        assertEquals(cachedHash, unchangedHash);
    }

    /**
     * Format a new file that required formatting.
     * The new hash should be added to the cache.
     */
    @Test
    public void format_new_dirty_java_file() throws Exception {
        File testFile = dirtyFile();

        mojo.doFormatFile(testFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        String expectedHash = mojo.sha512hash(FileUtils.fileRead(testFile));
        assertEquals(cachedHash, expectedHash);
    }

    /**
     * Format a new file that was already formatted.
     * The content is unchanged, but the file's hash should still be added to the cache.
     */
    @Test
    public void format_new_clean_java_file() throws Exception {
        File testFile = cleanFile();
        String filePath = testFile.getCanonicalPath();

        mojo.doFormatFile(testFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        String expectedHash = mojo.sha512hash(FileUtils.fileRead(testFile));
        assertEquals(cachedHash, expectedHash);
    }

    private File dirtyFile() throws IOException {
        Files.copy(original, sourceFile);
        return sourceFile;
    }

    private File cleanFile() throws IOException, BadLocationException {
        JavaFormatter formatter = new JavaFormatter();
        formatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
        File fileToFormat = dirtyFile();
        String dirtyCode = FileUtils.fileRead(fileToFormat);
        String formattedCode = formatter.doFormat(dirtyCode, LineEnding.AUTO);
        FileUtils.fileWrite(fileToFormat, formattedCode);
        return fileToFormat;
    }

}