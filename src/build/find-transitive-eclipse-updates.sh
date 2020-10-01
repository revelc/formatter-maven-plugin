#! /usr/bin/env bash
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e
trap 'echo "[ERROR] Error occurred at $BASH_SOURCE:$LINENO command: $BASH_COMMAND"' ERR

groupId='org.eclipse.jdt'
artifactId='org.eclipse.jdt.core'

root_dir=$(git rev-parse --show-toplevel)
version=$(sed -e 's/xmlns=[^ ]*//' "$root_dir/pom.xml" | xmllint --xpath "//dependency[./groupId='$groupId' and ./artifactId='$artifactId']/version/text()" -)

if [[ -z $version ]]; then
  echo "Could not find version for $groupId:$artifactId"
  exit 1
fi

# ensure the artifact's pom is downloaded
mvn dependency:get -DgroupId="$groupId" -DartifactId="$artifactId" -Dversion="$version"

workfile=$(mktemp /tmp/update-deps-XXXXXXXXXX) || { echo "fatal: unable to allocate a temporary file"; exit 1; }

mvn -U --ntp -f ~/".m2/repository/${groupId//./\/}/${artifactId}/${version}/${artifactId}-${version}.pom" dependency:list -DincludeScope=runtime -Dsort=true -DoutputFile="$workfile"

# remove the old transitive deps resolved here
awk 'NR==FNR{if (/<!-- transitive dependency of .* resolved here to avoid version range -->/) for (i=-1;i<=4;i++) del[NR+i]; next} !(FNR in del)' "$root_dir/pom.xml" "$root_dir/pom.xml" > "$workfile-awktmp"
mv "$workfile-awktmp" "$root_dir/pom.xml"

depComment="transitive dependency of $artifactId resolved here to avoid version range"
mapfile -t deps < <(grep '^ .*:compile' "$workfile" | sed -e 's/\(^ *\| *$\)//g')
rm "$workfile"

for x in "${deps[@]}"; do
  depGroupId=$(echo "$x"    | cut -f1 -d:)
  depArtifactId=$(echo "$x" | cut -f2 -d:)
  depVersion=$(echo "$x"    | cut -f4 -d:)
  cat >>"$workfile" <<EOF
      <dependency>
        <!-- $depComment -->
        <groupId>$depGroupId</groupId>
        <artifactId>$depArtifactId</artifactId>
        <version>$depVersion</version>
      </dependency>
EOF
done

insertPos=$(( $(grep -n '<dependencyManagement>' pom.xml  | cut -f1 -d: | head -n1 ) + 1 ))
cat <(head --lines="$insertPos" "$root_dir/pom.xml") \
    "$workfile" \
    <(tail --lines=+"$(( insertPos + 1 ))" "$root_dir/pom.xml") \
    >"$workfile-combined"
rm "$workfile"
mv "$workfile-combined" "$root_dir/pom.xml"

(cd "$root_dir" && mvn clean verify)

