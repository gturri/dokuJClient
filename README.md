Dokujclient is both a command line tool to interact with instances of Dokwiki,
and a Java library for [Dokuwiki xmlrpc interface](https://www.dokuwiki.org/devel:xmlrpc)
which is also compatible with Android.

Currently tested with:
* Hogfather          (dokuwiki-2020-07-29)
* Greebo             (dokuwiki-2018-04-22)
* Frusterick Manners (dokuwiki-2017-02-19)
* Elenor of Tsort    (dokuwiki-2016-06-26)
* Detritus           (dokuwiki-2015-08-10)
* Hrun               (dokuwiki-2014-09-29)
* Ponder Stibbons    (dokuwiki-2014-05-05)
* Binky              (dokuwiki-2013-12-08)

See the "Compatibility" section for more info

Command line tool
=================

Getting started
---------------

Here's a glimpse of what this tool can do:

    dokujclient --user myUser --password myPassword --url http://mywiki/lib/lib/exe/xmlrpc.php getTitle
    > myWiki title

    dokujclient help
    > [(-u|--user) <user>] --url <url> [(-p|--password) <password>] [-h|--help] [--version] [--password-interactive] command
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
It may be installed from the packages on several Linux distributions:

    sudo apt-get install dokujclient

If it isn't available in the packages of your plateform you may:
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

If you want to build your own application, if you don't want to deal with xmlrpc requests yourself,
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
JAR files are available via [Maven Central](http://repo1.maven.org/maven2/fr/turri/):

```xml
<dependency>
    <groupId>fr.turri</groupId>
    <artifactId>dokujclient</artifactId>
    <version>3.9.1</version>
</dependency>
```

Binaries may alse be [downloaded](http://turri.fr/dokujclient) directly.

To build them from the sources, see below.

Compiling from the command line
-------------------------------

On ubuntu, at the root of the project run:

    # You need maven to compiler
    sudo apt-get install maven

    #Actually build
    mvn package

It will generate in the directory `target` a dokujclient-x.y.z-bin.zip which contains
both the .jar and the executable command line tool


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

    mvn test


You can also run

    mvn site

in order to generate a test report and a test coverage report.

Compatibility
=============
dokuJClient aims at providing the same behavior for every supported version of Dokuwiki.
There are however, some discrepancies:

* getAttachmentInfo can't retrieve the page title with Angua (dokuwiki-2012-01-25b). It will set it to the page id instead
* addAcl and delAcl are supported for dokuwiki-2013-12-08 (Binky) or newer
* logoff will always clear the local cookies, but it will clear the server side ones only if you have dokuwiki-2014-05-05 (Ponder Stibbons) or a more recent one

Mailing list
============
The mailing list is oriented toward development and usage of DokuJClient. You can subscribe and unsubscribe from https://www.freelists.org/list/dokujclient
After subscribing, messages can be sent to dokujclient@freelists.org

Charityware
===========
Dokujclient is a personal open source project started in 2012. I have put hundreds of hours to maintain and enhance it.

It is provided as a [charityware](https://en.wikipedia.org/wiki/Charityware). It can be downloaded and installed at no charge. If you found it useful and would like to support its development, you may make a donation to a non-profit charitable organization.

To who
------
The preference goes to the [Wordl Wildlife Fund](https://support.worldwildlife.org/site/SPageServer?pagename=main_onetime) (WWF) because the are both protecting biodiversity and fighting climate change effectively.

If it isn't possible, any association acting for the environment would do the trick.

How
---
Forwarding me (address available e.g. in the git log) the confirmation email you send or receive will ensure your kind gesture will motivate me to continue developing this software.


I can't give money
-----------------
Making sure you have a positive impact would already be awesome:
* Volunteer to an NGO near you. Give some of your time
* Ride you bike instead of taking your car
* Buy local organic food
* Use a reusable bag and stop using plastic straws
* Plant trees
* Take only what you need, not what you can
