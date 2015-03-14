This page contains list of features for each version of this software.

**0.1.0 (unstable)**
  * 2010-05-07: ([r26](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=26)) Added feature to read from eclipse formatter config XML file
  * 2010-05-01: ([r16](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=16)) Skip writing formatted file if the original and result hash code is the same

**0.2.0 (unstable)**
  * 2010-09-06: ([r59](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=59)) Changed plugin phase to run prior to compilation
  * 2010-09-17: ([r60](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=60)) Added parameter to specify line ending of formatted files
  * 2010-09-17: ([r61](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=61),[r67](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=67)) Added parameter to override config XML file compiler version
  * 2010-09-23: ([r66](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=66)) Fail plugin execution for an invalid config XML file
  * 2010-09-23: ([r68](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=68)) Allow config XML file to be resolved from dependency of plugin
  * 2010-09-28: ([r72](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=72)) Use Eclipse version 3.5 code formatter
  * 2010-09-28: ([r82](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=82),[r83](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=83),[r84](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=84)) Provide [Maven site for 0.2.0 release](http://maven-java-formatter-plugin.googlecode.com/svn/site/0.2.0/index.html)
  * 2010-09-28: Available from Maven central repository

**0.3**
  * 2011-01-21: ([r86](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=86)) Added includes/excludes parameters to replace directories parameter
  * 2011-01-24: ([r89](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=89)) Added encoding parameter to specify charset used to read/write source files and create hash
  * 2011-01-24: ([r90](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=90)) Fixed eclipse dependencies to avoid checking for updates

**0.4**
  * 2011-03-03: ([r109](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=109)) Added supporting compiler plugin properties for source/target
  * 2012-12-01: ([r118](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=118),[r119](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=119)) Updated eclipse formatter to version 3.8 from 3.5
  * 2013-05-23: ([r121](https://code.google.com/p/maven-java-formatter-plugin/source/detail?r=121)) Added back directories parameter to allow formatting sources other than main/test