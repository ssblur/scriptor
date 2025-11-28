./gradlew build

rm -rf beta
mkdir beta
mv ./neoforge/build/libs/scriptor-*[!-dev-shadow][!-sources].jar ./beta
mv ./fabric/build/libs/scriptor-*[!-dev-shadow][!-sources].jar ./beta
mv ./beta/scriptor-neoforge-*.jar ./beta/scriptor-neoforge-beta.jar
mv ./beta/scriptor-fabric-*.jar ./beta/scriptor-fabric-beta.jar

eval "export $(grep "unfocused_version=" gradle.properties)"

cd beta
wget "https://mvn.blur.lol/releases/com/ssblur/unfocused/unfocused-fabric/$unfocused_version/unfocused-fabric-$unfocused_version.jar"
wget "https://mvn.blur.lol/releases/com/ssblur/unfocused/unfocused-neoforge/$unfocused_version/unfocused-neoforge-$unfocused_version.jar"
