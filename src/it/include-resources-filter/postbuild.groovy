/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

println '***************************************************'
println 'Checking that includes filter is honoured with includeResources'
println '***************************************************'
println ''

File buildlog = new File(basedir, 'build.log')
assert buildlog.exists()

println '*********************************'
println 'Checking Build Log was successful'
println '*********************************'
println ''

assert buildlog.text.contains("[INFO] BUILD SUCCESS")

// The resource directory contains three files:
//   - changelog-1.0.xml  -> matches **/changelog-*.xml  (should be processed)
//   - log4j.xml          -> does NOT match               (must be excluded)
//   - app.json           -> does NOT match               (must be excluded)
//
// With the bug, DEFAULT_INCLUDES (**/*.xml, **/*.json, ...) are appended to the
// scanner even when the user has specified custom includes, so all three files
// get processed instead of just the one changelog file.
//
// Correct behaviour: exactly 1 file processed.

println '**********************************************************'
println 'Checking that only the changelog file was processed (1 file)'
println '**********************************************************'
println ''

assert buildlog.text.contains("Processed 1 files"),
    "Expected 'Processed 1 files' in build log but got:\n" +
    buildlog.readLines().find { it.contains("Processed") }
