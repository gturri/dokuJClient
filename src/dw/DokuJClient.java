package dw;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dw.exception.DokuException;

public class DokuJClient {
	CoreClient _client;
	Locker _locker;
	
	public DokuJClient(String url) throws MalformedURLException{
		this(url, "", "");
	}
	
    public DokuJClient(String url, String user, String password) throws MalformedURLException{
    	_client = new CoreClient(url, user, password);
    	_locker = new Locker(_client);
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
	
	public List<PageVersion> getPageVersions(String pageId, Integer offset) throws DokuException {
		Object[] params = new Object[]{pageId, offset};
		Object result = genericQuery("wiki.getPageVersions", params);
		
		List<PageVersion> res = new ArrayList<PageVersion>();
		for ( Object o : (Object[]) result){
			@SuppressWarnings("unchecked")
			Map<String, Object> map  = (Map<String, Object>) o;
			
			String user = (String) map.get("user");
			String ip = (String) map.get("user");
			String type = (String) map.get("type");
			String summary = (String) map.get("sum");
			Date modified = (Date) map.get("modified");
			Integer version = (Integer) map.get("version");
			
			PageVersion pv = new PageVersion(user, ip, type, summary, modified, version);
			res.add(pv);
		}
		
		return res;
	}
	
	public String getPageVersion(String pageId, Integer timestamp) throws DokuException{
		Object[]params = new Object[]{pageId, timestamp};
		return (String) genericQuery("wiki.getPageVersion", params);
	}
	
	public List<Page> getPages(String namespace) throws DokuException {
		return getPages(namespace, null);
	}
	
	public List<Page> getPages(String namespace, Map<String, Object> options) throws DokuException {
		List<Object> params = new ArrayList<Object>();
		params.add(namespace);
		params.add(options == null ? "" : options);
		
		Object result = genericQuery("dokuwiki.getPagelist", params.toArray());
		List<Page> res = new ArrayList<Page>();
		for(Object o : (Object[]) result ){
			res.add(BuildPageFromResult(o));
		}

		return res;
	}
	
	public Integer aclCheck(String pageId) throws DokuException{
		Object res = _client.genericQuery("wiki.aclCheck", pageId);
		if ( res instanceof Integer ){
			return (Integer) res;
		}
		return Integer.valueOf((String) res);
	}
	
	public Integer getRPCVersionSupported() throws DokuException{
		return (Integer) genericQuery("wiki.getRPCVersionSupported");
	}
	
	public LockResult setLocks(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		return _locker.setLocks(pagesToLock, pagesToUnlock);
	}
	
	public LockResult lock(String pageId) throws DokuException{
		return _locker.lock(pageId);
	}
	
	public LockResult unlock(String pageId) throws DokuException{
		return _locker.unlock(pageId);
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
			Integer score = (Integer) mapResult.get("score");
			String snippet = (String) mapResult.get("snippet");
			Page page = BuildPageFromResult(mapResult);
			SearchResult sr = new SearchResult(page, score, snippet);
			searchResults.add(sr);
		}
		return searchResults;
	}
	
	public PageInfo getPageInfo(String pageId) throws DokuException{
		Object result = genericQuery("wiki.getPageInfo",pageId);
		return BuildPageInfoFromResult(result);
	}
	
	public PageInfo getPageInfoVersion(String pageId, Integer timestamp) throws DokuException {
		Object[] params = new Object[]{pageId, timestamp};
		Object result = genericQuery("wiki.getPageInfoVersion", params);
		return BuildPageInfoFromResult(result);
	}
	
	private PageInfo BuildPageInfoFromResult(Object result) throws DokuException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) result;
		String name = (String) resMap.get("name");
		Date modified = (Date) resMap.get("modified");
		String author = (String) resMap.get("author");
		Integer version = (Integer) resMap.get("version");
		return new PageInfo(name, modified, author, version);
	}
	
	@SuppressWarnings("unchecked")
	private Page BuildPageFromResult(Object result){
		return BuildPageFromResult((Map<String, Object>) result);
	}
	
	private Page BuildPageFromResult(Map<String, Object> result){
		String id = (String) result.get("id");
		Integer rev = (Integer) result.get("rev");
		Integer mtime = (Integer) result.get("mtime");
		Integer size = (Integer) result.get("size");
		return new Page(id, rev, mtime, size);
	}
	
	public String getPageHTML(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPageHTML", pageId);
	}
	
	public Object genericQuery(String action, Object param) throws DokuException{
		return _client.genericQuery(action, param);
	}
	
	public Object genericQuery(String action, Object[] params) throws DokuException{
		return _client.genericQuery(action, params);
	}
}
