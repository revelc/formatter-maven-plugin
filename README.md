formatter-maven-plugin
======================

[![Linux Build Status](https://travis-ci.org/revelc/formatter-maven-plugin.svg)](https://travis-ci.org/revelc/formatter-maven-plugin)
[![Windows Build status](https://ci.appveyor.com/api/projects/status/j3cd0dwqlyu0iv2y?svg=true)](https://ci.appveyor.com/project/velo/formatter-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/revelc/formatter-maven-plugin/badge.svg?branch=master)](https://coveralls.io/r/revelc/formatter-maven-plugin?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/)
[![Issues](https://img.shields.io/github/issues/revelc/formatter-maven-plugin.svg)](https://github.com/revelc/formatter-maven-plugin/issues)
[![Forks](https://img.shields.io/github/forks/revelc/formatter-maven-plugin.svg)](https://github.com/revelc/formatter-maven-plugin/network)
[![Stars](https://img.shields.io/github/stars/revelc/formatter-maven-plugin.svg)](https://github.com/revelc/formatter-maven-plugin/stargazers)
[![Apache License](http://img.shields.io/badge/license-ASL-blue.svg)](https://github.com/revelc/formatter-maven-plugin/blob/master/license.txt)

forked from http://code.google.com/p/maven-java-formatter-plugin/
It was started after having read an article posted [here][1] (thanks to John for writing such a helpful article).

The original project aims to provide Java source file formatting using the Eclipse code formatter as a Maven plugin.  

Here I aim to provide file formatting accross languages.  So far, java and javascript meet my needs, HTML, XML and Scala are on my plans, but not limited to.
Also, this project also provides m2e configurator.  So you can be sure the IDE form the entire development team is properly configured.

M2e configurator update site:
http://revelc.github.io/formatter-maven-plugin/p2/2.0.0


This software is provided WITHOUT ANY WARRANTY.  Any loss of codes caused by the usage of this plugin is not
the responsibility of the author(s).  Be sure to use some source repository management system such as GIT
before using this plugin.

## How to use

View the Maven site documentation for the latest release [here](http://code.revelc.net/formatter-maven-plugin/)

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
2.1.0           | 4.6.3 (neon)

[1]: http://ssscripting.wordpress.com/2009/06/10/how-to-use-the-eclipse-code-formatter-from-your-code/
