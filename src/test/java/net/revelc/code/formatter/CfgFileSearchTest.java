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

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.resource.DefaultResourceManager;
import org.codehaus.plexus.resource.ResourceManager;
import org.codehaus.plexus.resource.loader.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class CfgFileSearchTest.
 */
class CfgFileSearchTest {

    /**
     * Test search config file.
     */
    @Test
    void testSearch() throws URISyntaxException {
        var fakeProjectRoot = Paths
                .get(ClassLoader.getSystemResource("fake-nested-mvn-project/sub-module/sub-sub-module").toURI());
        var cfgFileSearcher = new CfgFileSearcher(new SystemStreamLog(), fakeProjectRoot.toFile(),
                getResourceManager());

        // search absolute path
        Assertions.assertDoesNotThrow(() -> {
            // ${project.basedir}/../html.properties
            var magic = cfgFileSearcher
                    .getOptionsFromPropertiesFile(fakeProjectRoot.getParent().resolve("html.properties").toString())
                    .get("magic");
            Assertions.assertEquals("for-findup-test", magic);
        });

        // search default location(class path)
        Assertions.assertDoesNotThrow(() -> {
            var magic = cfgFileSearcher.getOptionsFromPropertiesFile("formatter-maven-plugin/jsoup/html.properties")
                    .get("magic");
            Assertions.assertEquals(null, magic);
        });

        // upward recursive search: cfg file found
        Assertions.assertDoesNotThrow(() -> {
            var magic = cfgFileSearcher.getOptionsFromPropertiesFile("html.properties").get("magic");
            Assertions.assertEquals("for-findup-test", magic);
        });

        // upward recursive search: cfg file not found
        var exception = Assertions.assertThrows(MojoExecutionException.class, () -> {
            cfgFileSearcher.getOptionsFromPropertiesFile("html.properties.non-exist");
        });
        var actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains("Cannot find config file"));
    }

    private ResourceManager getResourceManager() {
        var map = new HashMap<String, ResourceLoader>();
        map.put(FileResourceLoader.ID, new FileResourceLoader());
        map.put(URLResourceLoader.ID, new URLResourceLoader());
        map.put(JarResourceLoader.ID, new JarResourceLoader());
        map.put(ThreadContextClasspathResourceLoader.ID, new ThreadContextClasspathResourceLoader());
        return new DefaultResourceManager(map);
    }
}
