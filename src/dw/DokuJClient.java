package dw;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import dw.exception.DokuException;
import dw.exception.ExceptionConverter;

public class DokuJClient {
	private XmlRpcClient _client;
	
	public DokuJClient(String url) throws MalformedURLException{
		this(url, "", "");
	}
	
    public DokuJClient(String url, String user, String password) throws MalformedURLException{
    	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    	config.setServerURL(new URL(url));
    	config.setBasicUserName(user);
    	config.setBasicPassword(password);
    	_client = new XmlRpcClient();
    	_client.setConfig(config);
	}
    
    public Integer getTime() throws DokuException{
    	return (Integer) genericQuery("dokuwiki.getTime");
    }
    
    public Integer getXMLRPCAPIVersion() throws DokuException{
		return (Integer) genericQuery("dokuwiki.getXMLRPCAPIVersion");
    }
    
	public String getVersion() throws DokuException{
		return (String) genericQuery("dokuwiki.getVersion");
	}
	
	public List<Page> getPages(String namespace) throws DokuException {
		return getPages(namespace, new HashMap<String, Object>());
	}
	
	@SuppressWarnings("unchecked")
	public List<Page> getPages(String namespace, Map<String, Object> options) throws DokuException {
		List<Object> params = new ArrayList<Object>();
		params.add(namespace);
		params.add(options == null ? "" : options);
		Object result = null;
		
		result = genericQuery("dokuwiki.getPagelist", params.toArray());
		List<HashMap<String, Object>> resList = new ArrayList<HashMap<String, Object>>();
		for(Object o : (Object[]) result ){
			resList.add((HashMap<String, Object>) o);
		}

		List<Page> res = new ArrayList<Page>();
		for ( HashMap<String, Object> pageData : resList){
			Page page = new Page((String) pageData.get("id"),
					(Integer) pageData.get("rev"),
					(Integer) pageData.get("mtime"),
					(Integer) pageData.get("size"));
			res.add(page);
		}

		return res;
	}
	
	public void setLock(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lock", pagesToLock == null ? new ArrayList<String>() :pagesToLock);
		params.put("unlock", pagesToUnlock == null ? new ArrayList<String>() : pagesToUnlock);
		genericQuery("dokuwiki.setLocks", params);
	}
	
	public void lock(String pageId) throws DokuException{
		List<String> pageIds = new ArrayList<String>();
		pageIds.add(pageId);
		setLock(pageIds, null);
	}
	
	public void unlock(String pageId) throws DokuException{
		List<String> pageIds = new ArrayList<String>();
		pageIds.add(pageId);
		setLock(null, pageIds);
	}
	
	public String getTitle() throws DokuException{
		return (String) genericQuery("dokuwiki.getTitle");
	}
	
	public void appendPage(Page page, String rawWikiText) throws DokuException{
		appendPage(page.id(), rawWikiText);
	}
	
    public void appendPage(String pageId, String rawWikiText) throws DokuException {
		//TODO: check returned value
    	//TODO: Let use summary and isMinor
		Map<String, Object> attributes = new HashMap<String, Object>();
		genericQuery("dokuwiki.appendPage", new Object[]{pageId, rawWikiText, attributes});
	}
	
	public String getPage(Page page) throws DokuException {
		return getPage(page.id());
	}
	
	public String getPage(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPage", pageId);
	}
	
	public Object genericQuery(String action) throws DokuException {
		Object[] params = new Object[]{};
		return genericQuery(action, params);
	}
	
	public void putPage(Page page, String rawWikiText)throws DokuException {
		putPage(page.id(), rawWikiText);
	}
	
	public void putPage(String pageId, String rawWikiText)throws DokuException {
		//TODO: check returned value (documentation says it's a boolean, but in practice it seems to be an int
		//TODO: Let use summary and isMinor
		Map<String, Object> attributes = new HashMap<String, Object>();
		genericQuery("wiki.putPage", new Object[]{pageId, rawWikiText, attributes});
	}

	public List<SearchResult> search(String pattern) throws DokuException{
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		
		Object[] results = (Object[]) genericQuery("dokuwiki.search", pattern);
		for(Object result : results){
			@SuppressWarnings("unchecked")
			Map<String, Object> mapResult = (Map<String, Object>) result;
			String id = (String) mapResult.get("id");
			Integer rev = (Integer) mapResult.get("rev");
			Integer mtime = (Integer) mapResult.get("mtime");
			Integer score = (Integer) mapResult.get("score");
			String snippet = (String) mapResult.get("snippet");
			Integer size = (Integer) mapResult.get("size");
			Page page = new Page(id, rev, mtime, size);
			SearchResult sr = new SearchResult(page, score, snippet);
			searchResults.add(sr);
		}
		return searchResults;
	}
	
	public Object genericQuery(String action, Object param) throws DokuException{
		return genericQuery(action, new Object[]{param});
	}
	
	public Object genericQuery(String action, Object[] params) throws DokuException{
		try {
			return _client.execute(action, params);
		} catch (XmlRpcException e){
			System.out.println(e.toString());
			throw ExceptionConverter.Convert(e);
		}
	}
}
