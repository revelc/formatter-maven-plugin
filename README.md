<!--

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

# formatter-maven-plugin

[![mvn verify][ci_img]][ci_link]
[![Maven Central][maven_img]][maven_link]
[![Apache License][license_img]][license_link]

This project provides a mechanism to automatically (re)format your [Maven]
project during a Maven build, or to verify its formatting, so that your project
can converge on consistent code style regardless of user preferences, IDE
settings, etc.

It began following a post on the topic (which can be found [here][blog]; thanks
to John for writing such a helpful article). It now uses the [Eclipse] code
formatter for Java, and has grown to support formatting of other file types as
well.

For a companion [m2e] project configurator, see [formatter-m2e-configurator].

This software is provided WITHOUT ANY WARRANTY, and is available under the
Apache License, Version 2. Any code loss caused by using this plugin is not the
responsibility of the author(s). Be sure to use some source repository
management system such as GIT before using this plugin.

Contributions are welcome.

## How to use

View the Maven plugin documentation for the latest release [here][plugin-docs].

## Eclipse Compatibility

The primary use case of this plugin is to ensure consistent formatting for a
project, regardless of differences between IDE preferences or IDE versions that
developers use. Nevertheless, some users may find it useful to correlate
versions of this plugin to the version of Eclipse whose code is used as the
basis for this plugin's formatting. See [the project website][compat] for a
list of recent versions of this plugin and their corresponding Eclipse
versions.

## Integration Tests

mvn clean verify

## JDK Requirements
- 2.16.x requires jdk 8 as required by Eclipse binaries
- 2.17.x and later requires jdk 11 as required by Eclipse binaries
- 2.24.x and later requires jdk 17 as required by Eclipse binaries

[Eclipse]: https://eclipse.org
[Maven]: https://maven.apache.org
[blog]: https://ssscripting.wordpress.com/2009/06/10/how-to-use-the-eclipse-code-formatter-from-your-code/
[ci_img]: https://github.com/revelc/formatter-maven-plugin/actions/workflows/maven.yaml/badge.svg
[ci_link]: https://github.com/revelc/formatter-maven-plugin/actions
[compat]: https://code.revelc.net/formatter-maven-plugin/eclipse-versions.html
[formatter-m2e-configurator]: https://github.com/revelc/formatter-m2e-configurator
[license_img]: https://img.shields.io/badge/license-Apache%202.0-blue.svg
[license_link]: https://github.com/revelc/formatter-maven-plugin/blob/main/LICENSE
[m2e]: https://eclipse.org/m2e
[maven_img]: https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/badge.svg
[maven_link]: https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin
[plugin-docs]: https://code.revelc.net/formatter-maven-plugin/
[related1]: https://wiki.eclipse.org/M2E_extension_development_environment
[related2]: https://wiki.eclipse.org/Submitting_M2E_marketplace_entries
[related3]: https://www.eclipse.org/forums/index.php/t/478639/0/unread/
[related4]: https://www.vogella.com/articles/EclipsePreferences/article.html
