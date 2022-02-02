/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
println '*****************************'
println 'Checking Sample Cache Created'
println '*****************************'
println ''

File cache = new File(basedir, "target/formatter-maven-cache.properties")
assert cache.exists()

println '*********************************'
println 'Checking Sample Build Log Created'
println '*********************************'
println ''

File buildlog = new File(basedir, 'build.log')
assert buildlog.exists()

println '*********************************'
println 'Checking Build Log was successful'
println '*********************************'
println ''

assert buildlog.text.contains("[INFO] BUILD SUCCESS")

println '*****************************************'
println 'Checking Number of cache files is correct'
println '*****************************************'
println ''

assert !cache.text.contains("#")
