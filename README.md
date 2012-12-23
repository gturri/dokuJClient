Description
===========

Java client for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc)

Dependencies
============
[Apache XML-RPC](http://ws.apache.org/xmlrpc/download.html)

More precisely:
  * ws-common-util.jar
  * xmlrpc-client.jar
  * xmlrpc-common.jar

Running integration tests
==========================
Integration tests need to be able to connect to a fake wiki.
Please see src/itest/README.md to know how to set it up.

TODO
====
This is but POC. To make it evolves into a draft it needs to at least:
  * Cover the rest of the interface (the target for the first version is the current last release of Dokuwiki: Adora Belle (2012-10-13))
  * getPageVersions seems sensitive to filesystem mtime. May be worth studying.
  * Check retun values of appendPage and putPage
