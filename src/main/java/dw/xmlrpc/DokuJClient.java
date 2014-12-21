package dw.xmlrpc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuMethodDoesNotExistsException;
import dw.xmlrpc.exception.DokuNoChangesException;

/**
 * Main public class to actually make an xmlrpc query
 *
 * Instantiate one such client for a given wiki and a given user, then make
 * xmlrpc query using its methods.
 *
 * Most methods may throw DokuException because many things can go wrong
 * (bad url, wrong credential, no network, unreachable server, ...), so you may
 * want to make sure you handle them correcty
 */
public class DokuJClient {
	private final CoreClient _client;
	private final Locker _locker;
	private final Attacher _attacher;
	private Logger _logger;

	private final String COOKIE_PREFIX = "DW";

	/**
	 * Let override the default Logger
	 */
	public void setLogger(Logger logger){
		_logger = logger;
		_client.setLogger(logger);
	}

	/**
	 * Instantiate a client for the given user on the given wiki
	 *
	 * The wiki should be configured in a way to let this user access the
	 * xmlrpc interface
	 *
	 * @param url Should looks like http[s]://server/mywiki/lib/exe/xmlrpc.php
	 * @param user Login of the user
	 * @param password Password of the user
	 * @throws MalformedURLException
	 * @throws DokuException
	 */
    public DokuJClient(String url, String user, String password) throws MalformedURLException, DokuException{
    	this(url);
    	loginWithRetry(user, password, 2);
	}

	/**
	 * Instantiate a client for an anonymous user on the given wiki
	 *
	 * Likely to be unsuitable for most wiki since anonymous user are often
	 * not authorized to use the xmlrpc interface
	 *
	 * @param url Should looks like http[s]://server/mywiki/lib/exe/xmlrpc.php
	 * @throws MalformedURLException
	 */
	public DokuJClient(String url) throws MalformedURLException{
		this(CoreClientFactory.Build(url));
	}

    public DokuJClient(DokuJClientConfig dokuConfig) throws DokuException{
    	this(CoreClientFactory.Build(dokuConfig));
    	if ( dokuConfig.user() != null){
    		loginWithRetry(dokuConfig.user(), dokuConfig.password(), 2);
    	}
    }

    private DokuJClient(CoreClient client){
    	_client = client;
    	_locker = new Locker(_client);
    	_attacher = new Attacher(_client);
    	Logger logger = Logger.getLogger(DokuJClient.class.toString());
    	setLogger(logger);
    }

    public boolean hasDokuwikiCookies(){
    	for(String cookieKey : cookies().keySet()){
    		if ( cookieKey.startsWith(COOKIE_PREFIX) ){
    			return true;
    		}
    	}
    	return false;
    }

    public Map<String, String> cookies(){
    	return _client.cookies();
    }

    //Because it's been observed that some hosting services sometime mess up a bit with cookies...
    private void loginWithRetry(String user, String password, int nbMaxRetry) throws DokuException {
    	login(user, password);
    	for(int retry=1 ; retry < nbMaxRetry && !hasDokuwikiCookies() ; retry++ ){
    		login(user, password);
    	}
    }

    public void login(String user, String password) throws DokuException{
    	Object[] params = new Object[]{user, password};
       	genericQuery("dokuwiki.login", params);
    }

    /**
     * Uploads a file to the wiki
     *
     * @param attachmentId Id the file should have once uploaded (eg: ns1:ns2:myfile.gif)
     * @param localPath The path to the file to upload
     * @param overwrite TRUE to overwrite if a file with this id already exist on the wiki
     * @throws IOException
     * @throws DokuException
     */
	public void putAttachment(String attachmentId, String localPath, boolean overwrite) throws IOException, DokuException{
		putAttachment(attachmentId, new File(localPath), overwrite);
	}

    /**
     * Uploads a file to the wiki
     *
     * @param attachmentId Id the file should have once uploaded (eg: ns1:ns2:myfile.gif)
     * @param localFile The file to upload
     * @param overwrite TRUE to overwrite if a file with this id already exist on the wiki
     * @throws IOException
     * @throws DokuException
     */
	public void putAttachment(String attachmentId, File localFile, boolean overwrite) throws IOException, DokuException{
		putAttachment(attachmentId, _attacher.serializeFile(localFile), overwrite);
	}

    /**
     * Uploads a file to the wiki
     *
     * @param attachmentId Id the file should have once uploaded (eg: ns1:ns2:myfile.gif)
     * @param localFile base64 encoded file
     * @param overwrite TRUE to overwrite if a file with this id already exist on the wiki
     * @throws IOException
     * @throws DokuException
     */
	public void putAttachment(String attachmentId, byte[] localFile, boolean overwrite) throws DokuException{
		_attacher.putAttachment(attachmentId, localFile, overwrite);
	}

	/**
	 * Returns information about a media file
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @throws DokuException
	 */
	public AttachmentInfo getAttachmentInfo(String fileId) throws DokuException{
		return _attacher.getAttachmentInfo(fileId);
	}

	/**
	 * Deletes a file. Fails if the file is still referenced from any page in the wiki.
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @throws DokuException
	 */
	public void deleteAttachment(String fileId) throws DokuException{
		_attacher.deleteAttachment(fileId);
	}

	/**
	 * Let download a file from the wiki
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @param localPath Where to put the file
	 * @throws DokuException
	 * @throws IOException
	 */
	public File getAttachment(String fileId, String localPath) throws DokuException, IOException{
		byte[] b = getAttachment(fileId);
		File f = new File(localPath);
		_attacher.deserializeFile(b, f);
		return f;

	}

	/**
	 * Let download a file from the wiki
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @throws DokuException
	 * @return the data of the file, encoded in base64
	 */
	public byte[] getAttachment(String fileId) throws DokuException {
		return _attacher.getAttachment(fileId);
	}

	/**
	 * Returns information about a list of media files in a given namespace
	 *
	 * @param namespace Where to look for files
	 * @throws DokuException
	 */
	public List<AttachmentDetails> getAttachments(String namespace) throws DokuException{
		return getAttachments(namespace, null);
	}

	/**
	 * Returns information about a list of media files in a given namespace
	 *
	 * @param namespace Where to look for files
	 * @param additionalParams Potential additional parameters directly sent to Dokuwiki.
	 * Available parameters are:
	 *  * recursive: TRUE if also files in subnamespaces are to be included, defaults to FALSE
	 *  * pattern: an optional PREG compatible regex which has to match the file id
	 * @throws DokuException
	 */
	public List<AttachmentDetails> getAttachments(String namespace, Map<String, Object> additionalParams) throws DokuException{
		return _attacher.getAttachments(namespace, additionalParams);
	}

	/**
	 * Returns a list of recent changed media since given timestamp
	 * @param timestamp
	 * @throws DokuException
	 */
	public List<MediaChange> getRecentMediaChanges(Integer timestamp) throws DokuException{
		return _attacher.getRecentMediaChanges(timestamp);
	}

	/**
	 * Wrapper around {@link #getRecentMediaChanges(Integer)}
	 * @param date Do not return chances older than this date
	 */
	public List<MediaChange> getRecentMediaChanges(Date date) throws DokuException {
		return getRecentMediaChanges((int)(date.getTime() / 1000));
	}

	/**
	 * Returns the current time at the remote wiki server as Unix timestamp
	 * @throws DokuException
	 */
    public Integer getTime() throws DokuException{
    	return (Integer) genericQuery("dokuwiki.getTime");
    }

    /**
     * Returns the XML RPC interface version of the remote Wiki.
     * This is DokuWiki implementation specific and independent of the supported
     * standard API version returned by wiki.getRPCVersionSupported
     * @throws DokuException
     */
    public Integer getXMLRPCAPIVersion() throws DokuException{
		return (Integer) genericQuery("dokuwiki.getXMLRPCAPIVersion");
    }

    /**
     * Returns the DokuWiki version of the remote Wiki
     * @throws DokuException
     */
	public String getVersion() throws DokuException{
		return (String) genericQuery("dokuwiki.getVersion");
	}

	/**
	 * Returns the available versions of a Wiki page.
	 *
	 * The number of pages in the result is controlled via the "recent" configuration setting of the wiki.
	 *
	 * @param pageId Id of the page (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public List<PageVersion> getPageVersions(String pageId) throws DokuException {
		return getPageVersions(pageId, 0);
	}

	/**
	 * Returns the available versions of a Wiki page.
	 *
	 * The number of pages in the result is controlled via the recent configuration setting of the wiki.
	 *
	 * @param pageId Id of the page (eg: ns1:ns2:mypage)
	 * @param offset Can be used to list earlier versions in the history.
	 * @throws DokuException
	 */
	public List<PageVersion> getPageVersions(String pageId, Integer offset) throws DokuException {
		Object[] params = new Object[]{pageId, offset};
		Object result = genericQuery("wiki.getPageVersions", params);
		return ObjectConverter.toPageVersion((Object[]) result, pageId);
	}

	/**
	 * Returns the raw Wiki text for a specific revision of a Wiki page.
	 * @param pageId Id of the page (eg: ns1:ns2:mypage)
	 * @param timestamp Version of the page
	 * @throws DokuException
	 */
	public String getPageVersion(String pageId, Integer timestamp) throws DokuException{
		Object[]params = new Object[]{pageId, timestamp};
		return (String) genericQuery("wiki.getPageVersion", params);
	}

	/**
	 * Lists all pages within a given namespace
	 * @param namespace Namespace to look for (eg: ns1:ns2)
	 * @throws DokuException
	 */
	public List<PageDW> getPagelist(String namespace) throws DokuException {
		return getPagelist(namespace, null);
	}

	/**
	 * Lists all pages within a given namespace
	 * @param namespace Namespace to look for (eg: ns1:ns2)
	 * @param options Options passed directly to dokuwiki's search_all_pages()
	 * @throws DokuException
	 */
	public List<PageDW> getPagelist(String namespace, Map<String, Object> options) throws DokuException {
		List<Object> params = new ArrayList<Object>();
		params.add(namespace);
		params.add(ensureWeComputeThePageHash(options));

		Object result = genericQuery("dokuwiki.getPagelist", params.toArray());
		return ObjectConverter.toPageDW((Object[]) result);
	}

	private Map<String, Object> ensureWeComputeThePageHash(Map<String, Object> initialOptions){
		Map<String, Object> result;
		if ( initialOptions == null ){
			result = new HashMap<String, Object>();
		} else {
			result = new HashMap<String, Object>(initialOptions);
		}
		if ( !result.containsKey("hash") ){
			result.put("hash", true);
		}
		return result;
	}

	/**
	 * Returns the permission of the given wikipage.
	 * @param pageId Id of the page (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public Integer aclCheck(String pageId) throws DokuException{
		Object res = _client.genericQuery("wiki.aclCheck", pageId);
		return ObjectConverter.toPerms(res);
	}

	/**
	 * Returns the supported RPC API version
	 *
	 * cf http://www.jspwiki.org/wiki/WikiRPCInterface2 for more info
	 * @throws DokuException
	 */
	public Integer getRPCVersionSupported() throws DokuException{
		return (Integer) genericQuery("wiki.getRPCVersionSupported");
	}

	/**
	 * Allows to lock or unlock a whole bunch of pages at once.
	 * Useful when you are about to do a operation over multiple pages
	 * @param pagesToLock  Ids of pages to lock
	 * @param pagesToUnlock Ids of pages to unlock
	 * @throws DokuException
	 */
	public LockResult setLocks(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		return _locker.setLocks(pagesToLock, pagesToUnlock);
	}

	/**
	 * Lock a page
	 * @param pageId Id of the page to lock (eg: ns1:ns2:mypage)
	 * @return TRUE the page has been successfully locked, FALSE otherwise
	 * @throws DokuException
	 */
	public boolean lock(String pageId) throws DokuException{
		return _locker.lock(pageId).locked().contains(pageId);
	}

	/**
	 * Unlock a page
	 * @param pageId Id of the page to unlock (eg: ns1:ns2:mypage)
	 * @return TRUE the page has been successfully unlocked, FALSE otherwise
	 * @throws DokuException
	 */
	public boolean unlock(String pageId) throws DokuException{
		return _locker.unlock(pageId).unlocked().contains(pageId);
	}

	/**
	 * Returns the title of the wiki
	 * @throws DokuException
	 */
	public String getTitle() throws DokuException{
		return (String) genericQuery("dokuwiki.getTitle");
	}

	/**
	 * Appends text to a Wiki Page.
	 * @param pageId Id of the page to edit (eg: ns1:ns2:mypage)
	 * @param rawWikiText Text to add to the current page content
	 * @throws DokuException
	 */
	public void appendPage(String pageId, String rawWikiText) throws DokuException {
		appendPage(pageId, rawWikiText, null);
	}

	/**
	 * Appends text to a Wiki Page.
	 * @param pageId Id of the page to edit (eg: ns1:ns2:mypage)
	 * @param rawWikiText Text to add to the current page content
	 * @param summary A summary of the modification
	 * @param minor Whether it's a minor modification
	 * @throws DokuException
	 */
	public void appendPage(String pageId, String rawWikiText, String summary, boolean minor) throws DokuException {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("sum",  summary);
		options.put("minor", minor);

		appendPage(pageId, rawWikiText, options);
	}

	/**
	 * Appends text to a Wiki Page.
	 * @param pageId Id of the page to edit (eg: ns1:ns2:mypage)
	 * @param rawWikiText Text to add to the current page content
	 * @param options Options passed to Dokuwiki. ie: 'sum' and/or 'minor'
	 * @throws DokuException
	 */
	public void appendPage(String pageId, String rawWikiText, Map<String, Object> options) throws DokuException {
		if ( options == null ){
			options = new HashMap<String, Object>();
		}
		genericQuery("dokuwiki.appendPage", new Object[]{pageId, rawWikiText, options});
	}

	/**
	 * Returns the raw Wiki text for a page
	 * @param pageId Id of the page to fetch (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public String getPage(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPage", pageId);
	}

	/**
	 * Saves a Wiki Page
	 * @param pageId Id of the page to save
	 * @param rawWikiText Text to put
	 * @throws DokuException
	 */
	public void putPage(String pageId, String rawWikiText)throws DokuException {
		putPage(pageId, rawWikiText, null);
	}

	/**
	 * Saves a Wiki Page
	 * @param pageId Id of the page to save
	 * @param rawWikiText Text to put
	 * @param summary Summary of the edition
	 * @param minor Whether it's a minor edition
	 * @throws DokuException
	 */
	public void putPage(String pageId, String rawWikiText, String summary, boolean minor) throws DokuException{
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("sum",  summary);
		options.put("minor", minor);
		putPage(pageId, rawWikiText, options);
	}

	/**
	 * Saves a Wiki Page
	 * @param pageId Id of the page to save
	 * @param rawWikiText Text to put
	 * @param options Options passed to Dokuwiki. ie: 'sum' and/or 'minor'	 * @throws DokuException
	 */
	public void putPage(String pageId, String rawWikiText, Map<String, Object> options)throws DokuException {
		if (options == null){
			options = new HashMap<String, Object>();
		}
		genericQuery("wiki.putPage", new Object[]{pageId, rawWikiText, options});
	}

	/**
	 * Performs a fulltext search
	 * @param pattern A query string as described on https://www.dokuwiki.org/search
	 * @return Matching pages. Snippets are provided for the first 15 results.
	 * @throws DokuException
	 */
	public List<SearchResult> search(String pattern) throws DokuException{
		Object[] results = (Object[]) genericQuery("dokuwiki.search", pattern);
		return ObjectConverter.toSearchResult(results);
	}

	/**
	 * Returns information about a Wiki page
	 * @param pageId Id of the page wanted (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public PageInfo getPageInfo(String pageId) throws DokuException{
		Object result = genericQuery("wiki.getPageInfo",pageId);
		return ObjectConverter.toPageInfo(result);
	}

	/**
	 * Returns information about a specific version of a Wiki page
	 * @param pageId Id of the page wanted(eg: ns1:ns2:mypage)
	 * @param timestamp version wanted
	 * @throws DokuException
	 */
	public PageInfo getPageInfoVersion(String pageId, Integer timestamp) throws DokuException {
		Object[] params = new Object[]{pageId, timestamp};
		Object result = genericQuery("wiki.getPageInfoVersion", params);
		return ObjectConverter.toPageInfo(result);
	}

	/**
	 * Returns a list of all Wiki pages in the remote Wiki
	 * @throws DokuException
	 */
	public 	List<Page> getAllPages() throws DokuException {
		Object result = genericQuery("wiki.getAllPages");
		return ObjectConverter.toPage((Object[]) result);
	}

	/**
	 * Returns a list of backlinks of a Wiki page
	 * @param pageId Id of the page wanted (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public List<String> getBackLinks(String pageId) throws DokuException{
		Object result = genericQuery("wiki.getBackLinks", pageId);
		return ObjectConverter.toString((Object[]) result);
	}

	/**
	 * Returns the rendered XHTML body of a Wiki page
	 * @param pageId Id of the wanted page (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public String getPageHTML(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPageHTML", pageId);
	}

	/**
	 * Returns the rendered HTML of a specific version of a Wiki page
	 * @param pageId Id of the wanted page (eg: ns1:ns2:mypage)
	 * @param timestamp Version wanted
	 * @throws DokuException
	 */
	public String getPageHTMLVersion(String pageId, Integer timestamp) throws DokuException{
		Object[] params = new Object[]{pageId, timestamp};
		return (String) genericQuery("wiki.getPageHTMLVersion", params);
	}

	/**
	 * Returns a list of all links contained in a Wiki page
	 * @param pageId Id of the wanted page (eg: ns1:ns2:mypage)
	 * @throws DokuException
	 */
	public List<LinkInfo> listLinks(String pageId) throws DokuException {
		Object result = genericQuery("wiki.listLinks", pageId);
		return ObjectConverter.toLinkInfo((Object[]) result);
	}

	/**
	 * Returns a list of recent changes since a given timestamp
	 *
	 * According to Dokuwiki documentation (https://www.dokuwiki.org/recent_changes):
	 *
	 * * Only the most recent change for each page is listed, regardless of how many times that page was changed.
	 * * The number of changes shown per page is controlled by the "recent" setting.
	 * * Users are only shown pages to which they have read access
	 *
	 * @param timestamp Do not return changes older than this timestamp
	 * @throws DokuException
	 */
	public List<PageChange> getRecentChanges(Integer timestamp) throws DokuException{
		Object result;
		try {
			result = genericQuery("wiki.getRecentChanges", timestamp);
		} catch (DokuNoChangesException e){
			return new ArrayList<PageChange>();
		}

		Object[] pageChanges;
		try {
			pageChanges = (Object[]) result;
		} catch (ClassCastException e){
			//It likely happens when there are no changes, with only a few versions of DW
			//(newer versions yield a DokuNoChangesException instead)
			//Hence it might be enough to just return an empty list... but in doubt I'd rather cast
			@SuppressWarnings("unchecked")
			Map<String, Object> pageChangesMap = (Map<String, Object>) result;
			pageChanges = pageChangesMap.values().toArray();
		}
		return ObjectConverter.toPageChange(pageChanges);
	}

	/**
	 * Wrapper around {@link #getRecentChanges(Integer)}
	 * @param date Do not return changes older than this date
	 */
	public List<PageChange> getRecentChanges(Date date) throws DokuException {
		return getRecentChanges((int)(date.getTime() / 1000));
	}

	/**
	 * Tries to logoff by expiring auth cookies and the associated PHP session
	 */
	public void logoff() throws DokuException {
		try {
			_client.genericQuery("dokuwiki.logoff");
		} catch(DokuMethodDoesNotExistsException e){
			if (_logger != null){
				_logger.log(Level.WARNING,
						"This Dokuwiki instance doesn't support 'logoff' (api version < 9). " +
						"We're clearing the cookies of this client, but we can't destroy the server side php session");
			}
		}
		_client.clearCookies();
	}

	/**
	 * Let execute any xmlrpc query without argument
	 * @param action The name of the xmlrpc method to invoke
	 * @return Whatever the xmlrpc should return, as an Object
	 * @throws DokuException
	 */
	public Object genericQuery(String action) throws DokuException {
		return _client.genericQuery(action);
	}

	/**
	 * Let execute any xmlrpc query with one argument
	 * @param action The name of the xmlrpc method to invoke
	 * @param param The unique parameter, as an Object
	 * @return Whatever the xmlrpc should return, as an Object
	 * @throws DokuException
	 */
	public Object genericQuery(String action, Object param) throws DokuException{
		return _client.genericQuery(action, param);
	}

	/**
	 * Let execute any xmlrpc query with an arbitrary number of arguments
	 * @param action The name of the xmlrpc method to invoke
	 * @param params The parameters, as an array of Objects
	 * @return Whatever the xmlrpc should return, as an Object
	 * @throws DokuException
	 */
	public Object genericQuery(String action, Object[] params) throws DokuException{
		return _client.genericQuery(action, params);
	}
}
