package com.relativitas.maven.plugins.formatter.xml;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author mblanchette
 */
public class Profiles {

	public static final String PROFILE_KIND = "CodeFormatterProfile";

	private List profiles = new ArrayList();

	public Profiles() {
	}

	public void addProfile(Profile profile) {
		if (PROFILE_KIND.equals(profile.getKind())) {
			profiles.add(profile.getSettings());
		}
	}

	public List getProfiles() {
		return profiles;
	}
}
