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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.resource.ResourceManager;
import org.codehaus.plexus.resource.loader.FileResourceLoader;
import org.codehaus.plexus.resource.loader.ResourceNotFoundException;
import org.xml.sax.SAXException;

import net.revelc.code.formatter.model.ConfigReadException;
import net.revelc.code.formatter.model.ConfigReader;

class CfgFileSearcher {

    /**
     * The maven {@link Log}.
     */
    private final Log log;

    /**
     * Project's base directory as specified in the POM.
     */
    private final File basedir;

    /**
     * ResourceManager for retrieving the configFile resource.
     */
    private final ResourceManager resourceManager;

    /**
     * Instantiate a new cfg file searcher.
     *
     * @param log
     *            maven logger
     * @param basedir
     *            the project's basedir as specified in the POM
     * @param resourceManager
     *            the resourceManager
     */
    public CfgFileSearcher(Log log, File basedir, ResourceManager resourceManager) {
        this.log = log;
        this.basedir = basedir;
        this.resourceManager = resourceManager;
    }

    /**
     * Read config file and return the config as {@link Map}.
     *
     * @param newConfigFile
     *            the new config file
     *
     * @return the options from config file
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    Map<String, String> getOptionsFromConfigFile(final String newConfigFile) throws MojoExecutionException {
        log.debug("Start search config file: " + newConfigFile);
        var searchBase = basedir;
        var possibleCfgSearchBase = findUp(log, basedir.getAbsolutePath(), newConfigFile);
        if (possibleCfgSearchBase != null) {
            searchBase = possibleCfgSearchBase.toFile();
        } else {
            log.debug("FindUp search strategy did not find any file, fallback to default basedir.");
        }

        this.log.debug("Using search path at: " + searchBase.getAbsolutePath());
        this.resourceManager.addSearchPath(FileResourceLoader.ID, searchBase.getAbsolutePath());

        try (var configInput = this.resourceManager.getResourceAsInputStream(newConfigFile)) {
            return new ConfigReader().read(configInput);
        } catch (final ResourceNotFoundException e) {
            throw new MojoExecutionException("Cannot find config file [" + newConfigFile + "]");
        } catch (final IOException e) {
            throw new MojoExecutionException("Cannot read config file [" + newConfigFile + "]", e);
        } catch (final SAXException e) {
            throw new MojoExecutionException("Cannot parse config file [" + newConfigFile + "]", e);
        } catch (final ConfigReadException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * Read properties file and return the properties as {@link Map}.
     *
     * @param newPropertiesFile
     *            the new properties file
     *
     * @return the options from properties file or null if not properties file found
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     */
    Map<String, String> getOptionsFromPropertiesFile(final String newPropertiesFile) throws MojoExecutionException {
        log.debug("Start search config file: " + newPropertiesFile);
        var searchBase = basedir;
        var possibleCfgSearchBase = findUp(this.log, basedir.getAbsolutePath(), newPropertiesFile);
        if (possibleCfgSearchBase != null) {
            searchBase = possibleCfgSearchBase.toFile();
        } else {
            log.debug("FdindUp search strategy did not find any file, fallback to default basedir.");
        }

        this.log.debug("Using search path at: " + searchBase);
        this.resourceManager.addSearchPath(FileResourceLoader.ID, searchBase.getAbsolutePath());

        final var properties = new Properties();
        try {
            properties.load(this.resourceManager.getResourceAsInputStream(newPropertiesFile));
        } catch (final ResourceNotFoundException | IOException e) {
            throw new MojoExecutionException("Cannot find config file [" + newPropertiesFile + "]", e);
        }

        final Map<String, String> map = new HashMap<>();
        for (final String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

    /**
     * Find target cfg file by walking up parent directories, stop when pom.xml is not found.
     *
     * @param log
     *            maven logger
     * @param searchBase
     *            directory where the walking-up starts
     * @param cfgName
     *            name of the target cfg file to search
     *
     * @return the {@link Path} where cfg file with the name of cfgName was found or null if no cfg file found
     */
    private Path findUp(Log log, String searchBase, String cfgName) {
        var searchBasePath = Paths.get(searchBase);
        if (!Files.exists(searchBasePath.resolve("pom.xml"))) {
            log.debug("FindUp: config file not found");
            return null;
        }
        if (Files.exists(searchBasePath.resolve(cfgName).toAbsolutePath())) {
            log.debug("FindUp: config file found: " + searchBase);
            return searchBasePath;
        }
        log.debug("FindUp: search parent: " + searchBase);
        return findUp(log, searchBasePath.getParent().toAbsolutePath().toString(), cfgName);
    }
}
