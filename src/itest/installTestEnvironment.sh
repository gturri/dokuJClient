#directory where Dokuwiki should be installed in order to be reachable at http://localhost
serverFileSystemRoot=/var/www/
#Owner of the files (to make sure the instance of dokuwiki can ediable its pages)
serverFileSystemOwner=www-data
#Shouldn't be changed since itests try to connect to this url
destDir=$serverFileSystemRoot/dokuwikiITestsForXmlRpcClient

dwVersion=dokuwiki-2012-10-13
installDir=tmpForInstallation
relativeTestFileDir=testEnvironment

mkdir -p $installDir
cd $installDir

#Avoid downloading the tarball again if we already have it
if [ ! -e $dwVersion.tgz ]; then
  echo Starting to download $dwVersion.tgz
  wget www.splitbrain.org/_media/projects/dokuwiki/$dwVersion.tgz
else
  echo $dwVersion.tgz found. No need to download it again.
fi

rm -rf $dwVersion
tar -xzf $dwVersion.tgz


echo "Copying files to the server"
rm -rf $destDir $destDir
cp -r $dwVersion $destDir

echo "Configuring the wiki"
cp ../$relativeTestFileDir/conf/* $destDir/conf
cp -r ../$relativeTestFileDir/data/pages/* $destDir/data/pages
chown -R $serverFileSystemOwner $destDir

echo Done. You may want to remove $installDir
