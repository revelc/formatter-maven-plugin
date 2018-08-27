formatter-maven-plugin
======================

[![Linux Build Status](https://travis-ci.org/revelc/formatter-maven-plugin.svg)](https://travis-ci.org/revelc/formatter-maven-plugin)
[![Windows Build status](https://ci.appveyor.com/api/projects/status/j3cd0dwqlyu0iv2y?svg=true)](https://ci.appveyor.com/project/velo/formatter-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/revelc/formatter-maven-plugin/badge.svg?branch=master)](https://coveralls.io/r/revelc/formatter-maven-plugin?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/)
[![Issues](https://img.shields.io/github/issues/revelc/formatter-maven-plugin.svg)](https://github.com/revelc/formatter-maven-plugin/issues)
[![Forks](https://img.shields.io/github/forks/revelc/formatter-maven-plugin.svg)](https://github.com/revelc/formatter-maven-plugin/network)
[![Stars](https://img.shields.io/github/stars/revelc/formatter-maven-plugin.svg)](https://github.com/revelc/formatter-maven-plugin/stargazers)
[![Apache License](http://img.shields.io/badge/license-ASL-blue.svg)](https://github.com/revelc/formatter-maven-plugin/blob/master/LICENSE)

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

-------------------------------------
Plugin Version	| Eclipse Version
--------------  | ---------------
0.1.0           | 3.3.0
0.2.0 - 0.3.1   | 3.5.2
0.4             | 3.8.1 
0.5.2           | 4.4.1
1.0.0           | 3.8.3 (velo fork)
1.1.0           | 3.8.3 (velo fork)
1.2.0           | 3.8.3 (velo fork)
1.3.0           | 3.8.3 (velo fork)
1.4.0           | 4.4.0 (velo fork - luna)
1.6.0.RC1       | 4.5.0.RC1 (velo fork - mars)
1.6.0.RC2       | 4.5.0.RC2 (velo fork - mars)
1.6.0.RC3       | 4.5.0.RC3 (velo fork - mars)
1.6.0.RC4       | 4.5.0.RC4 (velo fork - mars)
2.0.0 - 2.0.1   | 4.5.2 (merged forks - mars)
2.1.0 - 2.6.0   | 4.6.3 (neon)
2.7.&ast;       | 4.7.&ast; (oxygen)
2.8.&ast;       | 4.8.&ast; (photon)

[Eclipse]: https://eclipse.org
[Maven]: https://maven.apache.org
[m2e]: https://eclipse.org/m2e
[blog]: http://ssscripting.wordpress.com/2009/06/10/how-to-use-the-eclipse-code-formatter-from-your-code/
[plugin-docs]: http://code.revelc.net/formatter-maven-plugin/
[formatter-m2e-configurator]: https://github.com/revelc/formatter-m2e-configurator
[related1]: http://wiki.eclipse.org/M2E_extension_development_environment
[related2]: http://wiki.eclipse.org/Submitting_M2E_marketplace_entries
[related3]: http://www.eclipse.org/forums/index.php/t/478639/0/unread/
[related4]: http://www.vogella.com/articles/EclipsePreferences/article.html
