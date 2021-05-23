#!/bin/bash -e

# Make sure we're in the directory where the script is
if [ -L "$0" ] && [ -x $(which readlink) ]; then
  thisFile="$(readlink -mn "$0")"
else
  thisFile="$0"
fi
cd "$(dirname "$thisFile")"

#directory where Dokuwiki should be installed in order to be reachable at http://localhost
serverFileSystemRoot=/var/www/html
#Owner of the files (to make sure the instance of dokuwiki can ediable its pages)
serverFileSystemOwner=www-data
#Shouldn't be changed since itests try to connect to this url
baseUrl=http://localhost
dirNamePrefix=dokuwikiITestsForXmlRpcClient_

dwVersions="dokuwiki-2013-12-08 dokuwiki-2014-05-05 dokuwiki-2014-09-29a dokuwiki-2015-08-10a dokuwiki-2016-06-26 dokuwiki-2017-02-19 dokuwiki-2018-04-22a dokuwiki-2020-07-29"
installDir=tmpForInstallation
relativeTestFileDir=testEnvironment

mkdir -p $installDir
cd $installDir

function runIndexer {
# Must be called from a directory where are all the pages (to find out all the names)
# The required environment variables must be set beforehand
  for f in $(find . -name "*txt"); do
    f=$(echo $f | cut -d '.' -f 2 | tr / :)
    wget -O /dev/null -q $baseUrl/$dirName/lib/exe/indexer.php?id=$f || wget -O /dev/null -q $baseUrl/$dirName/lib/exe/taskrunner.php?id=$f
  done
}

function runIndexerSeveralTimes {
  # run it several times (and sleep in between) because on some setups, one time only seems to not be enough
  runIndexer
  for i in {1..10}; do
    echo Indexer: $i/10
    sleep 1
    runIndexer
  done
}

function installFakeWiki {
#Argument 1 is the name of the version of Dokuwiki to install
#Argument 2 is optional. It can be "norpc". It asks for the setup of a particular wiki
#Argument 3 is required if arg2 is provided. It overrides the destination name
  dwVersion=$1
  if [ $# -eq 3 ]; then
    typeOfWiki=$2
    customDestDir=$3
  else
    unset typeOfWiki
    unset customDestDir
  fi
  echo "Going to install $dwVersion"
  pushd . >/dev/null

  #Avoid downloading the tarball again if we already have it
  if [ ! -e $dwVersion.tgz ]; then
    echo " Starting to download $dwVersion.tgz"
    wget http://download.dokuwiki.org/src/dokuwiki/$dwVersion.tgz
  else
    echo " $dwVersion.tgz found. No need to download it again."
  fi

  rm -rf $dwVersion
  tar -xzf $dwVersion.tgz


  echo " Copying files to the server"
  dirName=${dirNamePrefix}${dwVersion}

  if [ $# -eq 3 ]; then
    destDir=$serverFileSystemRoot/$customDestDir
    echo " Installing in $destDir"
  else
    destDir=$serverFileSystemRoot/$dirName
  fi

  rm -rf $destDir
  cp -r $dwVersion $destDir

  echo " Configuring the wiki"
  cp ../$relativeTestFileDir/conf/* $destDir/conf
  rm -rf $destDir/data/pages
  cp -r ../$relativeTestFileDir/data/* $destDir/data
  chown -R $serverFileSystemOwner $destDir

  if [ x$typeOfWiki = xnorpc ]; then
    echo " Using conf to not accept rpc queries"
    cp ../$relativeTestFileDir/conf/local.disabled_rpc.php $destDir/conf/local.php
  fi

  echo " Reseting some mtimes"
  touch -t201212230020.00 $destDir/data/attic/rev/start.1356218400.txt.gz
  touch -t201212230020.11 $destDir/data/attic/rev/start.1356218411.txt.gz
  touch -t201212230020.19 $destDir/data/attic/rev/start.1356218419.txt.gz
  touch -t201212230020.19 $destDir/data/pages/rev/start.txt
  touch -t201308011800.00 $destDir/data/pages/nswithanotherns/otherns/page.txt
  touch -t201311040647.25 $destDir/data/pages/nssearch/start.txt
  touch -t201308011900.00 $destDir/data/pages/nssearch/page3.txt
  touch -t201212242111.00 $destDir/data/media/ro_for_tests/img1.gif

  echo " Running the indexer"
  cd ../$relativeTestFileDir/data/pages

  runIndexerSeveralTimes

  echo " Installed $dwVersion"
  popd >/dev/null
}

for dwVersion in $dwVersions; do
  installFakeWiki $dwVersion
  installFakeWiki $dwVersion norpc ${dirNamePrefix}${dwVersion}_noRpc
done

echo Done.
