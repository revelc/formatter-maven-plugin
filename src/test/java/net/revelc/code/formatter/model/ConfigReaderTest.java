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
package net.revelc.code.formatter.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Test class for {@link ConfigReader}.
 */
class ConfigReaderTest {

    /**
     * Test successfully read a config file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void test_success_read_config() throws Exception {
        final var cl = Thread.currentThread().getContextClassLoader();
        try (var in = cl.getResourceAsStream("sample-config.xml")) {
            final var configReader = new ConfigReader();
            final var config = configReader.read(in);
            Assertions.assertNotNull(config);
            Assertions.assertEquals(264, config.size());
            // test get one of the entry in the file
            Assertions.assertEquals("true", config.get("org.eclipse.jdt.core.formatter.comment.format_html"));
        }
    }

    /**
     * Test reading an invalid config file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void test_read_invalid_config() throws Exception {
        final var cl = Thread.currentThread().getContextClassLoader();
        try (var in = cl.getResourceAsStream("sample-invalid-config.xml")) {
            final var configReader = new ConfigReader();
            Assertions.assertThrows(SAXException.class, () -> {
                configReader.read(in);
            });
        }
    }

    /**
     * Test reading an invalid config file.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void test_read_invalid_config2() throws Exception {
        final var cl = Thread.currentThread().getContextClassLoader();
        try (final var in = cl.getResourceAsStream("sample-invalid-config2.xml")) {
            final var configReader = new ConfigReader();
            Assertions.assertThrows(ConfigReadException.class, () -> {
                configReader.read(in);
            });
        }
    }

}
