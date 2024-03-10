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

package net.revelc.code.formatter.json;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.codehaus.plexus.util.IOUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JsonFormatterTest.
 */
class JsonFormatterTest extends AbstractFormatterTest {

    /**
     * Test do format file.
     *
     */
    @Test
    void testDoFormatFile() {
        // Since we set the line endings via options for json, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        final var expectedHash = LineEnding.LF.isSystem()
                ? "1c8b8931b79a7dfaa4d2ab1986ebfe5967716b63877aa0311091214bf870f5480469a80e920fc825a98ad265f252e94e1ca4b94a55a279d0d2d302a20dcb4fa3"
                : "c6e19e9d042d8d2045eb17d2966f105e6c538d5c05c614c556eb88dfb020645cb2d410cf059643a14ca193487b888e24194499ee8be2c337afdc89067a23e4cd";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(Collections.emptyMap(), new JsonFormatter(), "someFile.json", expectedHash, lineEnding);
    }

    /**
     * Test is initialized.
     */
    @Test
    void testIsInitialized() {
        final var jsonFormatter = new JsonFormatter();
        Assertions.assertFalse(jsonFormatter.isInitialized());
        jsonFormatter.init(Collections.emptyMap(),
                new AbstractFormatterTest.TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(jsonFormatter.isInitialized());
    }

    /**
     * Test do format file with config.
     *
     */
    @Test
    void testDoFormatFileWithConfig() {
        final Map<String, String> jsonFormattingOptions = new HashMap<>();
        jsonFormattingOptions.put("indent", "2");
        jsonFormattingOptions.put("spaceBeforeSeparator", "false");
        jsonFormattingOptions.put("alphabeticalOrder", "true");

        // Since we set the line endings via options for json, we cannot rely on CRLF inside twoPassTest.
        // The option will not be available inside json formatter init so it will use whatever the system
        // default is regardless of requesting it to be CRLF later which is ignored.
        final var expectedHash = LineEnding.LF.isSystem()
                ? "3886d1b96fa1b887e48ec7317c7e7aa034b30b902e6697a646401e8c04d9301e08621dd8a4c7830f8a3a51977592574020e489a5de0d8f0b799f5c412cd57bd9"
                : "0e37513ff8430246ff75edb33b61b3427546a84ef4eb83eaf2214249572b1f91305a2d4b3bd748354d74f9baf638d8ac8c3d7ee1cac2fb3aee55c19a040aea23";
        final var lineEnding = LineEnding.LF.isSystem() ? LineEnding.LF : LineEnding.CRLF;
        this.twoPassTest(jsonFormattingOptions, new JsonFormatter(), "someFile.json", expectedHash, lineEnding);
    }

    @Test
    public void testMultipleJson() throws IOException {
        testFormattingObjects("/multiple-json");
    }

    @Test
    public void testNormalJson() throws IOException {
        testFormattingObjects("/normal-json");
    }

    private void testFormattingObjects(String testpath) throws IOException {
        String originalJson;
        String expectedFormattedJson;
        try (var in = getClass().getResourceAsStream(testpath + "/before.json")) {
            originalJson = IOUtil.toString(Objects.requireNonNull(in), "UTF-8");
        }
        try (var in = getClass().getResourceAsStream(testpath + "/after.json")) {
            expectedFormattedJson = IOUtil.toString(Objects.requireNonNull(in), "UTF-8");
        }
        for (LineEnding currentTestedLineEnding : EnumSet.of(LineEnding.CRLF, LineEnding.LF, LineEnding.CR)) {
            final var jsonFormatter = new JsonFormatter();
            Assertions.assertFalse(jsonFormatter.isInitialized());
            jsonFormatter.init(Map.of("lineending", currentTestedLineEnding.getChars()),
                    new TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
            Assertions.assertTrue(jsonFormatter.isInitialized());
            String result = jsonFormatter.doFormat(originalJson, currentTestedLineEnding);
            Assertions
                    .assertEquals(expectedFormattedJson.replaceAll(LineEnding.CRLF.getChars(), LineEnding.LF.getChars())
                            .replaceAll(LineEnding.LF.getChars(), currentTestedLineEnding.getChars()), result);
        }
    }

}
