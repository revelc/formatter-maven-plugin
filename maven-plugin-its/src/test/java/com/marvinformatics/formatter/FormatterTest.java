package com.marvinformatics.formatter;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({ "3.0.5", "3.2.5", "3.3.9" })
public class FormatterTest
{

  @Rule
  public final TestResources resources = new TestResources();

  public final MavenRuntime maven;

  public FormatterTest(MavenRuntimeBuilder mavenBuilder) throws Exception
  {
    this.maven = mavenBuilder.withCliOptions("-B").build();
  }

  @Test
  public void simpleFormat() throws Exception
  {
    File basedir = resources.getBasedir("format");
    maven.forProject(basedir)
        .withCliOption("-X")
        .execute("formatter:format")
        .assertErrorFreeLog()
        .assertLogText("Successfully formatted: 1 file(s)");
  }

  @Test
  public void validate() throws Exception
  {
    File basedir = resources.getBasedir("format");
    maven.forProject(basedir)
        .withCliOption("-X")
        .execute("formatter:validate")
        .assertLogText("Format doesn't match!");
  }
}
