Description
===========

Java client for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc)

Dependencies
============
(Apache XML-RPC)[http://ws.apache.org/xmlrpc/download.html]

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
  * Simplify the way the test environment is setup
  * Deal more cleanly with errors (ie: have custom exceptions for wrong login, wrong url, disconnected...)
  * Cover the rest of the interface (the target for the first version is the current last release of Dokuwiki: Adora Belle (2012-10-13))
  * Refactor to clean the code and the tests
