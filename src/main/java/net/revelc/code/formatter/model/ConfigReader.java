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
package net.revelc.code.formatter.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

/**
 * This class reads a config file for Eclipse code formatter.
 */
public class ConfigReader {

    /**
     * Read from the <code>input</code> and return it's configuration settings as a {@link Map}.
     *
     * @param input
     *            the input
     *
     * @return return {@link Map} with all the configurations read from the config file, or throws an exception if
     *         there's a problem reading the input, e.g.: invalid XML.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SAXException
     *             the SAX exception
     * @throws ConfigReadException
     *             the config read exception
     */
    public Map<String, String> read(final InputStream input) throws IOException, SAXException, ConfigReadException {
        final var digester = new Digester();
        digester.addRuleSet(new RuleSet());

        final var result = digester.parse(input);
        if (!(result instanceof Profiles)) {
            throw new ConfigReadException("No profiles found in config file");
        }

        final var profiles = (Profiles) result;
        final var list = profiles.getProfiles();
        if (list.isEmpty()) {
            throw new ConfigReadException("No profile in config file of kind: " + Profiles.PROFILE_KIND);
        }

        return list.get(0);
    }

}
