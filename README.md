# maven-java-formatter-plugin
======================
[![Build Status](https://api.shippable.com/projects/54cfaf705ab6cc13528a8b4c/badge?branchName=master)](https://app.shippable.com/projects/54cfaf705ab6cc13528a8b4c/builds/latest)  
[![Coverage Status](https://coveralls.io/repos/velo/maven-formatter-plugin/badge.svg?branch=master)](https://coveralls.io/r/velo/maven-formatter-plugin?branch=master)  
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.revelc.code.formatter/formatter-maven-plugin/) 
[![Issues](https://img.shields.io/github/issues/velo/maven-formatter-plugin.svg)](https://github.com/velo/maven-formatter-plugin/issues) 
[![Forks](https://img.shields.io/github/forks/velo/maven-formatter-plugin.svg)](https://github.com/velo/maven-formatter-plugin/network) 
[![Stars](https://img.shields.io/github/stars/velo/maven-formatter-plugin.svg)](https://github.com/velo/maven-formatter-plugin/stargazers)
[![Apache License](http://img.shields.io/badge/license-ASL-blue.svg)](https://github.com/velo/maven-formatter-plugin/blob/master/license.txt)

forked from http://code.google.com/p/maven-java-formatter-plugin/
It was started after having read an article posted [here][1] (thanks to John for writing such a helpful article).

The original project aims to provide Java source file formatting using the Eclipse code formatter as a Maven plugin.  

Here I aim to provide file formatting accross languages.  So far, java and javascript meet my needs, HTML, XML and Scala are on my plans, but not limited to.
Also, this project also provides m2e configurator.  So you can be sure the IDE form the entire development team is properly configured.

M2e configurator update site:
http://velo.github.io/maven-formatter-plugin/p2/1.5.0


This software is provided WITHOUT ANY WARRANTY.  Any loss of codes caused by the usage of this plugin is not
the responsibility of the author(s).  Be sure to use some source repository management system such as GIT
before using this plugin.

##How to use

View the Maven site documentation for the latest release [here](http://code.revelc.net/formatter-maven-plugin/)
http://code.revelc.net/formatter-maven-plugin/

##Eclipse Compatibility

-------------------------------------
Plugin Version	| Eclipse Version
--------------  | ---------------
0.1.0           |	3.3.0
0.2.0 - 0.3.1   |	3.5.2
0.4             |	3.8.1 
0.5.2           |	4.4.1
1.0.0           |	3.8.3
1.1.0           |	3.8.3
1.2.0           |	3.8.3
1.3.0           |	3.8.3
1.4.0           |	4.4.0 (luna)
1.6.0.RC1       |	4.5.0.RC1 (mars)
1.6.0.RC2       |	4.5.0.RC2 (mars)
1.6.0.RC3       |	4.5.0.RC3 (mars)
1.6.0.RC4       |	4.5.0.RC4 (mars)

[1]: http://ssscripting.wordpress.com/2009/06/10/how-to-use-the-eclipse-code-formatter-from-your-code/
