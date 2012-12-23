package itest;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dw.DokuJClient;
import dw.LinkInfo;
import dw.Page;
import dw.PageInfo;
import dw.PageVersion;
import dw.SearchResult;

public class T_XmlRpcQueries {
	private DokuJClient _client;
	private DokuJClient _clientWriter;

	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
		_clientWriter = new DokuJClient(TestParams.url, TestParams.writerLogin, TestParams.writerPwd);
	}

	@org.junit.Test
	public void getVersion() throws Exception {
		assertEquals(TestParams.wikiVersion, _client.getVersion());
	}
	
	@org.junit.Test
	public void getPageInfo() throws Exception {
		String pageId = "rev:start";
		PageInfo pageInfo = _client.getPageInfo(pageId);
		assertEquals(pageId, pageInfo.name());
	}
	
	@org.junit.Test
	public void getPageInfoVersion() throws Exception {
		String pageId = "rev:start";
		Integer version = 1356218411;
		PageInfo pageInfo = _client.getPageInfoVersion(pageId, version);
		assertEquals(pageId, pageInfo.name());
		assertEquals(version, pageInfo.version());
		System.out.println(pageInfo.toString());
	}
	
	@org.junit.Test
	public void getPageVersions() throws Exception{
		List<PageVersion> versions = _client.getPageVersions("rev:start", 0);
		PageVersion version = versions.get(0);
		assertEquals((Integer) 1356218411, version.version());
	}
	
	@org.junit.Test
	public void getPageVersion() throws Exception {
		String pageId = "rev:start";
		assertEquals("version 1", _client.getPageVersion(pageId, 1356218400));
		assertEquals("", _client.getPageVersion(pageId, 1356218401));
		assertEquals("v2", _client.getPageVersion(pageId, 1356218411));
		assertEquals("3rd version", _client.getPageVersion(pageId, 1356218419));
	}
	
	@org.junit.Test
	public void aclCheck() throws Exception {
		assertEquals((Integer) 255, _client.aclCheck("ns1:start"));
		assertEquals((Integer) 8, _clientWriter.aclCheck("ns1:start"));
	}
	
	@org.junit.Test
	public void getRPCVersionSupported() throws Exception {
		assertEquals(TestParams.rpcVersionSupported, _client.getRPCVersionSupported());
	}

	@org.junit.Test
	public void getXMLRPCAPIVersion() throws Exception {
		assertEquals(TestParams.apiVersion, _client.getXMLRPCAPIVersion());
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
	public void genericQueryWithParameters() throws Exception {
		Object[] params = new Object[] { "ns1:start" };
		//255 because we make the query as an admin
		assertEquals(255, _client.genericQuery("wiki.aclCheck", params));
	}
	
	@org.junit.Test
	public void genericQueryWithoutParameters() throws Exception {
		assertEquals(TestParams.wikiVersion, _client.genericQuery("dokuwiki.getVersion"));		
	}
	
	@org.junit.Test
	public void getTitle() throws Exception {
		assertEquals(TestParams.wikiTitle, _client.getTitle());		
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
	public void getPageHTML() throws Exception {
		String pageId = "rev:start";
		assertEquals("\n<p>\n3rd version\n</p>\n", _client.getPageHTML(pageId));
	}
	
	@org.junit.Test
	public void getPageHTMLVersion() throws Exception{
		String pageId = "rev:start";
		Integer version = 1356218411;
		assertEquals("\n<p>\nv2\n</p>\n", _client.getPageHTMLVersion(pageId, version));
	}
	
	@org.junit.Test
	public void listLinks() throws Exception{
		List<LinkInfo> links = _client.listLinks("links:start");
		LinkInfo link0 = new LinkInfo(LinkInfo.Type.extern, "http://dokuwiki.org", "http://dokuwiki.org");
		LinkInfo link1 = new LinkInfo(LinkInfo.Type.extern, "http://github.com/gturri", "http://github.com/gturri");
		LinkInfo link2 = new LinkInfo(LinkInfo.Type.local, "ns1:dummy","/dokuwikiITestsForXmlRpcClient/doku.php?id=ns1:dummy" );

		assertEquals(link0, links.get(0));
		assertEquals(link1, links.get(1));
		assertEquals(link2, links.get(2));
	}
	
	@org.junit.Test
	public void getBackLinks() throws Exception{
		List<String> links = _client.getBackLinks("ns1:dummy");
		assertEquals("links:start", links.get(0));
	}
	
	@org.junit.Test
	public void getAllPages() throws Exception{
		List<Page> pages = _client.getAllPages();
		assertEquals(12, pages.size());
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
