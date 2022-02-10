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
import java.util.Collections;
import java.util.Objects;

import net.revelc.code.formatter.AbstractFormatterTest;
import net.revelc.code.formatter.LineEnding;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NormalJsonFormatterTest extends AbstractFormatterTest {

    @Test
    void test() throws IOException {
        final var jsonFormatter = new JsonFormatter();
        Assertions.assertFalse(jsonFormatter.isInitialized());
        jsonFormatter.init(Collections.emptyMap(),
                new TestConfigurationSource(AbstractFormatterTest.TEST_OUTPUT_PRIMARY_DIR));
        Assertions.assertTrue(jsonFormatter.isInitialized());

        String beforeString = null;
        try (InputStream inputStream = this.getClass().getResourceAsStream("/normal-json/before.json")) {
            beforeString = IOUtil.toString(Objects.requireNonNull(inputStream), "UTF-8");
        }

        String afterString = null;
        try (InputStream inputStream = this.getClass().getResourceAsStream("/normal-json/after.json")) {
            afterString = IOUtil.toString(Objects.requireNonNull(inputStream), "UTF-8");
        }

        {
            String result = jsonFormatter.doFormat(beforeString, LineEnding.CRLF);
            Assertions.assertEquals(
                    afterString.replaceAll(LineEnding.LF.getChars(), LineEnding.CRLF.getChars()),
                    result
            );
        }

        {
            String result = jsonFormatter.doFormat(beforeString, LineEnding.LF);
            Assertions.assertEquals(
                    afterString.replaceAll(LineEnding.CRLF.getChars(), LineEnding.LF.getChars()),
                    result
            );
        }

    }

}
