package com.marvinformatics.formatter.connector.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

import com.marvinformatics.formatter.connector.FormatterCore;

public class FormatterProjectConfigurator extends AbstractProjectConfigurator {

	public enum Formatter {
		JAVA("configFile", "src/config/eclipse/formatter/java.xml");

		private final String configuratioName;
		private final String defaultPath;

		private Formatter(String configuratioName, String defaultPath) {
			this.configuratioName = configuratioName;
			this.defaultPath = defaultPath;
		}

		public String getConfigurationName() {
			return this.configuratioName;
		}

		public String getDefaultPath() {
			return this.defaultPath;
		}
	}

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

		printSettings();
		if (eclipseProject.hasNature(JavaCore.NATURE_ID)) {
			Xpp3Dom[] settings = parseConfigurationFile(request, monitor);

			for (Xpp3Dom setting : settings) {
				Platform.getPreferencesService()
						.getRootNode()
						.node("project")
						.node(eclipseProject.getName())
						.node(JavaCore.PLUGIN_ID)
						.put(setting.getAttribute("id"),
								setting.getAttribute("value"));
			}

			Platform.getPreferencesService().getRootNode().node("project")
					.node(eclipseProject.getName()).node("org.eclipse.jdt.ui")
					.put("cleanup.format_source_code", "true");
		}

		// jsdtConfigFile = cfg.getChild("configJsFile").getValue();
		// src/config/eclipse/formatter/javascript.xml
	}

	private Xpp3Dom[] parseConfigurationFile(
			ProjectConfigurationRequest request, IProgressMonitor monitor)
			throws CoreException {
		InputStream content = readConfigFile(Formatter.JAVA, request, monitor);
		Xpp3Dom dom;
		try {
			dom = Xpp3DomBuilder.build(content, "UTF-8");
		} catch (XmlPullParserException e) {
			throw new CoreException(new Status(Status.ERROR,
					FormatterCore.PLUGIN_ID, "Invalid configuration XML", e));
		} catch (IOException e) {
			throw new CoreException(new Status(Status.ERROR,
					FormatterCore.PLUGIN_ID,
					"Unable to read configuration XML", e));
		}
		Xpp3Dom[] settings = dom.getChild("profile").getChildren("setting");
		return settings;
	}

	private InputStream readConfigFile(Formatter formatter,
			ProjectConfigurationRequest request, IProgressMonitor monitor)
			throws CoreException {
		IMavenProjectFacade mavenProject = request.getMavenProjectFacade();
		List<MojoExecution> executions = mavenProject.getMojoExecutions(
				"com.marvinformatics.formatter", "formatter-maven-plugin",
				monitor, "validate");

		MojoExecution execution = executions.get(0);
		Xpp3Dom cfg = execution.getConfiguration();
		String javaConfigFile = cfg.getChild(formatter.getConfigurationName())
				.getValue();
		if (javaConfigFile == null
				|| javaConfigFile.equalsIgnoreCase("${"
						+ formatter.getConfigurationName() + "}"))
			javaConfigFile = formatter.getDefaultPath();

		IFile cfgFile = request.getProject().getFile(javaConfigFile);

		if (!cfgFile.exists())
			throw new CoreException(new Status(IStatus.CANCEL,
					FormatterCore.PLUGIN_ID, "Configuration file not found!"));

		InputStream content = cfgFile.getContents();
		return content;
	}

	private void printSettings() {
		StringBuilder sb = new StringBuilder();
		Preferences prefs = Platform.getPreferencesService().getRootNode();
		try {
			eval(prefs, "\t", sb);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			File f = new File("tree.txt");
			FileWriter fw = new FileWriter(f);
			fw.write(sb.toString().toCharArray());
			fw.close();
			f.getAbsolutePath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void eval(Preferences prefs, String spacer, StringBuilder sb)
			throws Exception {
		String[] children = prefs.childrenNames();
		for (String child : children) {
			sb.append(spacer).append(child).append("\n");
			eval(prefs.node(child), spacer + "\t", sb);
		}
		String[] keys = prefs.keys();
		for (String key : keys) {
			sb.append(spacer).append(" * ").append(key).append(": ")
					.append(prefs.get(key, null)).append("\n");
		}
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
