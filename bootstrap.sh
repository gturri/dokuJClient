#!/bin/bash

git submodule init
git submodule update
pushd .
cd 3rdparty/aXMLRPC
git clean -fxd
git checkout .
popd
