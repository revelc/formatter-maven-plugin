package com.relativitas.maven.plugins.formatter;

/*
 * Copyright 2010. All work is copyrighted to their respective author(s),
 * unless otherwise stated.
 *
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.relativitas.maven.plugins.formatter.xml.Profiles;

/**
 * This class reads a config file for Eclipse code formatter.
 * 
 * @author jecki
 * @author Matt Blanchette
 */
public class ConfigReader {

	/**
	 * Read from the <code>input</code> and return it's configuration settings
	 * as a {@link Map}.
	 * 
	 * @param input
	 * @return return {@link Map} with all the configurations read from the
	 *         config file, or throws an exception if there's a problem reading
	 *         the input, e.g.: invalid XML.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ConfigReadException
	 */
	public Map<String, String> read(InputStream input) throws IOException,
			SAXException, ConfigReadException {
		Digester digester = new Digester();
		digester.addRuleSet(new RuleSet());

		Object result = digester.parse(input);
		if (result == null && !(result instanceof Profiles)) {
			throw new ConfigReadException("No profiles found in config file");
		}

		Profiles profiles = (Profiles) result;
		List<Map<String, String>> list = profiles.getProfiles();
		if (list.size() == 0) {
			throw new ConfigReadException("No profile in config file of kind: "
					+ Profiles.PROFILE_KIND);
		}

		return (Map<String, String>) list.get(0);
	}

}
