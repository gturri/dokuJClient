Description
===========

Java client for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc).
Currently tested with:
* Adora Belle (2012-10-03)
* Angua (dokuwiki-2012-01-25b)

See the "Compatibility" section for more info

Getting started
===============
Everything is done through the DokuJClient: just create one and play with its methods.
Here is a quick example which displays the title of the wiki and the list of its pages:

    import dw.xmlrpc.DokuJClient;
    import dw.xmlrpc.Page;
    
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

Make sure to add the jar listed in the Dependencies section below, as well as dokujclient.jar to your classpath.
Also make sure to configure your wiki so that xmlrpc interface is enabled, and so that your user is
allowed to use it (ie: "remote" and "remoteuser" entries in your configuration)

Getting the binaries
====================
Binaries may be [downloaded](http://turri.fr/dokujclient) directly.


You may also build them:

On ubuntu, at the root of the project run:

    sudo apt-get install ant
    ant

Documentation
============

To build documentation you must have doxygen installed and run at the root of the repo:

    cd scripts
    ./doxygen.sh

You may also directly [browse it](http://turri.fr/dokujclient/doc) online.


Dependencies
============
[aXMLRPC](https://github.com/timroes/aXMLRPC)

More precisely:
  * aXMLRPC_v1.5.0.jar

Running integration tests
==========================
To run the tests you'll need junit 4.
You will also need to set up a fake wiki.
Please see src/dw/xmlrpc/itest/README.md to know how to set it up.


After that, to run the tests, just run, at the root of the repo:

    ant junit


or

    ant junitreport

if you have [Cobertura](http://cobertura.sourceforge.net/introduction.html) installed you
may also compute test coverage with:

    ant junitfullreport

You may want to run the tests in Eclipse. If so, please set the current working directory to "src".
ie: in the 'Run Configurations' window, choose its 'Arguments' tab, and set the
'Working directory' to '${workspace_loc:dokuJClient/src}

Compatibility
=============
dokuJClient aims at providing the same behavior for every supported version of Dokuwiki.
There are however, some discrepancies:

* Angua (dokuwiki-2012-01-25b)
** getAttachmentInfo can't retrieve the page title. It instead set it to the page id.
