package itest;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dw.DokuJClient;
import dw.Page;
import dw.SearchResult;
import dw.exception.DokuException;

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
	public void time() throws Exception {
		//We proceed as follow to avoid having to use Sleep:
		// * edit a page to update its modification time and retrieve it
		// * retrieve server time
		// * edit the page again and retrieve
		// * make sure times are consistent
		Page page = _client.getPages("singlePage").get(0);
		_client.putPage(page, "text before (time test)");
		
		page = _client.getPages("singlePage").get(0);
		Integer timeBefore = page.mtime();
		
		Integer serverTime = _client.getTime();
		
		_client.putPage(page, "text after (time test)");
		page = _client.getPages("singlePage").get(0);
		Integer timeAfter = page.mtime();
		
		assertTrue(0 < timeBefore);
		assertTrue(timeBefore <= serverTime);
		assertTrue(serverTime <= timeAfter);		
	}

	@org.junit.Test
	public void getPageListInAFlatNamespace() throws Exception {
		List<String> expectedPages = new ArrayList<String>();
		expectedPages.add("ns1:start");
		expectedPages.add("ns1:dummy");

		List<Page> actualPages = _client.getPages("ns1");

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
		List<Page> actualPages = _client.getPages("nswithanotherns", options);

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
	public void iCanPlayWihLockToAllowYouToWriteOrNot() throws Exception{
		String pageId  = "ns1:start";
		String initialContent = _client.getPage(pageId);
		String addedContent = "added";
		
		_client.lock(pageId);
		
		//Make sure you can't write
		DokuJClient otherClient = new DokuJClient(_url, "writeruser", "writer");
		try {
			otherClient.appendPage(pageId, addedContent);
		} catch (DokuException e){
			
		}
		String currentContent = _client.getPage(pageId);
		assertEquals(initialContent, currentContent);
		
		//Now check I can remove my lock and let you write
		_client.unlock("ns1:start");
		otherClient.appendPage(pageId, addedContent);
		currentContent = _client.getPage(pageId);
		assertEquals(initialContent + addedContent, currentContent);
	}
	
	//This doesn't really test the client, but it documents Dokuwiki's behavior,
	//hence it documents a non-intuitive behavior of the client
	@org.junit.Test
	public void iUnlockAutomaticallyWhenIWrite() throws Exception {
		String pageId  = "ns1:start";
		String initialContent = "init";
		String addedContent1 = "added1";
		String addedContent2 = "added2";
		
		//Get a known state
		_client.putPage(pageId, initialContent);
		
		_client.lock(pageId);
		
		//Now I write to let Dokuwiki unlock the page
		_client.appendPage(pageId, addedContent1);
		
		//And I make sure everyone may now write
		DokuJClient otherClient = new DokuJClient(_url, "writeruser", "writer");
		otherClient.appendPage(pageId, addedContent2);
		String currentContent = _client.getPage(pageId);
		assertEquals(initialContent + addedContent1 + addedContent2, currentContent);
	}
	
	@org.junit.Test
	public void genericQueryWithParameters() throws Exception {
		Object[] params = new Object[] { "ns1:start" };
		//255 because we make the query as an admin
		assertEquals(255, _client.genericQuery("wiki.aclCheck", params));
	}
	
	@org.junit.Test
	public void genericQueryWithoutParameters() throws Exception {
		assertEquals(_wikiVersion, _client.genericQuery("dokuwiki.getVersion"));		
	}
	
	@org.junit.Test
	public void getTitle() throws Exception {
		assertEquals(_wikiTitle, _client.getTitle());		
	}
	
	@org.junit.Test
	public void putAndGetPage() throws Exception {
		String pageId = "ns1:dummy";
		Page page = new Page(pageId);
		String content1 = "content1";
		String content2 = "content2";
		
		_client.putPage(pageId, content1);
		assertEquals(content1, _client.getPage(page));
		_client.putPage(page, content2);
		assertEquals(content2, _client.getPage(pageId));
	}

	@org.junit.Test
	public void appendPage() throws Exception {
		String pageId = "ns1:dummy";
		Page page = new Page(pageId);
		String initialContent = "put page in a clean state.";
		String append1 = "text appended.";
		String append2 = "final text";
		_client.putPage(pageId, initialContent);
		
		_client.appendPage(pageId, append1);
		assertEquals(initialContent + append1, _client.getPage(page));
		
		_client.appendPage(page, append2);
		assertEquals(initialContent + append1 + append2, _client.getPage(page));
	}
	
	@org.junit.Test
	public void search() throws Exception {
		List<SearchResult> results = _client.search("amet");
		
		assertEquals(2, results.size());
		
		assertEquals("nssearch:page3", results.get(0).page().id());
		assertEquals((Integer) 2, results.get(0).score());
		assertEquals("nssearch:start", results.get(1).page().id());
		assertEquals((Integer) 1, results.get(1).score());
	}
}
