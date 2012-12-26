#directory where Dokuwiki should be installed in order to be reachable at http://localhost
serverFileSystemRoot=/var/www/
#Owner of the files (to make sure the instance of dokuwiki can ediable its pages)
serverFileSystemOwner=www-data
#Shouldn't be changed since itests try to connect to this url
baseUrl=http://localhost
dirName=dokuwikiITestsForXmlRpcClient
destDir=$serverFileSystemRoot/$dirName

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
rm -rf $destDir/data/pages
cp -r ../$relativeTestFileDir/data/* $destDir/data
chown -R $serverFileSystemOwner $destDir

echo "Reseting some mtimes"
touch -t201212230020.11 $destDir/data/attic/rev/start.1356218411.txt.gz
touch -t201212230020.00 $destDir/data/attic/rev/start.1356218400.txt.gz
touch -t201212230020.19 $destDir/data/attic/rev/start.1356218419.txt.gz

echo "Running the indexer"
cd ../testEnvironment/data/pages
for f in $(find . -name "*txt"); do
  f=$(echo $f | cut -d '.' -f 2 | tr / :)
  wget -O /dev/null -q $baseUrl/$dirName/lib/exe/indexer.php?id=$f
done

echo Done.
