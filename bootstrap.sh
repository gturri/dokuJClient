#!/bin/bash

git clone https://github.com/timroes/aXMLRPC
cd aXMLRPC
git checkout v1.7.1
mvn clean install -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6
