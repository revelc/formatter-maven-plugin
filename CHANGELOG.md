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

ver 2.12.1
==========
- Do not write the cache file if it did not actually write any files for update witin the cache.  This allows users to save the cache to their repository and it will work on both windows/*nix without full rewrites.  The problem with the cache file write when not needed is due to java properties design issue where it logs timestamp upon change.

ver 2.12.2
==========
- Use target directory for the default cache location.

ver 2.13.0
==========
- Add support for excluding certain portions of java code from being reformatted.
- Change log level to DEBUG for messages with format "<type> formatting is skipped".
- Support Eclipse 2020-09 (4.17, JDT 3.23)

ver 2.14.0
==========
- Change log level to DEBUG for messages with format "<type> formatting is skipped".
- Support Eclipse 2020-12 (4.18, JDT 3.24)
- Improved JSON formatter with newline after formatting and alphabeticalOrder option

ver 2.15.0
==========
- Support Eclipse 2021-03 (4.19, JDT 3.25)

ver 2.16.0
==========
- Support Eclipse 2021-06 (4.20, JDT 3.26)
- Support overriding base directory
- Move docs about version compatibility to src/site/

ver 2.17.0
==========
- Support Eclipse 2021-09 (4.21, JDT 3.27) - now requires jdk 11
- Bump jsdt support to 2020-09
- Cleanup jsoup implementation as it only supports html in our implementation (had xml case statement but we use eclipse variation)
- Added patch to cssparser to retain ie 7,8,9 hack for '\9'
- Run our tests on two pass (ie test the formatting results) to confirm repeated same formatting.  Known issue with jsoup (html formatter)
- Improve logging output during plugin run (info) with more output in debug run
- Use timeutil to format time
- Add new parameter 'removeTrailingWhitespace' to remove trailing whitespace. (only happens if it actually formatted)

ver 2.17.1
==========
- Bump jsdt support to 2021-09 - now requires jdk 11
- Fix processing on end of line (EOL) markers making it accurate (mixed match resulted in odd behavior)

ver 2.18.0
==========
- Support Eclipse 2021-12 (4.22, JDT 3.28) - now requires jdk 11
- Move whitespace trim to ensure it's counted in formatting stats
- Set whitespace trim to default 'true' as it is formatting
- Added support for jsoup maxPaddingWidth, we default to -1 to disable to retain original behaviour on full pretty print
- Add <script> block to html tests to demonstrate upstream jsoup bug adding new lines has been fixed
- Add support for trimming trailing spaces from jsoup pretty print so our internal tests can function properly due to jsoup upstream bug
- Add support for counting leading spaces from jsoup pretty print so our internal tests can function property due to jsoup upstream bug
- Add support for breaking '--><! from jsoup pretty print so our internal tests can function propertly due to jsoup upstream bug
- Run overall code cleanup including using java 10 'var' since we require jdk 11 to work.
- Internal usage of http: has been switched to https and any redirects from old google code were updated to reflect this repo as seen in tests.  One left over http is a bogus site.
- Enhance cache support to not write timestamp to it and to sort content making it safe to check in and use via automation to update when using cross platform such as linux and windows.
  Without this, it can get random data write as this is read into memory first then written at end with no guarantee of order.
- Added first integration test to confirm caching with sorting and removal of timestamp can be confirmed properly

ver 2.19.0
==========
- Support Eclipse 2022-03 (4.23, JDT 3.29) - now requires jdk 11

ver 2.20.0
==========
- Support Eclipse 2022-06 (4.24, JDT 3.30) - requires jdk 11

ver 2.21.0
==========
- Support Eclipse 2022-09 (4.25, JDT 3.31) - requires jdk 11

ver 2.22.0
==========
- Support Eclipse 2022-12 (4.26, JDT 3.32) - requires jdk 11
