package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_LinksLister extends TestHelper{
	@org.junit.Test
	public void listLinks() throws Exception {
		String expected = "http://dokuwiki.org"
				+ "\nhttp://github.com/gturri"
				+ "\nns1:dummy";
		assertSuccess(expected, runWithArguments("listLinks", "links:start"));
	}

	@org.junit.Test
	public void listLinksWithLongFormat() throws Exception {
		String expected = "extern http://dokuwiki.org http://dokuwiki.org"
				+ "\nextern http://github.com/gturri http://github.com/gturri"
				+ "\nlocal ns1:dummy /dokuwikiITestsForXmlRpcClient_dokuwiki-2015-08-10a/doku.php?id=ns1:dummy";

		assertSuccess(expected, runWithArguments("listLinks", "-l", "links:start"));
	}
}
