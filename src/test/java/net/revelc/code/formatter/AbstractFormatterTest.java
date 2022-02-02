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

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

/**
 * The Class AbstractFormatterTest.
 */
public abstract class AbstractFormatterTest {

    /** The Constant RESOURCE_LOCATION_PRIMARY. */
    protected static final String RESOURCE_LOCATION_PRIMARY = "src/test/resources";

    /** The Constant RESOURCE_LOCATION_SECONDARY. */
    protected static final String RESOURCE_LOCATION_SECONDARY = "target/testoutput";

    /** The Constant TEST_OUTPUT_PRIMARY_DIR. */
    protected static final File TEST_OUTPUT_PRIMARY_DIR = new File("target/testoutput");

    /** The Constant TEST_OUTPUT_SECONDARY_DIR. */
    protected static final File TEST_OUTPUT_SECONDARY_DIR = new File("target/testoutputrepeat");

    /**
     * Creates the test dir.
     */
    @BeforeAll
    public static void createTestDir() {
        Assertions.assertTrue(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR.mkdirs()
                || AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR.isDirectory());
        Assertions.assertTrue(AbstractFormatterTest.TEST_OUTPUT_SECONDARY_DIR.mkdirs()
                || AbstractFormatterTest.TEST_OUTPUT_SECONDARY_DIR.isDirectory());
    }

    /**
     * The Class TestConfigurationSource.
     */
    public static class TestConfigurationSource implements ConfigurationSource {

        /** The target dir. */
        private final File targetDir;

        /**
         * Instantiates a new test configuration source.
         *
         * @param targetDir
         *            the target dir
         */
        public TestConfigurationSource(final File targetDir) {
            this.targetDir = targetDir;
        }

        @Override
        public File getTargetDirectory() {
            return this.targetDir;
        }

        @Override
        public Log getLog() {
            return new SystemStreamLog();
        }

        @Override
        public Charset getEncoding() {
            return StandardCharsets.UTF_8;
        }

        @Override
        public String getCompilerSources() {
            return "1.8";
        }

        @Override
        public String getCompilerCompliance() {
            return "1.8";
        }

        @Override
        public String getCompilerCodegenTargetPlatform() {
            return "1.8";
        }
    }

    /**
     * Single pass test.
     *
     * @param formatter
     *            the formatter
     * @param fileUnderTest
     *            the file under test
     * @param expectedSha512
     *            the expected sha 512
     * @param lineEnding
     *            the line ending
     */
    protected void singlePassTest(final Formatter formatter, final String fileUnderTest, final String expectedSha512,
            final LineEnding lineEnding) {
        this.multiPassTest(1, Collections.emptyMap(), formatter, fileUnderTest, expectedSha512, lineEnding);
    }

    /**
     * Two pass test.
     *
     * @param options
     *            the options
     * @param formatter
     *            the formatter
     * @param fileUnderTest
     *            the file under test
     * @param expectedSha512
     *            the expected sha 512
     * @param lineEnding
     *            the line ending
     */
    protected void twoPassTest(final Map<String, String> options, final Formatter formatter, final String fileUnderTest,
            final String expectedSha512, final LineEnding lineEnding) {
        this.multiPassTest(2, options, formatter, fileUnderTest, expectedSha512, lineEnding);
    }

    /**
     * Multi pass test.
     *
     * @param numPasses
     *            the num passes
     * @param options
     *            the options
     * @param formatter
     *            the formatter
     * @param fileUnderTest
     *            the file under test
     * @param expectedSha512
     *            the expected sha 512
     * @param lineEnding
     *            the line ending
     */
    private void multiPassTest(final int numPasses, final Map<String, String> options, final Formatter formatter,
            final String fileUnderTest, final String expectedSha512, final LineEnding lineEnding) {
        IntStream.rangeClosed(1, numPasses).forEachOrdered(passNumber -> {
            try {
                this.doTestFormat(options, formatter, fileUnderTest, expectedSha512, lineEnding, passNumber);
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    /**
     * Do test format.
     *
     * @param options
     *            the options
     * @param formatter
     *            the formatter
     * @param fileUnderTest
     *            the file under test
     * @param expectedSha512
     *            the expected sha 512
     * @param lineEnding
     *            the line ending
     * @param formatCycle
     *            the format cycle
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void doTestFormat(final Map<String, String> options, final Formatter formatter, final String fileUnderTest,
            final String expectedSha512, final LineEnding lineEnding, final int formatCycle) throws IOException {

        // Set the used resource location for test (either first pass or second pass)
        String resourceLocation;
        // Set the used output directory for test (either first pass or second pass)
        File testOutputDir;
        switch (formatCycle) {
        case 1:
            resourceLocation = AbstractFormatterTest.RESOURCE_LOCATION_PRIMARY;
            testOutputDir = AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR;
            break;
        case 2:
            resourceLocation = AbstractFormatterTest.RESOURCE_LOCATION_SECONDARY;
            testOutputDir = AbstractFormatterTest.TEST_OUTPUT_SECONDARY_DIR;
            break;
        default:
            throw new IllegalStateException("Unrecognized format cycle: " + formatCycle);
        }

        // Set original file and file to use for test
        final var originalSourceFile = new File(resourceLocation, fileUnderTest);
        final var sourceFile = new File(testOutputDir, fileUnderTest);

        // Copy file to new location
        Files.copy(originalSourceFile, sourceFile);

        // Read file to be formatted
        final var originalCode = FileUtils.fileRead(sourceFile, StandardCharsets.UTF_8.name());

        // Format the file and make sure formatting worked
        formatter.init(options, new TestConfigurationSource(testOutputDir));
        final var formattedCode = formatter.formatFile(sourceFile, originalCode, lineEnding);
        Assertions.assertNotNull(formattedCode);

        // Write the file we formatte4d
        FileUtils.fileWrite(sourceFile, StandardCharsets.UTF_8.name(), formattedCode);

        // Run assertions on formatted file, if not valid, reject and tester can look at resulting files
        // to debug issue.
        switch (formatCycle) {
        case 1:
            Assertions.assertNotEquals(originalCode, formattedCode);
            break;
        case 2:
            Assertions.assertEquals(originalCode, formattedCode);
            break;
        default:
            throw new IllegalStateException("Unrecognized format cycle: " + formatCycle);
        }

        // We are hashing this as set in stone in case for some reason our source file changes unexpectedly.
        final var sha512 = Files.asByteSource(sourceFile).hash(Hashing.sha512()).asBytes();
        final var sb = new StringBuilder();
        for (final byte element : sha512) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }

        Assertions.assertEquals(expectedSha512, sb.toString());
    }

}
