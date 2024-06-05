Changelog
=========

Commits on branch "main"

 * [ci] Fix the eclipse versions markdown to reflect 2.24 level at eclipse 4.31
 * [cleanup] Reuse file.getName() as a string as its called many times
 * [rework] Reduce reused code by using common calculate hash code method
 * [pom] Bump guava to 33.2.1-jre
 * [894] Use the formattedCode for the formattedHash.
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.24.0 – 05/29/2024 03:40 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.24.0
 * [GHA] Add java 22 to build cycle
 * [pom] Allow newer jdks by overriding the parent invoker plugin
 * [license] Add license to the maven settings file
 * [pom] Do not 'rat' on changelog.md
 * [ci] Add notes for changelog
 * [ci] Update changelog
 * [ci] Sample new changelog
 * [mvn] Change the name from 'ossrh' to 'sonatype-nexus-snapshots' (legacy usage here)
 * [GHA] Adjustments to maven builds to ensure maven using latest (wrapper on the fly)
 * [GHA] Run sonatype using maven in place
 * [GHA] Add maven settings file that will work with sonatype file once secrets added
 * [actions] Add sonatype to auto deploy to snapshots
 * [GHA] Fix spacing in dependabot settings
 * [pom] Bump commons-logging to 1.3.2
 * [pom] Bump guava to 33.2.0-jre
 * [pom] Bump jackson to 2.17.1
 * [pom] Bump plexus utils to 4.0.1 and commons io to 2.16.1
 * [xml] Update cache check
 * [pom] Bump xml-formatter to 0.4.0
 * [pom] Bump jsdt-core to 3.4.4
 * [dependabot] Exclude plexux xml 4 and slf4j 2
 * [pom] Bump commons-io to 2.16.0
 * [pom] Bump commons-logging to 1.3.1
 * [pom] Bump jsdt-core to 3.4.2
 * [GHA] Use maven wrapper
 * [GHA] Drop jdk 22 and 23 for now as groovy doesn't support yet
 * [GHA] Run in far more instances
 * [todo] Add note we need to remove legacy IE support as IE is no longer supported
 * [ci] Small cleanup and formatting
 * [cleanup] Use java files where possible and reduce guava usage
 * [json] Move to using commons io instead of plexus utils
 * [ci] Formatting
 * [constructor] Add private constructor due to class being entirely static
 * [tests] Fix spelling mistakes / remove public
 * [cleanup] Just use toList instead of passing stream to collectors unmodifiable list
 * [ci] Cleanup if else statement
 * [var] since majority of 'var' is used, used the rest
 * [cleanup] Modernize some code
 * [cleanup] Remove left over return that is now unnecessary
 * [pom] Make commons-lang3 compile time instead of provided
 * [lang3] Use commons-lang3 over plexus utils for string utils
 * [pom] Use commons-logging again instead of jcl-over-slf4j and cleanup
 * [pom] Bump jackson to 2.17.0
 * [pom] Remove jackson annotations as unnecessary due to bom usage and no direct usage
 * [pom] Use plexus xml 3.0.0 or we lose maven 3 support
 * [pom] Bump guava to 33.1.0-jre
 * [pom] Update to eclipse 2024-03
 * [tests] Add slf4j-simple to tests to clear warnings and correct jcl version to 1.7.36
 * [GHA] Let all branches run jobs
 * [GHA] Add workflow_dispatch so jobs can be easily started fresh on demand
 * [GHA] Change caching to built in from setup-java
 * [json] Move formatter creation closure to usage
 * [fix] Make options private in json module
 * [caching] Rework for performance (only set hash as used)
 * [slf4j] Bump to 2.0.12
 * [jsoup] Bump to 1.17.2 and update hash in test cases
 * [jackson] Bump to 2.16.2 and update hashes in test cases
 * [fix] Add missing check for json to config checks
 * [cache] Rewrite cache based of improved work from impsort port of similar logic
 * [caching] Rewrite d1d5852 fixing #757
 * [pom] Add missing relativePath set to nothing
 * Fix JSON handling of LineEnding.KEEP (#830)
 * Require JDK 17 explicitly in POM and README (#826)
 * Bump the github-actions group with 3 updates (#825)
 * Add parent POM revelc-4 (#823)
 * Bump org.apache.maven.plugins:maven-javadoc-plugin from 3.6.0 to 3.6.3
 * Bump org.apache.maven.plugins:maven-surefire-plugin from 3.1.2 to 3.2.3
 * Bump org.apache.maven.plugins:maven-compiler-plugin
 * Bump org.apache.maven.plugins:maven-site-plugin
 * Bump org.apache.maven:maven-plugin-api from 3.9.5 to 3.9.6
 * Bump com.google.guava:guava from 32.1.3-jre to 33.0.0-jre
 * Bump org.codehaus.plexus:plexus-resources from 1.2.0 to 1.3.0
 * Bump org.slf4j:jcl-over-slf4j from 2.0.9 to 2.0.10
 * Update for Eclipse 2023-12
 * Bump org.apache.maven.plugin-tools:maven-plugin-annotations
 * Bump org.apache.maven.plugins:maven-plugin-plugin from 3.10.1 to 3.10.2
 * Bump org.apache.maven:maven-model from 3.9.4 to 3.9.6
 * Bump org.junit:junit-bom from 5.10.0 to 5.10.1
 * Bump org.apache.maven.plugins:maven-plugin-report-plugin
 * Bump com.google.guava:guava from 32.1.2-jre to 32.1.3-jre
 * Bump org.apache.maven:maven-core from 3.9.5 to 3.9.6
 * Bump org.codehaus.mojo:versions-maven-plugin from 2.16.1 to 2.16.2
 * Bump org.apache.maven.plugins:maven-project-info-reports-plugin
 * Bump org.apache.maven:maven-plugin-api from 3.9.4 to 3.9.5
 * Bump org.apache.maven.plugins:maven-plugin-plugin from 3.9.0 to 3.10.1
 * Bump org.apache.maven.skins:maven-fluido-skin from 2.0.0-M7 to 2.0.0-M8
 * Bump com.fasterxml.jackson:jackson-bom from 2.15.2 to 2.15.3
 * Bump org.apache.maven.plugin-tools:maven-plugin-annotations
 * Bump org.apache.maven.plugins:maven-site-plugin
 * Bump org.apache.maven.plugins:maven-clean-plugin from 3.3.1 to 3.3.2
 * Bump org.apache.maven.plugins:maven-dependency-plugin
 * Bump org.apache.maven:maven-core from 3.9.4 to 3.9.5
 * Bump com.mycila:license-maven-plugin from 4.2 to 4.3
 * Bump org.gaul:modernizer-maven-plugin from 2.6.0 to 2.7.0
 * Bump org.codehaus.mojo:versions-maven-plugin from 2.16.0 to 2.16.1
 * Bump org.slf4j:jcl-over-slf4j from 2.0.7 to 2.0.9
 * Bump org.apache.maven.plugins:maven-javadoc-plugin from 3.5.0 to 3.6.0
 * Bump org.apache.maven.plugins:maven-enforcer-plugin from 3.4.0 to 3.4.1
 * Bump org.apache.maven.plugins:maven-enforcer-plugin from 3.3.0 to 3.4.0
 * Bump org.apache.maven:maven-core from 3.9.3 to 3.9.4
 * Bump org.apache.maven:maven-plugin-api from 3.9.3 to 3.9.4
 * Bump org.apache.maven:maven-model from 3.9.3 to 3.9.4
 * Bump org.apache.maven.skins:maven-fluido-skin from 2.0.0-M6 to 2.0.0-M7 (#775)
 * Bump com.github.ekryd.sortpom:sortpom-maven-plugin from 3.2.1 to 3.3.0 (#774)
 * Bump org.apache.maven.plugins:maven-project-info-reports-plugin (#773)
 * Bump org.junit:junit-bom from 5.10.0-M1 to 5.10.0 (#772)
 * Bump org.apache.maven.plugins:maven-site-plugin (#771)
 * Bump com.google.guava:guava from 32.0.0-jre to 32.1.2-jre (#770)
 * Bump maven-plugin-api from 3.9.2 to 3.9.3
 * Bump maven-model from 3.9.1 to 3.9.3
 * Bump maven-release-plugin from 3.0.0 to 3.0.1
 * Bump versions-maven-plugin from 2.15.0 to 2.16.0
 * Bump maven-invoker-plugin from 3.5.1 to 3.6.0
 * Bump maven-core from 3.9.2 to 3.9.3
 * Bump maven-surefire-plugin from 3.1.0 to 3.1.2
 * Bump maven-clean-plugin from 3.2.0 to 3.3.1
 * Remove CHANGELOG.md (#756)
 * Bump formatter plugin after release
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.23.0 – 06/01/2023 05:34 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.23.0
 * Bump plugins for 2.23.0 release
 * Bump maven-project-info-reports-plugin from 3.4.3 to 3.4.4
 * Bump maven-surefire-plugin from 3.0.0 to 3.1.0
 * Bump jackson-bom from 2.15.0 to 2.15.2
 * Bump maven-plugin-annotations from 3.8.2 to 3.9.0
 * Bump maven-plugin-api from 3.9.1 to 3.9.2
 * Bump maven-source-plugin from 3.2.1 to 3.3.0
 * Bump guava from 31.1-jre to 32.0.0-jre
 * Bump plexus-utils from 3.5.1 to 4.0.0
 * Bump maven-core from 3.9.1 to 3.9.2
 * Bump maven-plugin-plugin from 3.8.2 to 3.9.0
 * Sort class methods (#745)
 * Bump jsoup from 1.15.4 to 1.16.1 (#735)
 * Bump maven-enforcer-plugin from 3.2.1 to 3.3.0 (#739)
 * Bump junit-bom from 5.9.2 to 5.9.3 (#738)
 * Bump maven-plugin-annotations from 3.8.1 to 3.8.2 (#741)
 * Bump maven-project-info-reports-plugin from 3.4.2 to 3.4.3 (#742)
 * Bump maven-site-plugin from 4.0.0-M6 to 4.0.0-M7 (#743)
 * Bump maven-plugin-report-plugin from 3.8.1 to 3.8.2 (#744)
 * Bump maven-plugin-plugin from 3.8.1 to 3.8.2 (#736)
 * Bump jackson-bom from 2.14.2 to 2.15.0 (#740)
 * Bump maven-fluido-skin from 2.0.0-M5 to 2.0.0-M6 (#737)
 * Cache config (#734)
 * Remove unnecessarily thrown exceptions (#733)
 * Remove unnecessary execute flag in source files
 * Bump modernizer-maven-plugin from 2.5.0 to 2.6.0 (#723)
 * Bump maven-install-plugin from 3.1.0 to 3.1.1 (#724)
 * Bump maven-fluido-skin from 2.0.0-M3 to 2.0.0-M5 (#725)
 * Bump maven-invoker-plugin from 3.5.0 to 3.5.1 (#726)
 * Bump maven-deploy-plugin from 3.1.0 to 3.1.1 (#727)
 * Bump license-maven-plugin from 4.2.rc3 to 4.2 (#728)
 * Bump maven-resources-plugin from 3.3.0 to 3.3.1 (#729)
 * [cleanup] Fix all error prone issues (#721)
 * [pom] Upgrade to non deprecated maven plugin report plugin 3.8.1 (#720)
 * [pom] Remove invalid phase from plugin plugin (#722)
 * Review and update provided Eclipse configs (#719)
 * Update for Eclipse 2023-03
 * Remove redundant null check (#709)
 * Bump maven-model from 3.8.7 to 3.9.0
 * Bump maven-javadoc-plugin from 3.4.1 to 3.5.0
 * Bump maven-deploy-plugin from 3.0.0 to 3.1.0
 * Bump maven-plugin-annotations from 3.7.1 to 3.8.1
 * Bump maven-fluido-skin from 2.0.0-M2 to 2.0.0-M3
 * Bump maven-compiler-plugin from 3.10.1 to 3.11.0
 * Bump maven-core from 3.8.7 to 3.9.0
 * Bump maven-invoker-plugin from 3.4.0 to 3.5.0
 * Bump versions-maven-plugin from 2.14.2 to 2.15.0
 * Bump maven-surefire-plugin from 3.0.0-M8 to 3.0.0-M9
 * Bump maven-plugin-plugin from 3.7.1 to 3.8.1
 * [json] Add missing items to the json properties file
 * [enhance] Add support for xml-formatter 0.3.0
 * Update fluido skin and require JDK 17 to build (#694)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.22.0 – 02/06/2023 02:25 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.22.0
 * Update copyright date for 2023
 * Update src/build/find-transitive-eclipse-updates.sh
 * [pom] Bump jsdt-core to 3.3.0
 * Bump osgi.annotation from 8.0.1 to 8.1.0
 * Bump maven-fluido-skin from 1.11.1 to 1.11.2
 * Bump jackson-bom from 2.14.1 to 2.14.2
 * Bump maven-enforcer-plugin from 3.1.0 to 3.2.1
 * Bump maven-plugin-plugin from 3.7.0 to 3.7.1
 * Bump sortpom-maven-plugin from 3.2.0 to 3.2.1
 * Bump jcl-over-slf4j from 1.7.36 to 2.0.6
 * [css] Add empty new line at end of file
 * [pom] Exclude commons logging from bean utils too.
 * [pom] Bump surefire to 3.0.0-M8
 * [pom] Bump maven project info reports to 3.4.2
 * [pom] Bump maven dependency plugin to 3.5.0
 * [pom] Bump maven plugin annotations to 3.7.1
 * [pom] Bump missed maven item to 3.8.7 to match others
 * [pom] Bump junit bom to 5.9.2
 * [pom] Exclude commons-logging, use jcl over slf4j instead
 * Update dependencies for JDT 3.32
 * Bump maven-plugin-api from 3.8.6 to 3.8.7
 * Bump maven-core from 3.8.6 to 3.8.7
 * Bump maven-invoker-plugin from 3.3.0 to 3.4.0
 * Bump versions-maven-plugin from 2.13.0 to 2.14.2
 * Bump cssparser from 0.9.29 to 0.9.30
 * Bump maven-site-plugin from 4.0.0-M3 to 4.0.0-M4
 * Bump org.eclipse.jdt.core from 3.31.0 to 3.32.0
 * [672] Add a parameter for including resources when formatting.
 * Bump maven-install-plugin from 3.0.1 to 3.1.0
 * Bump modernizer-maven-plugin from 2.4.0 to 2.5.0
 * Bump maven-plugin-plugin from 3.6.4 to 3.7.0
 * Bump impsort-maven-plugin from 1.7.0 to 1.8.0
 * Bump maven-release-plugin from 3.0.0-M6 to 3.0.0-M7
 * Bump jackson-bom from 2.13.4.20221013 to 2.14.1
 * Bump maven-plugin-annotations from 3.6.4 to 3.7.0
 * Bump maven-dependency-plugin from 3.3.0 to 3.4.0
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.21.0 – 11/04/2022 02:31 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.21.0
 * Bump versions-maven-plugin from 2.12.0 to 2.13.0
 * Bump jackson-bom from 2.13.4 to 2.13.4.20221013
 * Bump plexus-utils from 3.4.2 to 3.5.0
 * Bump versions (#660)
 * Bump dependency versions for Eclipse 2022-09 (#657)
 * Bump jsoup from 1.15.2 to 1.15.3 (#655)
 * Bump maven-javadoc-plugin from 3.4.0 to 3.4.1 (#654)
 * Bump maven-site-plugin from 3.12.0 to 3.12.1 (#653)
 * Bump maven-project-info-reports-plugin from 3.4.0 to 3.4.1 (#652)
 * Bump maven-resources-plugin from 3.2.0 to 3.3.0 (#649)
 * Bump junit-bom from 5.9.0-RC1 to 5.9.0 (#650)
 * Bump snapshot version to planned next release
 * Revert "[pom] Remove versions plugin from dep management as no longer used"
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.20.0 – 07/24/2022 05:29 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.20.0
 * [pom] Remove versions plugin from dep management as no longer used
 * [pom] Bump maven install to 3.0.1
 * [pom] Bump jsdt-core to 3.2.0
 * [pom] Bump osgi.annotation to 8.1.0
 * [pom] Bump junit to 5.9.0-RC1
 * [pom] Bump maven project info reports to 3.4.0
 * [pom] Bump sortpom to 3.2.0
 * [pom] Bump formatter plugin to 2.19.0
 * [pom] Bump maven install to 3.0.0
 * [pom] Bump maven deploy to 3.0.0
 * [pom] Remove old dependency comment as no longer applies
 * [pom] Remove old snapshot usage from jsoup testing
 * [ci] Formatting
 * [pom] Remove maven compiler create missing package info class as fixed
 * [ci] Add some clarifications to jsoup patch processing
 * [jsoup] Split out line normalization as needed on indent of 1 as well
 * [jsoup] Clarify how we get mixed line endings that need normalized
 * [jsoup] Add fix to add new line at end of file
 * [jsoup] Add comment that we are normalizing jsoup line ending results
 * [jsoup] Rename variables and add additional comments to help understand patch
 * [jsoup] Clarify comments about jsoup issues
 * [jsoup] Remove temp patch for fixing header line break as resolved in jsoup
 * [profile] Update java.xml to version 22 and add missing items. (#641)
 * [pom] Move back to upstream jsoup 1.15.2
 * [ci] Correct spelling
 * Update JDT transitive dependencies
 * Bump org.eclipse.jdt.core from 3.29.0 to 3.30.0 (#637)
 * Bump maven-enforcer-plugin from 3.0.0 to 3.1.0 (#633)
 * Bump maven-plugin-api from 3.8.5 to 3.8.6 (#634)
 * Bump maven-fluido-skin from 1.11.0 to 1.11.1 (#635)
 * Bump maven-core from 3.8.5 to 3.8.6 (#636)
 * Bump maven-surefire-plugin from 3.0.0-M6 to 3.0.0-M7 (#638)
 * Bump maven-release-plugin from 3.0.0-M5 to 3.0.0-M6 (#639)
 * Fix integration testing
 * Update GitHub Actions versions
 * Fix docs for removed compiler compliance option
 * Fix copyright dates (update to 2022)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.19.0 – 06/01/2022 03:34 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.19.0
 * Bump formatter and junit versions
 * Bump sortpom-maven-plugin from 3.0.1 to 3.1.3 (#628)
 * Bump maven-fluido-skin from 1.10.0 to 1.11.0 (#626)
 * Bump versions-maven-plugin from 2.10.0 to 2.11.0 (#625)
 * Bump impsort-maven-plugin from 1.6.2 to 1.7.0 (#624)
 * Bump maven-invoker-plugin from 3.2.2 to 3.3.0 (#629)
 * Bump plexus-utils from 3.4.1 to 3.4.2 (#630)
 * Bump jackson-bom from 2.13.2.20220328 to 2.13.3 (#627)
 * Bump maven-clean-plugin from 3.1.0 to 3.2.0
 * Bump maven-project-info-reports-plugin from 3.2.2 to 3.3.0
 * Bump maven-site-plugin from 3.11.0 to 3.12.0
 * Bump license-maven-plugin from 4.2.rc2 to 4.2.rc3
 * Bump maven-surefire-plugin from 3.0.0-M5 to 3.0.0-M6
 * Bump maven-javadoc-plugin from 3.3.2 to 3.4.0
 * Bump org.eclipse.jdt.core from 3.28.0 to 3.29.0 (#606)
 * Bump maven-dependency-plugin from 3.1.2 to 3.3.0 (#611)
 * Bump modernizer-maven-plugin from 2.3.0 to 2.4.0 (#609)
 * Bump jackson-bom from 2.13.1 to 2.13.2.20220328 (#610)
 * Bump maven-plugin-api from 3.8.4 to 3.8.5 (#612)
 * Bump maven-core from 3.8.4 to 3.8.5 (#607)
 * Bump maven-compiler-plugin from 3.10.0 to 3.10.1 (#608)
 * Bump versions-maven-plugin from 2.9.0 to 2.10.0 (#613)
 * Bump maven-compiler-plugin from 3.9.0 to 3.10.0 (#601)
 * Bump maven-core from 3.3.9 to 3.8.4 (#599)
 * Bump guava from 31.0.1-jre to 31.1-jre (#600)
 * Bump maven-project-info-reports-plugin from 3.2.1 to 3.2.2 (#602)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.18.0 – 02/28/2022 10:32 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.18.0
 * Clean up JsonFormatter and JsonFormatterTest
 * [pom] Let formatter move pom entry for jsoup due to group id change
 * [pom] Switch jsoup to fork until jsoup releases later
 * [pom] Bump site plugin to 3.11.0
 * [pom] Bump project info reports to 3.2.1
 * [pom] Bump javadoc plugin to 3.3.2
 * [pom] Bump jsdt-core to 3.1.0 for Eclipse 2021-12
 * merge master
 * make functions private
 * make multipleJsonObjectFileAllowed default true.
 * support multiple-object json file
 * Use JUnit BOM instead of version property
 * Follow-up from #593 to make path relative
 * Provide actionable error message when validation fails (#593)
 * [tests] Add first integration test confirming caching
 * [enhance] Add logic to prevent any possibility of a rewrite of cache occurring with zero changes
 * [ci] Rewrite results so it doesn't format badly across multiple lines
 * [ci] Add more to change log for internal work
 * [jsoup] Fix issue affecting --><! tags from being set together instead of line break
 * [jsoup] Transverse all lines to correct jsoup counting issues
 * [jsoup] Run twoPassTest to demonstrate counting bug in jsoup
 * [jsoup] Add an internal patch to trim trailing whitespace so our tests work
 * [test] Fix the linux side of things
 * [test] Fix the windows based test due to https changes, will fix linux separately
 * [secure] More to https and for redirects, just use the redirect as this is a test only
 * [secure] Update URL for apache license to https
 * [pom] Sort the fluido change into order
 * [secure] Use secure xsd for site and secure for our maven site
 * [skin] Use property for fluido so that it is updated automatically by dependabot
 * [tests] fix linux sided tests due to https change
 * [tests] Fix test hashes given https changes
 * [secure] Use secure maven xsd
 * [secure] Use only the https copy of apache license references
 * [ci] Break another if statement that contained 'return' so it completes naturally
 * [ci] Remove more trailing javadoc issues
 * [tests] Run full code cleanup here
 * [ci] Remove unnecessary ()
 * [ci] Cleanup some if statements for better processing
 * [ci] Add new String[] for clarity
 * [ci] Fix spelling and display issue with javadoc
 * [ci] Cleanup javadocs in formatter mojo
 * [jdk10] Use var where we can and more finals / minor javadoc cleanup
 * [ci] Interface is abstract already so remove abstract from undefined methods
 * [ci] Code qualifications / more finals
 * [finals] For most methods where not overlapping other source cleanup, apply final
 * [pom] Remove doclint ignoring as jautodoc'd remainder of app
 * [javadocs] Add all missing javadocs
 * [oss] Remove author not a good indicator of who is editing files and not very open source friendly
 * Bump maven-compiler-plugin from 3.8.1 to 3.9.0
 * Bump maven-jar-plugin from 3.2.1 to 3.2.2
 * Bump versions-maven-plugin from 2.8.1 to 2.9.0
 * Bump maven-plugin-annotations from 3.6.2 to 3.6.4
 * Bump maven-plugin-plugin from 3.6.2 to 3.6.4
 * [tests] Ensure junit 5 style of tests
 * [pom] Bump maven-release-plugin to 3.0.0-M5
 * [pom] Bump maven-jar-plugin to 3.2.1
 * [pom] Bump license-maven-plugin to 4.2.rc2
 * [html] Add in <script> tag to html page to confirm it validates properly without breaking formatting
 * [pom] Sort the pom repository entry
 * [actions] Run on all branches (it won't do this on gh-pages by default, that is controlled automatically)
 * [pom] Add snapshots repo so it pulls from there as well on github
 * [jsoup] Upgrade to 1.15.1 and adjust hash checks
 * [default] Change remove trailing whitespace default to 'true'
 * [ci] Formatting
 * [ci] Per sonarlint, remove duplicated text and use static for same
 * [rework] Make sure trailing space removal is counted as formatting
 * Bump sortpom-maven-plugin from 3.0.0 to 3.0.1
 * Bump maven-deploy-plugin from 3.0.0-M1 to 3.0.0-M2
 * Bump maven-site-plugin from 3.9.1 to 3.10.0
 * Bump jackson-bom from 2.13.0 to 2.13.1
 * Bump dependencies for Eclipse 2021-12 (#549)
 * [ci] Update changelog, add reference to it, update site version match with jdk noted
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.17.1 – 12/01/2021 02:31 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.17.1
 * Bump maven-fluido-skin from 1.9 to 1.10.0 (#545)
 * Bump maven-plugin-annotations from 3.6.1 to 3.6.2 (#546)
 * Bump maven-plugin-plugin from 3.6.1 to 3.6.2 (#544)
 * Bump junit.version from 5.8.1 to 5.8.2 (#543)
 * Bump maven-plugin-api from 3.8.3 to 3.8.4 (#542)
 * [pom] Bump jsdt-core to 3.0.0
 * Build for Java 11 (#533)
 * [hash] Fix hashes for rework on line endings
 * [fix] Correct how line endings are addressed and allow better support for this on css, html, and xml
 * [ci] Add to change log the cssparser hack fix for ie 7,8,9 of \9 retention
 * [pom] Bump internal formatter to 2.17.0
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.17.0 – 10/31/2021 03:23 AM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.17.0
 * [ci] Restore license checking, omit git tasks (#530)
 * Clean up two-pass implementation for PR #528
 * [tests] Rewrite all tests to do two passes (#528)
 * [ci] Skip license check during test as shallow clone doesn't have enough data for that
 * [css] As tests now fixed, change the hash to match new result
 * [css] Add patch to retain \9 during css formatting as cssparser converts to \t
 * [ci] Add javadoc for new pattern match and remove static now replaced on results output
 * Add a parameter to remove trailing whitespace (#521)
 * Improve logged output from plugin (#518)
 * [pom] Bump jsdt-core to 2.8.1
 * [actions] Move to java v2 and use zulu distribution
 * [pom] Bump plexus resources to 1.2.0 and remove obsolete exclusion
 * [docs] Update documentation for 2.17
 * [pom] Bump formatter-maven-plugin to 2.16.0
 * [pom] Bump maven plugin api to 3.8.3
 * [ci] Add change log to document move to Eclipse 2021-09
 * [cleanup] Rewrite the jsoup logic as we deleted xml support in favor of eclipse formatter
 * [cleanup] Delete css.propreties and html.properties in tests as not used
 * [cleanup] Delete left over xml.properties from when we tried jsoup for xml
 * Bump maven-javadoc-plugin from 3.3.0 to 3.3.1 (#507)
 * Bump guava from 30.1.1-jre to 31.0.1-jre (#508)
 * Bump junit.version from 5.7.2 to 5.8.1 (#506)
 * Configure dependabot to ignore Eclipse deps
 * Bump jackson-bom from 2.12.5 to 2.13.0 (#500)
 * Update Eclipse JDT transitive deps for 3.27.0
 * Bump org.eclipse.jdt.core from 3.26.0 to 3.27.0 (#496)
 * Bump maven-plugin-api from 3.8.1 to 3.8.2
 * Bump modernizer-maven-plugin from 2.2.0 to 2.3.0
 * Bump jackson-bom from 2.12.4 to 2.12.5
 * Bump plexus-utils from 3.4.0 to 3.4.1
 * Bump jackson-bom from 2.12.3 to 2.12.4
 * Bump plexus-utils from 3.3.0 to 3.4.0
 * Bump maven-enforcer-plugin from 3.0.0-M3 to 3.0.0
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.16.0 – 07/01/2021 04:05 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.16.0
 * Bump dependencies for Eclipse 2021-06 (#484)
 * Use space in empty xml element for sortpom
 * Bump maven-gpg-plugin from 1.6 to 3.0.1
 * Bump maven-javadoc-plugin from 3.2.0 to 3.3.0
 * Bump junit.version from 5.7.1 to 5.7.2
 * Bump revelc plugin versions and format pom
 * Bump license-maven-plugin from 4.0 to 4.1
 * Bump maven-release-plugin from 3.0.0-M1 to 3.0.0-M4
 * Bump license-maven-plugin-git from 4.0 to 4.1
 * Bump jackson-bom from 2.12.2 to 2.12.3
 * Bump maven-plugin-plugin from 3.6.0 to 3.6.1
 * Bump sortpom-maven-plugin from 2.14.1 to 3.0.0
 * Bump maven-plugin-api from 3.6.3 to 3.8.1
 * Bump maven-project-info-reports-plugin from 3.1.1 to 3.1.2
 * Bump maven-plugin-annotations from 3.6.0 to 3.6.1
 * Update README for #458
 * Fix #458 Add Eclipse compatibility info to website
 * Add capacity to use a custom base diretory (#457)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.15.0 – 04/07/2021 11:40 AM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.15.0
 * Bump xml-formatter to 0.2.2
 * Bump plugin and update cache javadoc (#455)
 * Bump JDT and deps to latest (2021-03)
 * Bump guava from 30.1-jre to 30.1.1-jre (#437)
 * Bump license-maven-plugin from 4.0.rc2 to 4.0 (#447)
 * Bump modernizer-maven-plugin from 2.1.0 to 2.2.0 (#444)
 * Bump sortpom-maven-plugin from 2.13.1 to 2.14.1 (#445)
 * Bump impsort-maven-plugin from 1.5.0 to 1.6.0 (#446)
 * Bump jackson-bom from 2.12.1 to 2.12.2 (#440)
 * Use nanos for correct duration calculations (#434)
 * Bump junit.version from 5.7.0 to 5.7.1 (#432)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.14.0 – 02/17/2021 07:25 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.14.0
 * Update CHANGELOG, copyright year, and xml-formatter
 * Bump jackson-bom from 2.12.0 to 2.12.1
 * Bump impsort-maven-plugin from 1.4.1 to 1.5.0
 * Bump sortpom-maven-plugin from 2.12.0 to 2.13.1
 * Update remaining eclipse deps after updating jdt core
 * Bump org.eclipse.jdt.core from 3.23.0 to 3.24.0 (#424)
 * Bump org.eclipse.core.commands from 3.9.700 to 3.9.800 (#423)
 * Bump org.eclipse.core.runtime from 3.19.0 to 3.20.0 (#422)
 * Bump org.eclipse.osgi from 3.16.0 to 3.16.100 (#421)
 * Bump org.eclipse.compare.core from 3.6.900 to 3.6.1000 (#420)
 * Bump org.eclipse.core.resources from 3.13.800 to 3.13.900 (#419)
 * Bump org.eclipse.text from 3.10.300 to 3.10.400 (#418)
 * Bump org.eclipse.equinox.preferences from 3.8.0 to 3.8.100 (#417)
 * Bump guava from 30.0-jre to 30.1-jre (#416)
 * Bump org.eclipse.core.jobs from 3.10.800 to 3.10.1000 (#415)
 * [pom] Bump formatter maven plugin internal usage to 2.13.0
 * Fix checksums test for new cssparser version
 * Bump jackson-bom from 2.11.3 to 2.12.0
 * Bump cssparser from 0.9.28 to 0.9.29
 * Bump cssparser from 0.9.27 to 0.9.28 (#408)
 * Bump guava from 29.0-jre to 30.0-jre (#409)
 * Bump jackson-bom from 2.11.2 to 2.11.3 (#410)
 * [json] improved JSON formatter with alphabeticalOrder and new line
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.13.0 – 10/01/2020 02:45 AM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.13.0
 * Update to Eclipse 2020-09
 * Add script to automatically and lock JDT deps
 * Bump org.eclipse.core.resources from 3.13.700 to 3.13.800
 * Bump org.eclipse.equinox.common from 3.12.0 to 3.13.0
 * Bump org.eclipse.core.expressions from 3.6.800 to 3.7.0
 * Bump org.eclipse.osgi from 3.15.300 to 3.16.0
 * Bump maven-project-info-reports-plugin from 3.1.0 to 3.1.1
 * Bump org.eclipse.text from 3.10.200 to 3.10.300
 * Bump org.eclipse.core.runtime from 3.18.0 to 3.19.0
 * Bump org.eclipse.equinox.registry from 3.8.800 to 3.9.0
 * Bump org.eclipse.jdt.core from 3.22.0 to 3.23.0
 * Bump org.eclipse.equinox.app from 1.4.500 to 1.5.0
 * Change log level for "<type> formatting is skipped" messages (#394)
 * Update plugins and changelog
 * Allow skipping regions of the source (#390)
 * Bump versions-maven-plugin from 2.7 to 2.8.1
 * Bump maven-resources-plugin from 3.1.0 to 3.2.0
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.12.2 – 08/25/2020 04:02 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.12.2
 * Fix formatting of license header (auto-formatted during build)
 * Update license badge and links in README
 * Clean up unused surefire config
 * Remove unnecessary badge for issues
 * Update README for main branch
 * Bump jackson-bom from 2.11.1 to 2.11.2 (#389)
 * Switch to GitHub actions for CI (#387)
 * Add user property for cachedir, for CLI use
 * Use target/ directory for default cachedir (#385)
 * Reduce frequency of dependabot
 * [ci] Property adhere to properties line breaks
 * [ci] Add new parameter to css test
 * [ci] Formatting
 * [ci] Apply formatting
 * [css] Add support for 'useSourceStringValues' introduced in 0.9.25
 * [pom] Bump formatter internal usage to 2.12.1
 * [javadoc] Add javadoc indicating usage of cache
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.12.1 – 07/11/2020 11:46 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.12.1
 * [ci] Add change log for 2.12.1
 * [enhance] Do not write the cache properties file unless it actually changed
 * [ci] License the dependabot.yml file
 * Create Dependabot config file
 * Bump sortpom-maven-plugin from 2.11.0 to 2.12.0
 * [ci] Add 2.12.0 change log
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.12.0 – 06/29/2020 07:23 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.12.0
 * [pom] Bump jsdt-core to 2.8.0 (eclipse 2020-06 release)
 * [perf] Rewrite the formatting logic so it only reads file once and writes once
 * [ci] Change maven injections to 'boolean' instead of 'Boolean'
 * [ci] Cleanup javadocs (new rules - better readability)
 * [ci] Move cache file to basedir
 * [ci] Remove fully qualified exception as already imported
 * [revert-test] Remove enabled if system property - it already checks that internally
 * [sonar] Fix issue with class just calling super (just inherit it)
 * [tests] Cleanup per sonar/junit 5 standards
 * [cache] For skipped items, continue to write the cache and skip the write
 * [javascript] Update javascript config file to include new item
 * Bump jackson-bom from 2.11.0 to 2.11.1
 * Bump maven-site-plugin from 3.9.0 to 3.9.1
 * [ci] Cleanup / more comments
 * [config] Upgrade java.xml config to Eclipse 2020-06
 * [config] Sort the xml configuration properties
 * [config] Eclipse does not keep this in order. It does no harm in doing so. This is as-is.
 * [ci] Sort default includes for file types
 * [config] Sort the java.xml file so we can see what is actually being upgraded and/or missing
 * [ci] Add package info files for all packaging
 * [enhance] Add debugging line to denote that formatter not found to format file
 * Bump org.eclipse.text from 3.10.100 to 3.10.200
 * Bump org.eclipse.core.jobs from 3.10.700 to 3.10.800
 * Bump org.eclipse.core.expressions from 3.6.700 to 3.6.800
 * Bump org.eclipse.osgi from 3.15.200 to 3.15.300
 * Bump org.eclipse.equinox.preferences from 3.7.700 to 3.8.0
 * Bump org.eclipse.equinox.common from 3.11.0 to 3.12.0
 * Bump org.eclipse.core.runtime from 3.17.100 to 3.18.0
 * Bump org.eclipse.equinox.registry from 3.8.700 to 3.8.800
 * Bump maven-surefire-plugin from 3.0.0-M4 to 3.0.0-M5
 * Bump org.eclipse.equinox.app from 1.4.400 to 1.4.500
 * Bump org.eclipse.core.contenttype from 3.7.600 to 3.7.700
 * Bump org.eclipse.jdt.core from 3.21.0 to 3.22.0
 * Bump maven-project-info-reports-plugin from 3.0.0 to 3.1.0
 * Update jackson (use BOM) to 2.11.0
 * Bump impsort-maven-plugin from 1.3.2 to 1.4.1 (#339)
 * Conditionally enable tests which fail due to system line separator (#336)
 * Bump maven-fluido-skin from 1.8 to 1.9 (#335)
 * Bump guava from 28.2-jre to 29.0-jre
 * Bump junit.version from 5.6.1 to 5.6.2 (#333)
 * make 'excludes' a property (#332)
 * [ci] Document some javadoc items to clear sonar lint issues.
 * Bump jsdt-core from 2.7.0 to 2.7.1 (#327)
 * [tests] Change hash code generated as jsoup formatting changed
 * [pom] Move jsoup back to 1.13.1
 * [pom] For jackson annotation, ignore its inclusion
 * [pom] Temporarily move back to jsoup 1.12.1 and update with test separately after review
 * [pom] Add java configuration for modernizer plugin
 * [pom] Bring all libraries up to date
 * [pom] Remove deprecated maven.compiler.source / maven.compiler.target
 * Update examples that use a lifecycle execution binding. (#322)
 * Add log about fixed line endings (#320)
 * Fix #307: Don't fall back to system line ending for LineEnding.KEEP (#319)
 * [ci] Format the xmlformatter file
 * Update XMLFormatter.java
 * updates xml-formatter properties
 * [ci] Bump appveyor to maven 3.6.3
 * [pom] Update xml-apis as to why not to use older copy
 * [pom] Update all dependencies
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.11.0 – 11/07/2019 11:38 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.11.0
 * Updates for Eclipse 2019-09 (#309)
 * Update jackson to 2.9.10
 * Update plugins and deps and support JDK11 builds (#305)
 * Update jackson-databind
 * Workaround long logs from maven builds in travis
 * Update jackson-databind (CVE-2019-12814)
 * Use previous release for formatting current code
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.10.0 – 06/20/2019 02:49 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.10.0
 * Update copyright year
 * Deploy Maven site using the site-maven-plugin
 * Update plugin and dependency versions (#303)
 * Update jackson (CVE-2019-12086)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.9.0 – 03/27/2019 11:25 AM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.9.0
 * Update CHANGELOG and README for 2.9.0
 * Update dependency and plugin versions
 * Adds xml-formatter (#298)
 * [ci] Bump version to 2.9.0-snapshot and add notes for ecipse id 2019-03 to readme
 * [pom] Update surefire to 3.0.0-M3
 * [pom] Update fluido comment to ensure it indicates tracking only related to it
 * [pom] Update jdt core to 3.17.0 (eclipse ide 2019-03)
 * [pom] Update plexus-utils to 3.2.0
 * [pom] Update guava to 27.1-jre
 * [pom] Update junit jupiter to 5.4.0
 * [pom] Update surefire to 3.0.0-M2
 * [pom] Update versions plugin to 2.7
 * [pom] UPdate modernizer plugin to 1.7.1
 * [pom] Update jar plugin to 3.1.1
 * [pom] Update install plugin to 3.0.0-M1
 * [pom] Update internal formatter use to 2.8.1
 * [pom] Update sortpom plugin to 2.10.0
 * [pom] Update jdt core to 3.16.0
 * [pom] Update plexus-utils to 3.1.1
 * [pom] Update guava to 27.0.1-jre
 * [pom] Update jackson to 2.9.8
 * [pom] Update junit to 5.3.2
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.8.1 – 11/01/2018 06:28 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.8.1
 * Fix #288 Ensure comment indent options are used (#290)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.8.0 – 10/14/2018 12:26 AM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.8.0
 * Update plugin versions and support Photon (#287)
 * [pom] Add commons-beantuils 1.9.3
 * Remove unnecessary m2e config for formatter
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.7.5 – 08/27/2018 06:15 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.7.5
 * Undo bump to 2.8.0 and jdt core 3.14 (photon)
 * Remove unnecessary SystemUtil class
 * Update plugins and dependencies
 * fail fast in case the config file cannot be found (#284)
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.7.4 – 07/03/2018 11:30 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.7.4
 * Standardize import order and other issues
 * Fix #279 Allow JSON formatter to omit space before separator (#280)
 * [tests] Update to junit5 (5.2.0)
 * [appveyor] Update to maven 3.5.3
 * [ci] Add oraclejdk9 to travis build
 * [pom] Update jsoup to 1.11.3
 * [pom] Update guava to 25.1-jre
 * [pom] Update all the plugins
 * [secure] Upgrade to jackson 2.9.6
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.7.3 – 05/09/2018 06:24 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.7.3
 * Update changelog
 * Fix #261 Require Maven project to run goals
 * Add m2e lifecycle metadata
 * Fix #274 Apply upstream enum formatting bug fix
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.7.2 – 03/29/2018 06:46 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.7.2
 * Update copyright date
 * Update docs for version information
 * Update dependency versions
 * Fix #260 Update to newer JDT core
 * Add modernizer plugin to build
 * Use latest official JDT core dependency (3.13.100)
 * Improve example documentation on site
 * Example was confusing, point to the build-tools module defined by the user.
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.7.1 – 11/16/2017 06:43 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.7.1
 * Use same org.eclipse.jdt.core version as in Eclipse Oxygen.1a (4.7.1a)
 * Bump appveyor to Maven 3.5.2
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.7.0 – 10/29/2017 06:19 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.7.0
 * [pom] UPdate maven-plugin-api to 3.5.2
 * [pom] Update guava to 23.3-jre
 * Raise Eclipse Level to Oxygen
 * Update formatter used to format self
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.6.0 – 10/28/2017 04:19 AM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.6.0
 * Clean up POM and resources
 * Fix #154 separate m2e-configurator and plugin
 * Remove unneeded build tooling
 * [maven-release-plugin] prepare for next development iteration
 * [maven-release-plugin] increment Bundle-Version for 2.5.1-SNAPSHOT

Version formatter-maven-plugin-2.5.0 – 10/27/2017 08:12 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.5.0
 * [maven-release-plugin] increment Bundle-Version for 2.5.0
 * Fix broken repo name in m2e configurator site
 * Lots of cleanup
 * Fix #244 Correct/Improve license documentation
 * Fix #245 Remove jdt-core and jsdt-core
 * Turn off proguard as unused and unnecessary plus not complete
 * [ecilpse] Replace our jdt-core with official eclipse jdt core 3.12.3 (neon.3)
 * [manual-deploy] Setup for 2.1.0.qualifier for m2e
 * [manual-deploy] Setup for 2.1.0.RC1 for m2e
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-2.1.0.RC1 – 10/22/2017 07:43 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.1.0.RC1
 * [ci] Change changelog to markdown
 * [ci] Add commented debug dump contents of formatted code
 * [ci] Adjust hash codes due to changing copyright year to 2017
 * [ci] Not sure why these are getting formatted remotely so update coypright date to fix
 * [ci] Change md5 comment to sha512
 * [ci] Add line ending being used to process file logging
 * [java8] Use jre complation profile JavaSE-1.8
 * [ci] For json, we cannot rely on line ending setting as we set it different so adjust test
 * [json] Migrate from gson to jackson to support line endings and indention
 * [ci] Add logger to help find issue with formatting tests
 * [ci] Adjust abstractFormatterTest to avoid collision
 * [ci] Move jsoup into it's own package
 * [ci] Use internal line separator
 * [pom] Update to guava 23.2-jre (important change, no longer a default guava)
 * [java] Switch entirely to java 8
 * [pom] Use standard guava as we are java 8 now
 * [enhance] Properly initialize all new formatters and use them consistently
 * [improve] Use our config files as defaults
 * [ci] Add carraige line feed per checkstyle to property files
 * [ci] Remove redundant final in try with resources block
 * [enhance] Add useEclipseDefaults ignoring provided files including our preset defaults
 * [ci] Correct formatter run options on site page
 * [improve] Bring back default formatting with compiler level settings through use of new parameter
 * [ci] Correct formatting on javadoc not being fixed by our current release
 * [java6] Raise compiler default levels to 1.6
 * [improve] Add ability to skip java and javascript files as well for greater flexibility
 * [javadoc] Fix javadoc formatting by adding CodeFormatter.F_INCLUDE_COMMENTS
 * [build] Restore non takari usage.
 * [mojo] Remove unthrown io exception
 * [ci] Cleanup interface
 * [tests] Correct build helper test location within maven
 * [pom] Update maven-dependency-plugin to 3.0.2
 * [pom] Update surefire to 2.20.1
 * [site] Correct site so examples, goals, and usage accessible everywhere
 * [proguard] Feb 2017 release wasn't working
 * [pom] Update guava to 23.1-guava
 * [pom] Update gson to 2.8.2
 * [pom] Update maven-enforcer-plugin to 3.0.0-M1
 * [pom] Update maven-javadoc-plugin to 3.0.0-M1
 * [ci] Correct internal UNKNOWN enum spelling (missing 'N')
 * [travis] Add openjdk8
 * [pom] Move maven prerequisites to correct location as it is only valid under plugin*
 * [ci] Correct formatting in pom
 * [tests] Minor cleanup on currently disabled m2e tests module
 * [ci] Change 'Marvininformatics to code-revelc.net
 * [pom] Update plexus-utils to 3.1.0
 * [pom] Update guava to 23.0-android
 * [pom] Update directory-maven-plugin to 0.2
 * [pom] Update maven-assembly-plugin to 3.1.0
 * [pom] Update wagon-ssh to 3.0.0
 * [pom] Update maven-compiler-plugin to 3.7.0
 * [ci] Update deprecated guava code
 * [pom] Update m2e to 1.9.0
 * [ci] Add support for CSS, JSON, XML, and HTML to change log
 * 
 * 
 * 
 * 
 * [ci] maven install plugin skip false to overcome takari
 * [m2e] Raise to 1.8.1
 * [ci] Drop java 7 support
 * [pom] Update tycho to 1.0.0
 * [maven] Add configuration to maven plugin to allow latest tycho to build
 * [neon] Add eclipse neon
 * [pom] Raise m2e to 1.7.0
 * [ci] Update appvoyer to windows server 2016
 * [ci] Raise appvoyer to Visual Studio 2017
 * [pom] Raise minimum maven version to 3.5.0
 * [pom] Update guava to 22.0-android (ie java 7 support)
 * [maven] Appveyor - raise maven to 3.5.0
 * [site] Update site to show validate
 * [validate] Add better error message fixing #172
 * [ci] Add some exclusion rules to license plugin
 * Mark mojos as thread-safe
 * [ci] Cleanup spacing on readme
 * [ci] Cleanup readme
 * [travis] Run sudo false mode
 * [pom] Update maven-dependency-plugin to 3.0.1
 * [pom] Update versions maven plugin to 2.4
 * [pom] UPdate plexus-io to 3.0.0
 * [pom] Update plexus-io to 2.7.2
 * [maven] Update maven-wrapper to 0.2.1
 * [pom] Update surefire to 2.20
 * [mvn] Update maven wrapper to 3.5.0
 * [pom] Update maven-plugin-api to 3.5.0
 * [pom] Update proguard-maven-plugin to 2.0.14
 * [pom] Update plexus-resources to 1.1.0
 * [pom] Update tracking on wagon-ssh to 2.12
 * [pom] Update internal used formatter to 2.0.1
 * [pom] Update nexus-staging-maven-plugin to 1.6.8
 * [ci] Prepare for next development cycle

Version formatter-maven-plugin-2.0.1 – 10/27/2017 08:20 PM -0400

 * [pom] For release, make deploy work
 * [maven] Update for next release
 * [eclipse] Update for next release 2.0.1
 * [ci] Update copyright date on pom
 * [m2e] Update missed internal branding/versions
 * [ci] Update missed qualifier to 2.0.1
 * [tests] Cleanup unit tests
 * [tests] Use sha512 and cleanup unit test
 * [secure] Switch from md5hash to sha512hash
 * [pom] Update wagon-ssh to 2.12
 * [pom] Update jacoco-maven-plugin to 0.7.9
 * [ci] Catch TextEdit 'IndexOutOfBoundsException' and print error in debug with contents
 * [secure] Use sha256 instead of sha1
 * [tests] Use java 7 StandardCharsets instead of guava
 * [pom] Update maven-dependency-plugin to 3.0.0
 * [pom] Update maven-compiler-plugin to 3.6.1
 * [travis] Use maven wrapper to run before_script / after_success
 * [travis] Run highest base directory before running formatter validate
 * [pom] Move formatter-maven-configuration into plugin management
 * [pom] Switch to using self -1 for internal formatting requirements
 * [ci] Set eclipse portions for next version
 * [maven-release-plugin] prepare for next development iteration
 * [pom] Correct relative path location for tycho support, it's not local ;)
 * [pom] Update jacoco to 0.7.8
 * [pom] Require maven 3.3.9
 * [pom] Update maven-resources-plugin to 3.0.2

Version formatter-maven-plugin-2.0.0 – 11/29/2016 10:39 PM -0500

 * [maven-release-plugin] prepare release formatter-maven-plugin-2.0.0
 * [revert] Tycho back to 0.24.0 for release with java 7 support
 * Fix #149 match up OSGi version with Maven version
 * Update maven-release-plugin properties
 * [tests] Switch import to match project in formatting test file
 * [ci] Sort pom
 * [ci] Cleanup copyright / license header
 * [sonar] Fix some sonar issues
 * [ci] Format tabs to spaces in one pom
 * [readme] Update to indicate velo for versions that were his release
 * Update security for mvnw for travis-ci
 * [ci] Update changelog and update maven to 3.2.5 (again)
 * [maven] Add maven wrapper to bring us up to maven 3.3.9 building on travis-ci
 * [pom] Update tycho to 0.24.0
 * [pom] Adjustments due to sort plugin
 * [pom] Update maven-fluido-skin to 1.6
 * [pom] Move sortpom and configuratino to dependency management
 * [pom] Move jacoco to dependency management
 * [pom] Move nexus staging maven plugin to dependency management
 * [pom] Add snapshot repository location
 * [pom] Update maven-site-plugin to 3.6 and add comment about wagon-git
 * [pom] Split out test side java usage for future move to 8 before core
 * [pom] Fix scm information
 * [pom] Remove lower level developer list as that is inherrited.
 * [poms] Add more spacing and very minor cleanup
 * [ci] Remove currently unused support module
 * [osgi] Correct execution environment to be JavaSE-1.7
 * [readme] Correct project name
 * [pom] Correct my links to linkedin to be https
 * [ci] Add execution filter for directory-maven-plugin
 * [pom] Update coveralls-maven-plugin to 4.3.0
 * [pom] Update maven-assembly-plugin to 3.0.0
 * [site] Update maven-fluido-skin to 1.6
 * [pom] Upgrade maven-compiler-plugin to 3.6.0
 * [pom] Upgrade guava to 20.0
 * [pom] Update maven-plugin-plugin to 3.5
 * [pom] Update maven-plugin-annotations to 3.5
 * [license] Fix license header so it works.  It was completely disabled.
 * [pom] Update license-maven-plugin to 3.0
 * [pom] Update versions-maven-plugin to 2.3
 * [canwrite] Only attempt to format files that can be written (ie skipping readOnly)
 * [javascript] Javascript needs to do same as java and only look at javascript files
 * [bug] Skipped items counted twice and failed items attempted anyway
 * [logging] Correct logging to write out intended message
 * [cleanup] Minor cleanup
 * [site] Combined site information with cleanup
 * [site] Drop duplicate site velocity files - site will show all
 * [site] Added top level side to get bootstrap look back
 * [sonar] Rewrite configFile lookup and use try with resources
 * [site] Fix some issues discovered via site generation
 * [sonar] Remove unnecessary constructor
 * [java7] Use direct System.lineSepartor
 * [spelling] Correct spelling mistake
 * [sonar] Use try with resources
 * [sonar] User proper exception type
 * [sonar] Do not use very short variable names
 * [sonar] Use multi try catch
 * [sonar] Use proper charset validation
 * Support multimodule projects
 * [pom] Drop back to marvinformatics 1.7.0
 * [pom] Upgrade proguard-maven-plugin to 2.0.13
 * [pom] Upgrade formatter-maven-plugin (marvinformatics) to 1.8.0
 * [pom] Upgrade javadoc-maven-plugin to 0.7.7.201606060606
 * [pom] Upgrade maven-source-plugin to 3.0.1
 * [pom] Upgrade maven-javadoc-plugin to 2.10.4
 * [pom] Upgrade maven-jar-plugin to 3.0.2
 * [pom] Upgrade maven-resources-plugin to 3.0.1
 * #109 : prevent exeception if only one config file is provided.
 * [copyright] Fix year on AnyClass.java and change sha to fix appvoyer
 * [pom] Update formatter-maven-plugin (velo) to 1.6.0
 * [pom] Update maven-jar-plugin to 3.0.0
 * [pom] Update maven-resources-plugin to 3.0.0
 * Excludes fell through??? - apply additional license headers
 * [license] Ignore license on build properties as it fails under travis-ci jdk 7 builds
 * [license] Update license headers on untracked files (auto), add NOTICE/LICENSE and remove license.txt
 * [pom] Update m2e-configurator to 1.3.0
 * [pom] Upgrade proguard-maven-plugin to 2.0.12
 * [pom] Update maven-site-plugin to 3.5.1
 * [pom] Update plexus-utils to 3.0.24
 * Fix warnings and javadocs
 * [pom] Update plexus-utils to 3.0.23
 * [pom] Update nexus-staging-maven-plugin to 1.6.7
 * [pom] Update maven-project-info-reports-plugin to 2.9
 * [pom] Update plexus-io to 2.7.1
 * Updated copyright years
 * squid:S1155  Collection.isEmpty() should be used to test for emptiness squid:S1118 Utility classes should not have public constructors squid:S1148  Throwable.printStackTrace(...) should not be called
 * squid:S2325 'private' methods that don't access instance data should be 'static' squid:S1192 String literals should not be duplicated
 * Better support for building with Maven 3.0.5
 * [pom] Update maven-fluido-skin to 1.5 and update site xsd to 1.7.0
 * [pom] Update jacoco-maven-plugin to 0.7.6.201602180812
 * [pom] Update maven-source-plugin to 3.0.0
 * [pom] Update maven-site-plugin to 3.5
 * Update note about java 8 issue
 * [pom] Update sortpom-maven-plugin to 2.5.0
 * [pom] Update maven-release-plugin to 2.5.3
 * [pom] Update maven-compiler-plugin to 3.5.1
 * [pom] Updated maven-surefire-plugin to 2.19.1
 * [pom] Updated plexus-io to 2.7
 * Change tabs to spaces
 * [pom] Added tycho target-platform-configuration to get rid of warnings
 * Change to use spaces not tabs and use 120 byte line length
 * [output] Straighten up alignment so it shows in maven output more nicely
 * [pom] Update guava to 19.0
 * [license] Use git hook to get inception date and current year
 * [pom] Updated to maven-plugin-api 3.3.9
 * Run validate formatter in travis builds
 * appveyor - per appveyor they state to not rely on their settings of line endings and...
 * Make ResourceTest cross platform in line ending checks
 * Resolved issue preventing upgrade to maven-plugin-plugin
 * [pom] Raise minimum maven version to 3.2.5
 * [pom] Update maven-clean-plugin to 3.0.0
 * Change lineEndings to AUTO.  This makes it work better. And matches gitattributes.
 * Turn off validate restriction on build
 * compare actual chars if LineEnding.AUTO is set
 * [pom] Updated maven-surefire-plugin to 2.19
 * [pom] Updated plexus-io to 2.6.1
 * [pom] Updated maven-assembly-plugin to 2.6
 * [pom] Updated maven-project-info-reports-plugin to 2.8.1
 * [pom] UPdated nexus-staging-maven-plugin to 1.6.6
 * [pom] Updated wagon-ssh to 2.10
 * [pom] Updated proguard-maven-plugin to 2.0.11
 * [tests] Switch all to junit 4 style
 * Fix "usage" page documentation
 * Removed old link from readme
 * Fixed configFile noted for exceptions - it was wrong and wouldn't show javascript
 * Updated plexus-utils to 3.0.22
 * Updated maven-javadoc-plugin to 2.10.3
 * Updated maven-enforcer-plugin to 1.4.1
 * Re-enabled coveralls report
 * Updated some references
 * Fixed lineending
 * Applying default format
 * Set default formatter
 * Running maven in batch mode
 * Building on windows
 * Added sort plugin to the mix
 * Simplified license-maven-plugin configuration
 * Restored the hability to configure sourceDirectories
 * Undoing 0178a3e420ce0bba5620e857aec5c7d8990bf220 since it breaks tests on windows
 * Rework resource usage
 * Minor formatting
 * Make project.build.sourceDirectory read only
 * Basic language cleanups.
 * JavaFormatterTest expects SHA value which is not the same
 * Make Resource Test box independent for test
 * Added -dontnote to proguard
 * More micro cleanups
 * Add try with resources.
 * Drop back to maven-plugin-plugin 3.2 due to lineEndings.java bug.
 * More this modifier and small cleanups
 * Fixed parent dir
 * Added missing 'this' modifier.
 * Add parent dir so builds can occur at any level
 * Format formatterMojo and add back resource jar handling
 * Updated copyright to 2015.
 * [googlecode] to relevc
 * Rework poms to closely match original and cleanup minor issues.
 * Correct maven XSD locations
 * Format readme
 * Reduce gitignore and remove duplicate gitignore
 * Update readme
 * [todo] remove unnecessary todo statements
 * Apply commons digester 3 back
 * Formatted poms
 * Remove openjdk6 from travis build profile
 * Rebranding to revelc
 * Removed site-maven-plugin as we are not using that
 * Shrinking all eclipse jar
 * Cleanup readme and remainder of old google code
 * Update changelog
 * Update plugin examples with new name
 * Fix link on site page.
 * Update site and assembly plugins
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-0.5.2 – 06/04/2015 08:57 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-0.5.2
 * Fix pom dependency for wagon-git
 * Add git so we can release site.
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-0.5.1 – 06/04/2015 08:44 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-0.5.1
 * mvn scm tag issue on rollback bump version
 * [maven-release-plugin] prepare release formatter-maven-plugin-0.5
 * Correct gh-pages git reference
 * [maven-release-plugin] rollback the release of formatter-maven-plugin-0.5
 * [maven-release-plugin] prepare for next development iteration

Version formatter-maven-plugin-0.5 – 06/04/2015 08:25 PM -0400

 * [maven-release-plugin] prepare release formatter-maven-plugin-0.5
 * Update .rultor.yml
 * Update .rultor.yml
 * Update .rultor.yml
 * Update .rultor.yml
 * Update .rultor.yml
 * Update .rultor.yml
 * Update .rultor.yml
 * Testing rultor release
 * Update pom.xml
 * Testing rultor release
 * Testing rultor release
 * Preparing rultor release
 * Generating p2 update site
 * Update README.md
 * Next development version
 * 1.6.0.RC1
 * Preparing for eclipse mars
 * Added license
 * Fix maven-enforcer usage
 * Formatting pom
 * Correct spelling and naming on gh-pages.
 * Make <name> match<artifactId> for myEclipse
 * Updated fluido
 * Clean up README links / formatting
 * Fix #25 Use standard names for skip parameter
 * Fix #38 update groupId and artifactId
 * Added issues, forks and stars
 * Some minor code cleanup.
 * Updated more
 * Update dependencies
 * Prevent synthetic access
 * Minor corrections to pom
 * Remove google code from pom and update to github
 * m2e missed
 * Updated dependencies
 * Move m2e to profile
 * Update pom
 * Fix profile check bug...never failed.
 * Light code cleanup
 * Update dependencies
 * Adding missing test javadocs.
 * Adding missing javadocs
 * Cleanup gitignores.
 * Update dependencies and plugins.
 * Updated site page xsd.
 * added descriptor to execution filter.
 * More plugin updates
 * Update assembly plugin
 * Update jxr plugin
 * Removed unnecessary version
 * Removed unnecessary constructors
 * Removed temporary system out used for testing
 * Use Maven Annotations
 * formatted.
 * Adding badges
 * Adding travis ci
 * Upgrade deprecated maven expressions
 * Type Safety
 * Override plugins and use fluido for site
 * Update to Maven 3
 * Commons Updates and UTF-8
 * Reworked licensing tags using license plugin
 * Upgraded to Junit 4.11
 * Add xml encoding tag and tabs to spaces
 * Added plugin to eclipse version.
 * Updated gitignore
 * :lollipop: Added .gitattributes & .gitignore files
 * Update README.md
 * Create .rultor.yml
 * Fixing global.env
 * typo
 * Jacoco workarounds!
 * Coveralls
 * Update README.md
 * Update README.md
 * Fixed compilation after rebasing
 * 1.6.0 development
 * 1.5.0
 * Update .shippable.yml
 * Update .shippable.yml
 * Added maven central badge
 * Update README.md
 * Removed duplicate developers section, per project owner feedback.
 * Update README.md
 * Add plugin to shippable!
 * Enhancement to add support for classpath sources formatter configurations.
 * Next release development
 * 1.4.1 release
 * PlexusIoFileResourceCollection is trying to touch all files, ignoring exclusion rules, dont wanna play nice? ok, you are gone.
 * Next dev version
 * Ignoring pom.xml.versionsBackup
 * 1.4.0 released
 * Added java 8 support
 * Next development version
 * [Release 1.4.0.M6 matching luna M6]
 * Bump eclipse version
 * Update README.md Conflicts: 	README.md
 * Preparing next development version
 * Fix parent name
 * Preparing next release
 * Added kepler repository
 * Making room for more formatters
 * Create README.md
 * [release] next development version
 * Catalog
 * Added release stuff
 * [release] next stable version
 * Fixed catalog.xml
 * Fixed feature name and description
 * Add aggregator configurator for validation
 * Added samples
 * Now using osgi preferences service
 * First very rudimentar attempt to create a m2e configurator
 * Next development version
 * [maven-release-plugin] prepare for next development iteration
 * [maven-release-plugin] prepare release formatter-1.1.0
 * Fixed lineendings
 * Add expression for includes
 * Next development version
 * hate file modes
 * [maven-release-plugin] prepare for next development iteration
 * mode changes
 * [maven-release-plugin] prepare release formatter-1.0.0
 * Add validate goal
 * Fix lineendings if format is ok
 * Using java 5 annotations
 * Rearanged packages
 * Bumped to eclipse juno formatter
 * Changed project structure to consume formatter artifacts from P2 repository
 * Enable JS format
 * Code format
 * Created JS formatter
 * No real use for guava
 * Pom personal touch
 * Make room for other languages then java
 * Convert svn:ignore properties to .gitignore.
 * Create README.md
 * Updated skipFormatting to use '@parameter' instead of reading directly from system properties, this way it can be configured in the POM.xml as well
 * Added support to skip formatting by setting 'skipFormat' system property to true, for example adding '-DskipFormat=true' when executing the 'mvn' command
 * [maven-release-plugin] prepare for next development iteration
 * [maven-release-plugin] prepare release 0.4
 * preparing for 0.4 release:
 * issue #21 added supporting directories in config again, updated changelog
 * issue #18: updated formatter to include comments
 * issue #18: updated pom to use newer eclipse dependencies for code formatter, move from 3.5.2 to 3.8.1
 * issue #15: merged 0.3 branch fix in r112 to trunk
 * issue #13: Added using maven.compiler expressions for compiler parameters
 * Fixed maven site link for encoding section
 * [maven-release-plugin] prepare for next development iteration
 * [maven-release-plugin] prepare release 0.3
 * Updated encoding parameter doc
 * Updated changelog for version 0.3
 * Changed project version from 0.3.0 to 0.3
 * Updated maven site, Added home link, Added encoding section and link
 * Cleanup imports after r89
 * issue #11: Added versions for eclipse dependencies to avoid update checks
 * issue #12: Added encoding parameter to read/write source files, Added plexus-utils dependency
 * Updated docs to add since version for 0.2 parameters
 * issue #10: Updated maven site examples to use includes/excludes
 * issue #10: Replaced directories parameter with includes/excludes, Added plexus-io dependency
 * [maven-release-plugin] prepare for next development iteration
 * [maven-release-plugin] prepare release 0.2.0
 * Updated changelog for version 0.2
 * issue #7: added maven site distribution section to pom
 * issue #7: updated maven site examples, added creating configfile
 * issue #9: upgraded eclipse formatter version to 3.5
 * issue #7: fixed comments in classes
 * issue #7: updated maven site examples and usage
 * issue #7: added maven site
 * issue #5: added plexus resources to resolve config file from classpath
 * issue #3: fixes overriding config file compiler version, removed unmodifiable map
 * issue 6: added failing mojo exec for bad config file by throwing exceptions
 * issue #8: simplified ruleset and added new xml classes for digester
 * issue #4: updated pom, moved distribution to profile, added sonatype profile
 * issue #4: changed groupId to googlecode
 * issue #4: updated pom metadata, switched fast-md5 to commons-codec
 * issue #3: added property to override config file compiler versions
 * issue #1: added line ending property
 * issue #2: changed mojo phase
 * [maven-release-plugin] prepare for next development iteration
 * [maven-release-plugin] prepare release 0.1.0
 * Added changelog file.
 * remove unneeded import statement. remove System.out.println() statement
 * Added feature to read from eclipse formatter config XML file
 * Skip writing formatted file if the original and result hash code is the same.
 * Added distributionManagement entry to enable "mvn deploy" to googlecode.
 * Initial upload of project files.


Generated by Mavanagaiata 1.0.1 at 06/04/2024 06:31 PM -0400
