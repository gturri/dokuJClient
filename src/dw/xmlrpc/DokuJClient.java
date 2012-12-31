package dw.xmlrpc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import dw.xmlrpc.exception.DokuException;

public class DokuJClient {
	CoreClient _client;
	Locker _locker;
	Attacher _attacher;

	public void setLogger(Logger logger){
		_client.setLogger(logger);
	}
	
	public DokuJClient(String url) throws MalformedURLException{
		this(url, "", "");
	}
	
    public DokuJClient(String url, String user, String password) throws MalformedURLException{
    	_client = new CoreClient(url, user, password);
    	_locker = new Locker(_client);
    	_attacher = new Attacher(_client);
    	Logger logger = Logger.getLogger(DokuJClient.class.toString());
    	setLogger(logger);
	}
    
	public void putAttachment(String attachmentId, File attachment, boolean overwrite) throws IOException, DokuException{
		_attacher.putAttachment(attachmentId, attachment, overwrite);
	}
    
	public void putAttachment(String attachmentId, String attachment, boolean overwrite) throws IOException, DokuException{
		putAttachment(attachmentId, new File(attachment), overwrite);
	}
	
	public AttachmentInfo getAttachmentInfo(String fileId) throws DokuException{
		return _attacher.getAttachmentInfo(fileId);
	}
	
	public void deleteAttachment(String fileId) throws DokuException{
		_attacher.deleteAttachment(fileId);
	}
	
	public File getAttachment(String fileId, String localPath) throws DokuException, IOException{
		return _attacher.getAttachment(fileId, localPath);
	}

	public List<AttachmentInfo> getAttachments(String namespace) throws DokuException{
		return getAttachments(namespace, null);
	}
	
	public List<AttachmentInfo> getAttachments(String namespace, Map<String, Object> additionalParams) throws DokuException{
		return _attacher.getAttachments(namespace, additionalParams);
	}
	
	public List<MediaChange> getRecentMediaChanges(Integer timestamp) throws DokuException{
		return _attacher.getRecentMediaChanges(timestamp);
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
			Map<String, Object> map = (Map<String, Object>) o;
			if ( map.get("name") == null ){
				map.put("name", pageId);
			}
			res.add(buildPageVersionFromResult(map, false));
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private PageVersion buildPageVersionFromResult(Object result, boolean keyLastModified){
		return buildPageVersionFromResult((Map<String, Object>) result, keyLastModified);
	}
	
	private PageVersion buildPageVersionFromResult(Map<String, Object> map, boolean keyLastModified){
		String pageId = (String) map.get("name");
		String user = (String) map.get("user");
		String ip = (String) map.get("ip");
		String type = (String) map.get("type");
		String summary = (String) map.get("sum");
		Date modified = (Date) map.get(keyLastModified ? "lastModified" : "modified");
		Integer version = (Integer) map.get("version");
		return new PageVersion(pageId, user, ip, type, summary, modified, version);
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
			res.add(buildPageFromResult(o));
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
		return _client.genericQuery(action);
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
			Page page = buildPageFromResult(mapResult);
			SearchResult sr = new SearchResult(page, score, snippet);
			searchResults.add(sr);
		}
		return searchResults;
	}
	
	public PageInfo getPageInfo(String pageId) throws DokuException{
		Object result = genericQuery("wiki.getPageInfo",pageId);
		return buildPageInfoFromResult(result);
	}
	
	public PageInfo getPageInfoVersion(String pageId, Integer timestamp) throws DokuException {
		Object[] params = new Object[]{pageId, timestamp};
		Object result = genericQuery("wiki.getPageInfoVersion", params);
		return buildPageInfoFromResult(result);
	}
	
	private PageInfo buildPageInfoFromResult(Object result) throws DokuException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) result;
		String name = (String) resMap.get("name");
		Date modified = (Date) resMap.get("modified");
		String author = (String) resMap.get("author");
		Integer version = (Integer) resMap.get("version");
		return new PageInfo(name, modified, author, version);
	}
	
	public 	List<Page> getAllPages() throws DokuException {
		Object result = genericQuery("wiki.getAllPages");
		List<Page> res = new ArrayList<Page>();
		for( Object o : (Object[]) result){
			res.add(buildPageFromResult(o));
		}
		return res;
	}
	
	public List<String> getBackLinks(String pageId) throws DokuException{
		Object result = genericQuery("wiki.getBackLinks", pageId);
		List<String> res = new ArrayList<String>();
		for(Object o : (Object[]) result){
			res.add((String) o);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private Page buildPageFromResult(Object result){
		return buildPageFromResult((Map<String, Object>) result);
	}
	
	private Page buildPageFromResult(Map<String, Object> result){
		String id = (String) result.get("id");
		Integer rev = (Integer) result.get("rev");
		Integer mtime = (Integer) result.get("mtime");
		Integer size = (Integer) result.get("size");
		return new Page(id, rev, mtime, size);
	}
	
	public String getPageHTML(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPageHTML", pageId);
	}
	
	public String getPageHTMLVersion(String pageId, Integer timestamp) throws DokuException{
		Object[] params = new Object[]{pageId, timestamp};
		return (String) genericQuery("wiki.getPageHTMLVersion", params);
	}
	
	public List<LinkInfo> listLinks(String pageId) throws DokuException {
		Object result = genericQuery("wiki.listLinks", pageId);
		List<LinkInfo> res = new ArrayList<LinkInfo>();
		for(Object o : (Object[]) result){
			@SuppressWarnings("unchecked")
			Map<String, Object> resMap = (Map<String, Object>) o;
			String type = (String) resMap.get("type");
			String page = (String) resMap.get("page");
			String href = (String) resMap.get("href");
			res.add(new LinkInfo(type, page, href));
		}
		return res;
	}

	public List<PageVersion> getRecentChanges(Integer timestamp) throws DokuException{
		Object result = genericQuery("wiki.getRecentChanges", timestamp);
		List<PageVersion> res = new ArrayList<PageVersion>();
		
		for(Object o : (Object[]) result){
			res.add(buildPageVersionFromResult(o, true));
		}
		return res;
	}
	
	public Object genericQuery(String action, Object param) throws DokuException{
		return _client.genericQuery(action, param);
	}
	
	public Object genericQuery(String action, Object[] params) throws DokuException{
		return _client.genericQuery(action, params);
	}
}
