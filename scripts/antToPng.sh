#!/bin/bash
# Generate a dependency graph of the tasks in build.xml
#
# Requires:
# dot (http://www.graphviz.org/)
# xalan (http://xml.apache.org/xalan-j/)
# and2dot (http://ant2dot.sourceforge.net)

if [ ! -e ant2dot.xsl ]; then
  #This file isn't shipped because its license seems incompatible
  echo "Install ant2dot.xsl in this directory (see http://ant2dot.sourceforge.net/xsl/ant2dot.xsl)"
  exit -1
fi

xalan -in ../build.xml -xsl ant2dot.xsl -out temp.dot 
sed -e 's#label=\"jar|\$\\\\{aXMLRPC.dir\\\\}/build.xml\", ##' temp.dot > build.dot
dot -Tpng -obuild.png build.dot
