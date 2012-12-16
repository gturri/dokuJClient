Running integration tests
=========================

Tests will try to connect to an instance of Dokuwiki. To run tests you should:
* Retrieve a version of [Adora Belle](www.splitbrain.org/_media/projects/dokuwiki/dokuwiki-2012-10-13.tgz)
* Install it so that its xml-rpc interface is available at http://localhost/dokuwiki/lib/exe/xmlrpc.php, with the login "xmlrpcuser" and the password "xmlrpc".
* Set the wiki title is "test xmlrpc"
* Create the pages:
    * ns1:dummy.txt
    * ns1:start.txt
    * nswithanotherns:dummy.txt
    * nswithanotherns:start.txt
    * nswithanotherns:otherns:page.txt

TODO
====
Distribute relevant conf and data files to simplify this installation
