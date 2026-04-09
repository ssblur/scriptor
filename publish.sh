export SSBLURMVNUSERNAME=admin
export SSBLURMVNPASSWORD=$(pass maven/mvn.blur.lol/admin)
export VERSION_TIMESTAMP=$(date +%s)

./gradlew publishToMavenLocal
./gradlew publish

echo "Published with timestamp $VERSION_TIMESTAMP"