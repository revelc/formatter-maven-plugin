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

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing the profile XML element in the Eclipse formatter config
 * file, including the kind attribute and Map of setting id and value.
 * 
 * @author Matt Blanchette
 */
public class Profile {

	private String kind;
	private Map settings = new HashMap();

	public Profile() {
	}

	public void addSetting(Setting setting) {
		settings.put(setting.getId(), setting.getValue());
	}

	public Map getSettings() {
		return settings;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
}
