#!/bin/bash
# Builds documentation

outputDir=../build/doc

#Retrieve the project version
buildFile=../build.xml
versionNumber=$(grep name=\"version.number\" $buildFile | cut -d \" -f 4)
versionSuffix=$(grep name=\"version.suffix\" $buildFile | cut -d \" -f 4)
version=${versionNumber}${versionSuffix}

#Build Doxyfile from the template
rm -f Doxyfile
sed -e "s#@PROJECT_NUMBER@#${version}#" \
    -e "s#@OUTPUT_DIRECTORY@#$outputDir#" \
    -e "s#@HTML_OUTPUT@#doc-${version}#" \
    Doxyfile.template > Doxyfile

#Actualy build the documentation
rm -rf $outputFile
doxygen


echo Documentation is available in $outputDir
