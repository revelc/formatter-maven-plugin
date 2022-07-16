/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
import java.nio.file.Path;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.jface.text.BadLocationException;

/**
 * This mojo is very similar to Formatter mojo, but it is focused on CI servers.
 * <p>
 * If the code ain't formatted as expected this mojo will fail the build
 */
@Mojo(name = "validate", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = true, threadSafe = true)
public class ValidateMojo extends FormatterMojo {

    /** The aggregator. */
    @Parameter(defaultValue = "false", property = "aggregator", required = true)
    private boolean aggregator;

    /** The execution root. */
    @Parameter(defaultValue = "${project.executionRoot}", required = true)
    private boolean executionRoot;

    @Parameter(defaultValue = "${mojo.groupId}", required = true, readonly = true)
    private String mojoGroupId;

    @Parameter(defaultValue = "${mojo.artifactId}", required = true, readonly = true)
    private String mojoArtifactId;

    @Parameter(defaultValue = "${mojo.version}", required = true, readonly = true)
    private String mojoVersion;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", required = true, readonly = true)
    private MavenSession mavenSession;

    @Parameter(defaultValue = "${mojoExecution}", readonly = true, required = true)
    private MojoExecution mojoExecution;

    private boolean validationErrors;

    /**
     * When set to true, fail validation on first file formatter validation error.
     *
     * @since 2.20.0
     */
    @Parameter(defaultValue = "true", property = "formatter.failOnFirstValidationError")
    private boolean failOnFirstValidationError;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.aggregator && !this.executionRoot) {
            return;
        }

        super.execute();

        if (this.validationErrors) {
            throw new MojoFailureException(errorMessage());
        }
    }

    @Override
    protected void doFormatFile(final File file, final ResultCollector rc, final Properties hashCache,
            final String basedirPath, final boolean dryRun)
            throws IOException, MojoFailureException, BadLocationException, MojoExecutionException {
        super.doFormatFile(file, rc, hashCache, basedirPath, true);

        if (rc.successCount != 0) {
            if (failOnFirstValidationError) {
                throw new MojoFailureException(errorMessage());
            } else {
                this.validationErrors = true;
            }
        }
        if (rc.failCount != 0) {
            throw new MojoExecutionException("Error formatting '" + file + "' ");
        }
    }

    private String formatCommand() {
        String mojoInvocation = String.format("%s:%s:%s:%s", mojoGroupId, mojoArtifactId, mojoVersion,
                FORMAT_MOJO_NAME);
        boolean isMultiModule = mavenSession.getAllProjects().size() > 1;
        Path moduleDir = Path.of(".").toAbsolutePath().relativize(mavenProject.getBasedir().toPath().toAbsolutePath());
        String specifyModule = isMultiModule ? String.format("-f %s", moduleDir) : "";
        return String.format("mvn %s %s", specifyModule, mojoInvocation).replace("  ", " ");
    }

    private String errorMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("Formatter validation detected, see file above that triggered violation. ");
        builder.append("To see all violations, use `-Dformatter.failOnFirstValidationError=false`. ");
        builder.append("Please format file(s) (for example by invoking `");
        builder.append(formatCommand());
        builder.append("` and commit before running validation!");
        return builder.toString();
    }

}
