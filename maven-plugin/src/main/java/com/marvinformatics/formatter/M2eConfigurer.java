/**
 * Copyright (C) 2010 Marvin Herman Froeder (marvin@marvinformatics.com)
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
package com.marvinformatics.formatter;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.sonatype.plexus.build.incremental.BuildContext;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.marvinformatics.formatter.model.ConfigReader;
import com.marvinformatics.formatter.support.io.Resource;

public class M2eConfigurer {

	private final FormatterMojo config;
	private final BuildContext buildContext;

	public M2eConfigurer(FormatterMojo config, BuildContext buildContext) {
		this.config = config;
		this.buildContext = buildContext;
	}

	public void configure() {
		try {
			setupEclipsePrefs(Resource.forPath(config.javaConfig()), ".settings/org.eclipse.jdt.core.prefs");
		} catch (Exception e) {
			buildContext.addMessage(new File(config.basedir, "pom.xml"), 3, 4,
					"Unable to configure jdt (Java) formatter", BuildContext.SEVERITY_ERROR, e);
		}

		try {
			setupEclipsePrefs(Resource.forPath(config.jsConfig()), ".settings/org.eclipse.wst.jsdt.core.prefs");
		} catch (Exception e) {
			buildContext.addMessage(new File(config.basedir, "pom.xml"), 3, 4,
					"Unable to configure jsdt (Javascript) formatter", BuildContext.SEVERITY_ERROR, e);
		}
	}

	private void setupEclipsePrefs(Resource configFile, String prefsPath) throws Exception {
		if (configFile.exists()) {
			Properties jdtCorePrefs = new Properties();

			File prefsFile = new File(config.basedir, prefsPath);
			if (prefsFile.exists())
				try (Reader reader = Files.newReader(prefsFile, Charsets.UTF_8)) {
					jdtCorePrefs.load(reader);
				}

			try (InputStream configInput = configFile.asInputStream();) {
				ConfigReader configReader = new ConfigReader();
				configReader.read(configInput).forEach(jdtCorePrefs::setProperty);
			}

			try (OutputStream output = buildContext.newFileOutputStream(prefsFile);) {
				jdtCorePrefs.store(output, "This file was touched by formatter-maven-plugin at:");
			}
		}
	}

}
