Description
===========

Java client for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc).
Currently only the Adora Belle version (2012-10-03) is targeted

Dependencies
============
[Apache XML-RPC](http://ws.apache.org/xmlrpc/download.html)

More precisely:
  * ws-common-util.jar
  * xmlrpc-client.jar
  * xmlrpc-common.jar

Build
=====
On ubuntu, at the root of the project run:

> sudo apt-get install libxmlrpc3-common-java
> sudo apt-get install ant
> ant

Getting started
===============
Everyhing is done through the DokuJClient: just create one and play with its methods.
Here is a quick example which display the title of the wiki and the list of its pages:

    import dw.DokuJClient;
    import dw.Page;
    
    public class Main {
      public static void main(String[] args) throws Exception{
        String url = "http://mywiki/lib/exe/xmlrpc.php";
        String user = "myUser";
        String pwd = "myPassword";

        DokuJClient client = new DokuJClient(url, user, pwd);
        System.out.println("Pages in the wiki " + client.getTitle() + " are:");
        for(Page page : client.getAllPages()){
          System.out.println(page.id());
        }

      }
    }

Make sure to add the jar listed in the Dependencies, as well as dokujclient.jar to your classpath.
Also make sure to configure your wiki so that xmlrpc interface is enable, and so that your user is
allowed to use it (ie: "remote" and "remoteuser" entries in your configuration)

Running integration tests
==========================
It's possible to run unit and integration tests within Eclipse.

Integration tests need to be able to connect to a fake wiki.
Please see src/dw/xmlrpc/itest/README.md to know how to set it up.
