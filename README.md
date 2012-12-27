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

    sudo apt-get install libxmlrpc3-common-java
    sudo apt-get install ant
    ant

Getting started
===============
Everyhing is done through the DokuJClient: just create one and play with its methods.
Here is a quick example which displays the title of the wiki and the list of its pages:

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
To run the tests you'll need junit 4
Please see src/dw/xmlrpc/itest/README.md to know how to set it up.

After that, to run the tests, just run
    ant junit
or
    ant junitreport

You may want to run the tests in Eclipse. If so, please set the current working directory to "src".
ie: in the 'Run Configurations' window, choose its 'Arguments' tab, and set the
'Working directory' to '${workspace_loc:dokuJClient/src}
