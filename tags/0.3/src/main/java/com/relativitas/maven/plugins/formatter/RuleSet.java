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

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

import com.relativitas.maven.plugins.formatter.xml.Profile;
import com.relativitas.maven.plugins.formatter.xml.Profiles;
import com.relativitas.maven.plugins.formatter.xml.Setting;

/**
 * An Apache Commons Digester RuleSet for configuring a digester to parse the
 * Eclipse formatter config XML into objects.
 * 
 * @author jecki
 * @author Matt Blanchette
 */
class RuleSet extends RuleSetBase {

	/**
	 * @see org.apache.commons.digester.RuleSetBase#addRuleInstances(org.apache.commons.digester.Digester)
	 */
	public void addRuleInstances(Digester digester) {
		digester.addObjectCreate("profiles", Profiles.class);
		digester.addObjectCreate("profiles/profile", Profile.class);
		digester.addObjectCreate("profiles/profile/setting", Setting.class);

		digester.addSetNext("profiles/profile", "addProfile");
		digester.addSetNext("profiles/profile/setting", "addSetting");

		digester.addSetProperties("profiles/profile", "kind", "kind");
		digester.addSetProperties("profiles/profile/setting", "id", "id");
		digester.addSetProperties("profiles/profile/setting", "value", "value");
	}

}
