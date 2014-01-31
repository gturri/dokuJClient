Dokujclient is both a command line tool to interact with instances of Dokwiki,
and a Java library for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc).

Currently tested with:
* Binky       (dokuwiki-2013-12-08)
* Weatherwax  (dokuwiki-2013-05-10)
* Adora Belle (dokuwiki-2012-10-03)
* Angua       (dokuwiki-2012-01-25b)

See the "Compatibility" section for more info

Command line tool
=================

Getting started
---------------

Here's a glimpse of what this tool can do:

    dokujclient --user myUser --password myPassword --url http://mywiki/lib/lib/exe/xmlrpc.php getTitle
    > myWiki title

    dokujclient help
    > [(-u|--user) <user>] --url <url> [(-p|--password) <password>] [-h|--help] [--version] command
    >
    > Available commands:
    > [...skipped...]

    #put user, password, and url, in the config file
    vim ~/.dokujclientrc

    #get the list of pages of all the wiki
    dokujclient getPagelist .
    > [...skipped...]

    dokujclient appendPage builds:synthesis "Build launched at 12:23 took 3'24"
    dokujclient getPage builds:synthesis
    > Build launched at 11:12 took 3'19
    > Build launched at 12:23 took 3'24

    #help command can give information about a given command
    dokujclient help putAttachment
    > Syntax for putAttachment: [-f|--force] <attachmentId> <localFile>

    dokujclient putAttachment some:file.jpg ~/results.jpg

Just make sure that your wiki is configured so that the xmlrpc interface is enabled, and so that your user is allowed to use it (ie: "remote" and "remoteuser" entries in your configuration).

Installation
------------
* Download the [binaries](http://turri.fr/dokujclient).
* Extract it, and add the extracted directoy to your path
* Ensure it's correctly installed, typing e.g.:

    dokujclient --version

Config file
-----------
To avoid typing your url, user, and password each time, you may create in your home a .dokujclientrc,
and put some or all of this info in it.

    echo "url=http://myhost/mywiki/lib/exe/xmlrpc.php" > ~/.dokujclientrc
    echo "user=toto" >> ~/.dokujclientrc
    echo "password=myPassword" >> ~/.dokujclientrc


dokuJClient.jar
==========

If you want to build your own application, if you don't want to deal with xmlrpc request yourself,
or if you don't want to handle the different versions of Dokuwiki, you may use this library.

Getting started
---------------
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
--------------------
Binaries may be [downloaded](http://turri.fr/dokujclient) directly.

To build them from the sources, see below.

Compiling from the command line
-------------------------------

On ubuntu, at the root of the project run:

    # You need maven to compiler
    sudo apt-get install maven

    # Install the dependency aXMLRPC which isn't in maven central repo
    ./bootstrap.sh

    #Actually build
    maven package

Hacking with Eclipse
--------------------

This project uses Maven. To be able [to use Eclipse](http://maven.apache.org/guides/mini/guide-ide-eclipse.html) you should:

    # Install Maven
    sudo apt-get install maven

    # Set the M2_REPO classpath variable
    mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo

    # Generate the Eclipe project files
    mvn eclipse:eclipse

To use the Eclipse projet, you need to ensure every dependencies are available.

Just compile once from the command line (see above) to ensure it will be ok.

Documentation
------------

To build documentation you must have doxygen installed. Then, run at the root of the repo:

    mvn javadoc:javadoc

To browse the generated docs, point your browser to target/site/apidocs/index.html

You may also directly [browse it](http://turri.fr/dokujclient/doc) online.


Running integration tests
--------------------------
To run the tests you'll need to set up a fake wiki.
Please see src/test/resources/README.md to know how to set it up.


After that, to run the tests, just run, at the root of the repo:

    mvn junit


You can also run

    mvn site

in order to generate a test report and a test coverage report.

Compatibility
=============
dokuJClient aims at providing the same behavior for every supported version of Dokuwiki.
There are however, some discrepancies:

* Angua (dokuwiki-2012-01-25b)
** getAttachmentInfo can't retrieve the page title. It instead sets it to the page id.
