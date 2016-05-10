/**
 * Copyright 2010-2016. All work is copyrighted to their respective
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
package net.revelc.code.formatter.connector.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.osgi.service.prefs.Preferences;

import net.revelc.code.formatter.connector.FormatterCore;

public class FormatterProjectConfigurator extends AbstractProjectConfigurator {

    private static final Logger LOGGER = Logger.getLogger(FormatterProjectConfigurator.class.getName());

    public enum Formatter {
        JAVA("configFile", "src/config/eclipse/formatter/java.xml");

        private final String configuratioName;
        private final String defaultPath;

        private Formatter(String newConfiguratioName, String newDefaultPath) {
            this.configuratioName = newConfiguratioName;
            this.defaultPath = newDefaultPath;
        }

        public String getConfigurationName() {
            return this.configuratioName;
        }

        public String getDefaultPath() {
            return this.defaultPath;
        }
    }

    @Override
    public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade, MojoExecution execution,
            IPluginExecutionMetadata executionMetadata) {
        // nothing to do
        return null;
    }

    @Override
    public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {
        IProject eclipseProject = request.getProject();

        printSettings();
        if (eclipseProject.hasNature(JavaCore.NATURE_ID)) {
            Xpp3Dom[] settings = parseConfigurationFile(request, monitor);

            for (Xpp3Dom setting : settings) {
                Platform.getPreferencesService().getRootNode().node("project").node(eclipseProject.getName())
                        .node(JavaCore.PLUGIN_ID).put(setting.getAttribute("id"), setting.getAttribute("value"));
            }

            Platform.getPreferencesService().getRootNode().node("project").node(eclipseProject.getName())
                    .node("org.eclipse.jdt.ui").put("cleanup.format_source_code", "true");
        }

        // jsdtConfigFile = cfg.getChild("configJsFile").getValue();
        // src/config/eclipse/formatter/javascript.xml
    }

    private Xpp3Dom[] parseConfigurationFile(ProjectConfigurationRequest request, IProgressMonitor monitor)
            throws CoreException {
        Xpp3Dom dom;
        try (InputStream content = readConfigFile(Formatter.JAVA, request, monitor)) {
            dom = Xpp3DomBuilder.build(content, "UTF-8");
        } catch (XmlPullParserException e) {
            throw new CoreException(new Status(IStatus.ERROR, FormatterCore.PLUGIN_ID, "Invalid configuration XML", e));
        } catch (IOException e) {
            throw new CoreException(
                    new Status(IStatus.ERROR, FormatterCore.PLUGIN_ID, "Unable to read configuration XML", e));
        }
        return dom.getChild("profile").getChildren("setting");
    }

    private InputStream readConfigFile(Formatter formatter, ProjectConfigurationRequest request,
            IProgressMonitor monitor) throws CoreException {
        IMavenProjectFacade mavenProject = request.getMavenProjectFacade();
        List<MojoExecution> executions = mavenProject.getMojoExecutions("net.revelc.code.formatter",
                "formatter-maven-plugin", monitor, "validate");

        MojoExecution execution = executions.get(0);
        Xpp3Dom cfg = execution.getConfiguration();
        String javaConfigFile = cfg.getChild(formatter.getConfigurationName()).getValue();
        if (javaConfigFile == null || javaConfigFile.equalsIgnoreCase("${" + formatter.getConfigurationName() + "}")) {
            javaConfigFile = formatter.getDefaultPath();
        }

        IFile cfgFile = request.getProject().getFile(javaConfigFile);

        if (!cfgFile.exists()) {
            throw new CoreException(
                    new Status(IStatus.CANCEL, FormatterCore.PLUGIN_ID, "Configuration file not found!"));
        }

        return cfgFile.getContents();
    }

    private void printSettings() {
        StringBuilder sb = new StringBuilder();
        Preferences prefs = Platform.getPreferencesService().getRootNode();
        try {
            eval(prefs, "\t", sb);
        } catch (Exception e1) {
            LOGGER.info("Exception in eval " + e1.getMessage());
        }

        File f = new File("tree.txt");
        try (final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8"))) {
            osw.write(sb.toString().toCharArray());
            f.getAbsolutePath();
        } catch (IOException e1) {
            LOGGER.info("Exception in writing in tree.txt " + e1.getMessage());
        }
    }

    private static void eval(Preferences prefs, String spacer, StringBuilder sb) throws Exception {
        String[] children = prefs.childrenNames();
        for (String child : children) {
            sb.append(spacer).append(child).append("\n");
            eval(prefs.node(child), spacer + "\t", sb);
        }
        String[] keys = prefs.keys();
        for (String key : keys) {
            sb.append(spacer).append(" * ").append(key).append(": ").append(prefs.get(key, null)).append("\n");
        }
    }

    @Override
    public void mavenProjectChanged(MavenProjectChangedEvent event, IProgressMonitor monitor) throws CoreException {
        // Not Implemented
    }
}
