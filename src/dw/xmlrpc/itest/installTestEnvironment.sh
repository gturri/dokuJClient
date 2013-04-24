#!/bin/bash

#directory where Dokuwiki should be installed in order to be reachable at http://localhost
serverFileSystemRoot=/var/www/
#Owner of the files (to make sure the instance of dokuwiki can ediable its pages)
serverFileSystemOwner=www-data
#Shouldn't be changed since itests try to connect to this url
baseUrl=http://localhost
dirNamePrefix=dokuwikiITestsForXmlRpcClient_

dwVersions="dokuwiki-rc2013-03-06 dokuwiki-2012-10-13 dokuwiki-2012-01-25b"
installDir=tmpForInstallation
relativeTestFileDir=testEnvironment

mkdir -p $installDir
cd $installDir

function installFakeWiki {
#Argument 1 is the name of the version of Dokuwiki to install
#Argument 2 is optional. If provided, it overrides the destination name, and a "sleeping wiki" is installed there
  dwVersion=$1
  echo "Going to install $dwVersion"
  pushd . >/dev/null
  
  #Avoid downloading the tarball again if we already have it
  if [ ! -e $dwVersion.tgz ]; then
    echo " Starting to download $dwVersion.tgz"
    wget www.splitbrain.org/_media/projects/dokuwiki/$dwVersion.tgz
  else
    echo " $dwVersion.tgz found. No need to download it again."
  fi
  
  rm -rf $dwVersion
  tar -xzf $dwVersion.tgz
  
  
  echo " Copying files to the server"
  dirName=${dirNamePrefix}${dwVersion}

  if [ $# -eq 2 ]; then
    destDir=$serverFileSystemRoot/$2
    echo " Installing in $destDir"
  else
    destDir=$serverFileSystemRoot/$dirName
  fi

  rm -rf $destDir 
  cp -r $dwVersion $destDir

  #Make the wiki sleeps
  if [ $# -eq 2 ]; then
    echo "<?php" > temp.php
    echo "sleep(5);" >> temp.php
    tail -n +2 $destDir/lib/exe/xmlrpc.php >> temp.php
    mv temp.php $destDir/lib/exe/xmlrpc.php
  fi
  
  echo " Configuring the wiki"
  cp ../$relativeTestFileDir/conf/* $destDir/conf
  rm -rf $destDir/data/pages
  cp -r ../$relativeTestFileDir/data/* $destDir/data
  chown -R $serverFileSystemOwner $destDir
  
  echo " Reseting some mtimes"
  touch -t201212230020.00 $destDir/data/attic/rev/start.1356218400.txt.gz
  touch -t201212230020.11 $destDir/data/attic/rev/start.1356218411.txt.gz
  touch -t201212230020.19 $destDir/data/attic/rev/start.1356218419.txt.gz
  touch -t201212230020.19 $destDir/data/pages/rev/start.txt
  touch -t201308011800.00 $destDir/data/pages/nswithanotherns/otherns/page.txt
  touch -t201308011900.00 $destDir/data/pages/nssearch/page3.txt
  touch -t201212242111.00 $destDir/data/media/ro_for_tests/img1.gif
  
  echo " Running the indexer"
  cd ../testEnvironment/data/pages
  for f in $(find . -name "*txt"); do
    f=$(echo $f | cut -d '.' -f 2 | tr / :)
    wget -O /dev/null -q $baseUrl/$dirName/lib/exe/indexer.php?id=$f
  done
  echo " Installed $dwVersion"
  popd >/dev/null
}

for dwVersion in $dwVersions; do
  installFakeWiki $dwVersion
done

echo Installing wiki for timeout tests
installFakeWiki dokuwiki-2012-10-13 ${dirNamePrefix}sleepingWiki

echo Done.
