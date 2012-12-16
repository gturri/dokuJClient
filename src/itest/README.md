Running integration tests
=========================

Tests will try to connect to an instance of Dokuwiki. To run tests you should:
* Retrieve a version of [Adora Belle](www.splitbrain.org/_media/projects/dokuwiki/dokuwiki-2012-10-13.tgz)
* Install it so that its xml-rpc interface is available at http://localhost/dokuwiki/lib/exe/xmlrpc.php, with the login "xmlrpcuser" and the password "xmlrpc".
* Set the wiki title is "test xmlrpc"
* Replace dokuwiki data/pages directory by the src/itest/data/pages one

TODO
====
Simplify this installation. Eg: distribute relevant conf files, and a script to install everything
