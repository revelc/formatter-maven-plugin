ver 0.1.0 (2010-07-07)
======================
- accept following configuration options: directories, compilerSource, compilerCompliance, compilerTargetPlatform, configFile (eclipse formatter configuration file)

ver 0.2.0 (2010-09-28)
======================
- accept new configuration options: lineEnding, overrideConfigCompilerVersion, configFile (from dependency classpath, fail if invalid)
- use Eclipse 3.5 code formatter
- use process-sources phase
- use googlecode groupId
- provide Maven site
- available from Maven central repository

ver 0.3 (2011-03-01)
======================
- accept new configuration options: includes, excludes, encoding
- removed configuration option: directories
- uses specific Eclipse dependency versions to avoid checking for updates

ver 0.4 (2013-08-11)
======================
- using eclipse formatter version 3.8 instead of 3.5 (newer maven dependencies)
- added back configuration option: directories, to allow formatting source files other than main and test, such as generated sources
- added supporting compiler plugin properties for source/target

ver 0.5.2 (2015-06-04)
======================
- released under revelc
- using eclipse formatter version 4.4.1
- updated maven build
- deployed gh-pages
  -- Site page using bootstrap look and feel

ver 2.0.0 (2016-11-28)
======================
- Version increase to indicate master repo
- Supporting Eclipse Mars
- First release with velo/formatter-maven-plugin merged
- First supporting javascript from velo/formatter-maven-plugin

ver 2.0.1 (2017-02-12)
======================
- Trap bug in mars formatting with index out of bounds allowing formatting to continue.
- Internally use sha512 to hash files intead of md5.

ver 2.1.0 (2017-10-22)
======================
- Java 8 required Neon Release
- Support CSS, JSON, XML, and HTML formatting
- Small updates throughout

ver 2.5.0
=========
- Stop packaging jdt-core and jsdt-core
- Fix licensing for m2e-configurator
- Clean up maven POM files
- Ease releasing by auto-versioning Eclipse plugin files

ver 2.6.0
=========
- Separate formatter-m2e-configurator from formatter-maven-plugin

ver 2.7.0
=========
- Eclipse Oxygen release

ver 2.7.1
=========
- Switch to Tycho dependency of jdt-core for Eclipse Oxygen to fix enum formatting bug

ver 2.7.2
=========
- Switch back to Eclipse jdt-core depenency to fix html javadoc formatting bug

ver 2.7.3
=========
- Update jdt-core dependency to fix enum formatting bug; require Maven project to format/validate

ver 2.7.4
=========
- Plugin and dependency updates
- Support JDK9 builds
- Migrate to JUnit5
- Minor update to JSON formatter features
- Standardize import order with impsort plugin

ver 2.7.5
=========
- Plugin and dependency updates
- Fail fast on config file error

ver 2.8.0
=========
- Update plugins and support Eclipse 2018-09

ver 2.9.0
=========
- Support Eclipse 2019-03 formatter
- Update plugins, dependencies
- replace jsoup with xml-formatter, based on Eclipse ant formatter
- Apply bugfix for cssparser

ver 2.10.0
==========
- Support Eclipse 2019-06 formatter
- Update plugins, dependencies

ver 2.11.0
==========
- Support Eclipse 2019-09 formatter
- Update plugins, dependencies

ver 2.12.0
==========
- Support level with Eclipse 2020-06.
- Cache process rewritten to reduce reads/writes of files and speed up initial uncached usage.  Cache directory added with ability to override to survive maven clean on subsequent runs.  All files will be logged to cache now, not just files that are formatted.
- Patch vulnerable library usage.
- New 'wellFormedValidation=WARN' with xml formatter for allowing partial files.
- LineEnding.KEEP support adjustments to fix processing.
- Make 'formatter.excludes' property
- Additional debug logging for missing formatter or initialization errors.
- Bring up to date internal Eclipse Java and JavaScript format configuration files and sort for reasier future updates.  Sort other configuration files and sort file handling usage.
- All skip options switched from Wrapper Boolean to primitive boolean as all have defaults as false.
