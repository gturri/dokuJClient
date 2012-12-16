package itest;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

import dw.DokuJClient;
import dw.Page;

public class Test {
	private static String _url = "http://localhost/dokuwikiITestsForXmlRpcClient/lib/exe/xmlrpc.php";
	private static String _user = "xmlrpcuser";
	private static String _password = "xmlrpc";
	private static String _wikiVersion = "Release 2012-10-13 \"Adora Belle\"";
	private static String _wikiTitle = "test xmlrpc";
	private static Integer _apiVersion = 7;

	private DokuJClient _client;

	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(_url, _user, _password);
	}

	@org.junit.Test
	public void getVersion() throws Exception {
		assertEquals(_wikiVersion, _client.getVersion());
	}

	@org.junit.Test
	public void getXMLRPCAPIVersion() throws Exception {
		assertEquals(_apiVersion, _client.getXMLRPCAPIVersion());
	}

	@org.junit.Test
	public void getTime() throws Exception {
		int currentServerTime = _client.getTime();
		Thread.sleep(3000);
		int futureServerTime = _client.getTime();

		// We should expect that futureServerTime = currentServerTime + 3
		// but it could easily be +2 or +3 because it's rounded to second,
		// and I don't want he test to fail just because we have latency
		// on the network
		assertTrue(futureServerTime >= currentServerTime + 2);
		assertTrue(futureServerTime < currentServerTime + 10);
	}

	@org.junit.Test
	public void getPageListInAFlatNamespace() throws Exception {
		Set<String> expectedPages = new HashSet<String>();
		expectedPages.add("ns1:start");
		expectedPages.add("ns1:dummy");

		Set<Page> actualPages = _client.getPages("ns1");

		assertEquals(expectedPages.size(), actualPages.size());
		for (Page page : actualPages) {
			assertTrue(expectedPages.contains(page.id()));
		}
	}

	@org.junit.Test
	public void getPageListInANamespaceWithAnotherNamespace() throws Exception {
		// Check we get every pages with no max recursion level
		Set<String> expectedPages = new HashSet<String>();
		expectedPages.add("nswithanotherns:start");
		expectedPages.add("nswithanotherns:dummy");
		expectedPages.add("nswithanotherns:otherns:page");

		HashMap<String, Object> options = new HashMap<String, Object>();
		Set<Page> actualPages = _client.getPages("nswithanotherns", options);

		assertEquals(expectedPages.size(), actualPages.size());
		for (Page page : actualPages) {
			assertTrue(expectedPages.contains(page.id()));
		}

		// And now makes sure we can limit this recursion level
		expectedPages = new HashSet<String>();
		expectedPages.add("nswithanotherns:start");
		expectedPages.add("nswithanotherns:dummy");

		options.put("depth", "2");
		actualPages = _client.getPages("nswithanotherns", options);

		assertEquals(expectedPages.size(), actualPages.size());
		for (Page page : actualPages) {
			assertTrue(expectedPages.contains(page.id()));
		}
	}

	@org.junit.Test
	public void genericQueryWithParameters() throws XmlRpcException {
		Object[] params = new Object[] { "ns1:start" };
		//255 because we make the query as an admin
		assertEquals(255, _client.genericQuery("wiki.aclCheck", params));
	}
	
	@org.junit.Test
	public void genericQueryWithoutParameters() throws XmlRpcException {
		assertEquals(_wikiVersion, _client.genericQuery("dokuwiki.getVersion"));		
	}
	
	@org.junit.Test
	public void getTitle() throws XmlRpcException {
		assertEquals(_wikiTitle, _client.getTitle());		
	}
	
	@org.junit.Test
	public void getPage() throws XmlRpcException {
		String pageId = "ns1:dummy";
		
		String expectedContent = "dummy page inside ns1";
		assertEquals(expectedContent, _client.getPage(pageId));
		
		Page page = new Page(pageId);
		assertEquals(expectedContent, _client.getPage(page));
		
	}
}
