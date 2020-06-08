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

import static net.revelc.code.formatter.AbstractFormatterTest.TEST_OUTPUT_DIR;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jface.text.BadLocationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

import net.revelc.code.formatter.java.JavaFormatter;

class FormatterMojoTest {

    static final String PREVIOUS_HASH = "_PREVIOUS_HASH_";
    static final File ORIGINAL = new File("src/test/resources/", "AnyClass.java");
    static final AtomicInteger FILE_ID = new AtomicInteger(0);

    final FormatterMojo mojo = new FormatterMojo();
    final FormatterMojo.ResultCollector rc = new FormatterMojo.ResultCollector();
    final Properties cache = new Properties();

    File sourceFile;
    String basedir;
    String cacheKey;

    @BeforeEach
    public void setup() throws IOException {
        basedir = TEST_OUTPUT_DIR.getCanonicalPath();
        sourceFile = new File(TEST_OUTPUT_DIR, "AnyClass" + FILE_ID.incrementAndGet() + ".java");
        Files.createParentDirs(sourceFile);
        Files.copy(ORIGINAL, sourceFile);
        cacheKey = sourceFile.getCanonicalPath().substring(basedir.length());

        mojo.useEclipseDefaults = true;
        mojo.encoding = "UTF-8";
        mojo.skipJavaFormatting = false;
        mojo.lineEnding = LineEnding.AUTO;
        mojo.javaFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
    }

    /**
     * Format a known file that changed. The new hash should be recorded.
     */
    @Test
    public void format_known_dirty_java_file() throws Exception {
        String filePath = sourceFile.getAbsolutePath();
        cache.put(cacheKey, PREVIOUS_HASH);

        mojo.doFormatFile(sourceFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        String expectedHash = mojo.sha512hash(FileUtils.fileRead(sourceFile));
        assertEquals(cachedHash, expectedHash);
    }

    /**
     * Skip a known file that did not change. The existing hash should remain the same.
     */
    @Test
    public void skip_known_unchanged_java_file() throws Exception {
        formatSourceFile();
        String filePath = sourceFile.getAbsolutePath();
        String unchangedHash = mojo.sha512hash(FileUtils.fileRead(filePath));
        cache.put(cacheKey, unchangedHash);

        mojo.doFormatFile(sourceFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        assertEquals(rc.skippedCount, 1);
        assertEquals(cachedHash, unchangedHash);
    }

    /**
     * Format a new file that required formatting. The new hash should be added to the cache.
     */
    @Test
    public void format_new_dirty_java_file() throws Exception {
        mojo.doFormatFile(sourceFile, rc, cache, basedir, false);
        String cachedHash = (String) cache.get(cacheKey);
        String expectedHash = mojo.sha512hash(FileUtils.fileRead(sourceFile));
        assertEquals(cachedHash, expectedHash);
    }

    /**
     * Format a new file that was already formatted. The content is unchanged, but the file's hash should still be added
     * to the cache. FIXME this test is currently failing, showing how new files are left out of the cache if already
     * formatted see https://github.com/revelc/formatter-maven-plugin/issues/312
     */
    @Test
    public void format_new_clean_java_file() throws Exception {
        formatSourceFile();
        String filePath = sourceFile.getCanonicalPath();

        mojo.doFormatFile(sourceFile, rc, cache, basedir, false);

        String cachedHash = (String) cache.get(cacheKey);
        String expectedHash = mojo.sha512hash(FileUtils.fileRead(sourceFile));
        assertEquals(cachedHash, expectedHash);
    }

    private void formatSourceFile() throws IOException, BadLocationException {
        JavaFormatter formatter = new JavaFormatter();
        formatter.init(Collections.emptyMap(), new AbstractFormatterTest.TestConfigurationSource(TEST_OUTPUT_DIR));
        File fileToFormat = sourceFile;
        String dirtyCode = FileUtils.fileRead(fileToFormat);
        String formattedCode = formatter.doFormat(dirtyCode, LineEnding.AUTO);
        FileUtils.fileWrite(fileToFormat, formattedCode);
    }

}
