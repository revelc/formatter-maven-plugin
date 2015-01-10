/**
 * Copyright 2010-2014. All work is copyrighted to their respective
 * author(s), unless otherwise stated.
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
package com.relativitas.maven.plugins.formatter.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class representing the profiles XML element in the Eclipse formatter config
 * file, including a List of profile setting Maps with id and value.
 * 
 * @author Matt Blanchette
 */
public class Profiles {

	/** The Constant PROFILE_KIND. */
	public static final String PROFILE_KIND = "CodeFormatterProfile";

	/** The profiles. */
	private List<Map<String, String>> profiles = new ArrayList<Map<String, String>>();

	/**
	 * Adds the profile.
	 *
	 * @param profile the profile
	 */
	public void addProfile(Profile profile) {
		if (PROFILE_KIND.equals(profile.getKind())) {
			profiles.add(profile.getSettings());
		}
	}

	/**
	 * Gets the profiles.
	 *
	 * @return the profiles
	 */
	public List<Map<String, String>> getProfiles() {
		return profiles;
	}
}
