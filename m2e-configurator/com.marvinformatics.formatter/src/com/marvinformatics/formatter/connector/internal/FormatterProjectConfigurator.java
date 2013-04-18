package com.marvinformatics.formatter.connector.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;

public class FormatterProjectConfigurator extends AbstractProjectConfigurator {

	@Override
	public AbstractBuildParticipant getBuildParticipant(
			IMavenProjectFacade projectFacade, MojoExecution execution,
			IPluginExecutionMetadata executionMetadata) {
		// nothing to do
		return null;
	}

	@Override
	public void configure(ProjectConfigurationRequest request,
			IProgressMonitor monitor) throws CoreException {
		IProject eclipseProject = request.getProject();

		if (eclipseProject.hasNature(JavaCore.NATURE_ID)) {
			IJavaProject javaProject = JavaCore.create(eclipseProject);

			IMavenProjectFacade mavenProject = request.getMavenProjectFacade();
			List<MojoExecution> executions = mavenProject.getMojoExecutions(
					"com.marvinformatics.formatter", "formatter-maven-plugin",
					monitor, "validate");

			MojoExecution execution = executions.get(0);
			Xpp3Dom cfg = execution.getConfiguration();
			String javaConfigFile = cfg.getChild("configFile").getValue();
			if (javaConfigFile == null
					|| "${configfile}".equals(javaConfigFile))
				javaConfigFile = "src/config/eclipse/formatter/java.xml";

			IFile cfgFile = eclipseProject.getFile(javaConfigFile);

			if (!cfgFile.exists())
				return;

			InputStream content = cfgFile.getContents();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					content));
			String line = null;
			Map<String, String> opts = javaProject.getOptions(false);
			try {
				while ((line = reader.readLine()) != null) {
					// <setting
					// id="org.eclipse.jdt.core.formatter.comment.insert_new_line_before_root_tags"
					// value="insert" />
					if (!line.contains("<setting "))
						continue;

					int first = line.indexOf("id=\"") + 4;
					String id = line.substring(first, line.indexOf('"', first));
					first = line.indexOf("value=\"") + 7;
					String value = line.substring(first,
							line.indexOf('"', first));
					opts.put(id, value);
				}
			} catch (IOException e) {
			}

			javaProject.setOptions(opts);
		}
		// jsdtConfigFile = cfg.getChild("configJsFile").getValue();
		// src/config/eclipse/formatter/javascript.xml
	}

	private Map<? extends String, ? extends String> readFromCfg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mavenProjectChanged(MavenProjectChangedEvent event,
			IProgressMonitor monitor) throws CoreException {
		// TODO remover formatador do projeto
	}
}
