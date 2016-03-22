#!/bin/bash -xe

rm -rf app/src/test app/src/main/java

mkdir -p app/src/test/java/dw
cp -r ../src/test/java/dw/xmlrpc app/src/test/java/dw

mkdir -p app/src/main/java/dw
cp -r ../src/main/java/dw/xmlrpc app/src/main/java/dw

mkdir -p app/src/test/resources
cp -r ../src/test/resources/testEnvironment app/src/test/resources

./gradlew clean test
