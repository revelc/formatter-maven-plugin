/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;
import org.codehaus.plexus.util.IOUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MultipleJsonFormatterTest extends AbstractFormatterTest {

    private static String BEFORE_STRING;

    private static String AFTER_STRING;

    @BeforeAll
    public static void init() throws IOException {
        try (InputStream inputStream = NormalJsonFormatterTest.class
                .getResourceAsStream("/multiple-json/before.json")) {
            BEFORE_STRING = IOUtil.toString(Objects.requireNonNull(inputStream), "UTF-8");
        }
        try (InputStream inputStream = NormalJsonFormatterTest.class.getResourceAsStream("/multiple-json/after.json")) {
            AFTER_STRING = IOUtil.toString(Objects.requireNonNull(inputStream), "UTF-8");
        }
    }

    public static Stream<Arguments> testParamsProvider() {
        return Stream.of(Arguments.of(LineEnding.CRLF, "true"), Arguments.of(LineEnding.LF, "true"),
                Arguments.of(LineEnding.CR, "true"));
    }

    @ParameterizedTest
    @MethodSource("testParamsProvider")
    void test(LineEnding currentTestedLineEnding, String multipleJsonObjectFileAllowed) throws IOException {
        final JsonFormatter jsonFormatter = new JsonFormatter();
        Assertions.assertFalse(jsonFormatter.isInitialized());
        jsonFormatter.init(
                Map.of("lineending", currentTestedLineEnding.getChars(), "multipleJsonObjectFileAllowed",
                        multipleJsonObjectFileAllowed),
                new TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(jsonFormatter.isInitialized());
        String result = jsonFormatter.doFormat(BEFORE_STRING, currentTestedLineEnding);
        Assertions.assertEquals(AFTER_STRING.replaceAll(LineEnding.CRLF.getChars(), LineEnding.LF.getChars())
                .replaceAll(LineEnding.LF.getChars(), currentTestedLineEnding.getChars()), result);
    }

}
