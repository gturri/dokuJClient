#!/bin/bash
# Builds documentation

outputDir=../doc

#Retrieve the project version
buildFile=../build.xml
versionNumber=$(grep name=\"version.number\" $buildFile | cut -d \" -f 4)
versionSuffix=$(grep name=\"version.suffix\" $buildFile | cut -d \" -f 4)
version=${versionNumber}${versionSuffix}

#Build Doxyfile from the template
rm -f Doxyfile
sed -e "s#@PROJECT_NUMBER@#${version}#" \
    -e "s#@OUTPUT_DIRECTORY@#$outputDir#" \
    Doxyfile.template > Doxyfile

#Actualy build the documentation
rm -rf $outputFile
doxygen
