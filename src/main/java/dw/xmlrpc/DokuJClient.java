package dw.xmlrpc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuIncompatibleVersionException;
import dw.xmlrpc.exception.DokuMethodDoesNotExistsException;
import dw.xmlrpc.exception.DokuMisConfiguredWikiException;
import dw.xmlrpc.exception.DokuNoChangesException;
import dw.xmlrpc.exception.DokuPageDoesNotExistException;

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
	 *
	 * @param logger the logger to use
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
	 * @throws MalformedURLException If the string passed to represent a URL is malformed
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws MalformedURLException If the string passed to represent a URL is malformed
	 */
	public DokuJClient(String url) throws MalformedURLException{
		this(CoreClientFactory.build(url));
	}

    public DokuJClient(DokuJClientConfig dokuConfig) throws DokuException{
    	this(CoreClientFactory.build(dokuConfig));
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
    	boolean success = false;
    	for(int retry=0 ; retry < nbMaxRetry && !success ; retry++ ){
    		success = login(user, password);
    	}
    }

    public Boolean login(String user, String password) throws DokuException{
    	Object[] params = new Object[]{user, password};
    	return (Boolean) genericQuery("dokuwiki.login", params) && hasDokuwikiCookies();
    }

    /**
     * Uploads a file to the wiki
     *
     * @param attachmentId Id the file should have once uploaded (eg: ns1:ns2:myfile.gif)
     * @param localPath The path to the file to upload
     * @param overwrite TRUE to overwrite if a file with this id already exist on the wiki
     * @throws DokuException If an error occurs when querying the remote wiki
     * @throws IOException If an error occurs when reading the local file
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
     * @throws DokuException If an error occurs when querying the remote wiki
     * @throws IOException If an error occurs when reading the local file
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
     * @throws DokuException If an error occurs when querying the remote wiki
     */
	public void putAttachment(String attachmentId, byte[] localFile, boolean overwrite) throws DokuException{
		_attacher.putAttachment(attachmentId, localFile, overwrite);
	}

	/**
	 * Returns information about a media file
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return The information related to the file queried
	 */
	public AttachmentInfo getAttachmentInfo(String fileId) throws DokuException{
		return _attacher.getAttachmentInfo(fileId);
	}

	/**
	 * Deletes a file. Fails if the file is still referenced from any page in the wiki.
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public void deleteAttachment(String fileId) throws DokuException{
		_attacher.deleteAttachment(fileId);
	}

	/**
	 * Let download a file from the wiki
	 *
	 * @param fileId Id of the file on the wiki (eg: ns1:ns2:myfile.gif)
	 * @param localPath Where to put the file
	 * @throws IOException If an error occurs when reading the local file
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return The File retrieved
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return the data of the file, encoded in base64
	 */
	public byte[] getAttachment(String fileId) throws DokuException {
		return _attacher.getAttachment(fileId);
	}

	/**
	 * @param namespace Where to look for files
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return information about a list of media files in a given namespace
	 */
	public List<AttachmentDetails> getAttachments(String namespace) throws DokuException{
		return getAttachments(namespace, null);
	}

	/**
	 *
	 * @param namespace Where to look for files
	 * @param additionalParams Potential additional parameters directly sent to Dokuwiki.
	 *     Available parameters are:
	 *      * recursive: TRUE if also files in subnamespaces are to be included, defaults to FALSE
	 *      * pattern: an optional PREG compatible regex which has to match the file id
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return information about a list of media files in a given namespace
	 */
	public List<AttachmentDetails> getAttachments(String namespace, Map<String, Object> additionalParams) throws DokuException{
		return _attacher.getAttachments(namespace, additionalParams);
	}

	/**
	 * @return a list of recent changed media since given timestamp
	 * @param timestamp Do not return changes older than this timestamp
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public List<MediaChange> getRecentMediaChanges(Integer timestamp) throws DokuException{
		return _attacher.getRecentMediaChanges(timestamp);
	}

	/**
	 * Wrapper around {@link #getRecentMediaChanges(Integer)}
	 * @param date Do not return changes older than this date
	 * @return the list of changes to media olderthan the date queried
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public List<MediaChange> getRecentMediaChanges(Date date) throws DokuException {
		return getRecentMediaChanges((int)(date.getTime() / 1000));
	}

	/**
	 * @return the current time at the remote wiki server as Unix timestamp
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
    public Integer getTime() throws DokuException{
    	return (Integer) genericQuery("dokuwiki.getTime");
    }

    /**
     * @return the XML RPC interface version of the remote Wiki.
     *     This is DokuWiki implementation specific and independent of the supported
     *     standard API version returned by wiki.getRPCVersionSupported
     * @throws DokuException If an error occurs when querying the remote wiki
     */
    public Integer getXMLRPCAPIVersion() throws DokuException{
		return (Integer) genericQuery("dokuwiki.getXMLRPCAPIVersion");
    }

    /**
     * @return the DokuWiki version of the remote Wiki
     * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return The available versions of the wiki page requested.
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return the available versions of a Wiki page.
	 */
	public List<PageVersion> getPageVersions(String pageId, Integer offset) throws DokuException {
		boolean useFixForLegacyWiki = false;
		if ( offset > 0 ){
			if ( getXMLRPCAPIVersion() < 10 ){
				useFixForLegacyWiki = true;
				offset--;
			}
		}
		Object[] params = new Object[]{pageId, offset};
		Object[] result = (Object[]) genericQuery("wiki.getPageVersions", params);
		if ( useFixForLegacyWiki && result.length > 1 ){
			result = Arrays.copyOfRange(result, 1, result.length);
		}
		return ObjectConverter.toPageVersion(result, pageId);
	}

	/**
	 * @return the raw Wiki text for a specific revision of a Wiki page.
	 * @param pageId Id of the page (eg: ns1:ns2:mypage)
	 * @param timestamp Version of the page
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public String getPageVersion(String pageId, Integer timestamp) throws DokuException{
		Object[]params = new Object[]{pageId, timestamp};
		return (String) genericQuery("wiki.getPageVersion", params);
	}

	/**
	 * @return The list of all pages within a given namespace
	 * @param namespace Namespace to look for (eg: ns1:ns2)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public List<PageDW> getPagelist(String namespace) throws DokuException {
		return getPagelist(namespace, null);
	}

	/**
	 * @return The list of all pages within a given namespace
	 * @param namespace Namespace to look for (eg: ns1:ns2)
	 * @param options Options passed directly to dokuwiki's search_all_pages()
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @return The permission of the given wikipage.
	 * @param pageId Id of the page (eg: ns1:ns2:mypage)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public Integer aclCheck(String pageId) throws DokuException{
		Object res = _client.genericQuery("wiki.aclCheck", pageId);
		return ObjectConverter.toPerms(res);
	}

	/**
	 * @return The supported RPC API version
	 *     cf http://www.jspwiki.org/wiki/WikiRPCInterface2 for more info
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public Integer getRPCVersionSupported() throws DokuException{
		return (Integer) genericQuery("wiki.getRPCVersionSupported");
	}

	/**
	 * Allows to lock or unlock a whole bunch of pages at once.
	 * Useful when you are about to do a operation over multiple pages
	 * @param pagesToLock  Ids of pages to lock
	 * @param pagesToUnlock Ids of pages to unlock
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return Information about which lock succeeded or failed
	 */
	public LockResult setLocks(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		return _locker.setLocks(pagesToLock, pagesToUnlock);
	}

	/**
	 * Lock a page
	 * @param pageId Id of the page to lock (eg: ns1:ns2:mypage)
	 * @return TRUE the page has been successfully locked, FALSE otherwise
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public boolean lock(String pageId) throws DokuException{
		return _locker.lock(pageId).locked().contains(pageId);
	}

	/**
	 * Unlock a page
	 * @param pageId Id of the page to unlock (eg: ns1:ns2:mypage)
	 * @return TRUE the page has been successfully unlocked, FALSE otherwise
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public boolean unlock(String pageId) throws DokuException{
		return _locker.unlock(pageId).unlocked().contains(pageId);
	}

	/**
	 * @return The title of the wiki
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public String getTitle() throws DokuException{
		return (String) genericQuery("dokuwiki.getTitle");
	}

	/**
	 * Appends text to a Wiki Page.
	 * @param pageId Id of the page to edit (eg: ns1:ns2:mypage)
	 * @param rawWikiText Text to add to the current page content
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public void appendPage(String pageId, String rawWikiText, Map<String, Object> options) throws DokuException {
		if ( options == null ){
			options = new HashMap<String, Object>();
		}
		genericQuery("dokuwiki.appendPage", new Object[]{pageId, rawWikiText, options});
	}

	/**
	 * @return the raw Wiki text for a page
	 * @param pageId Id of the page to fetch (eg: ns1:ns2:mypage)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public String getPage(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPage", pageId);
	}

	/**
	 * Saves a Wiki Page
	 * @param pageId Id of the page to save
	 * @param rawWikiText Text to put
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public List<SearchResult> search(String pattern) throws DokuException{
		Object[] results = (Object[]) genericQuery("dokuwiki.search", pattern);
		return ObjectConverter.toSearchResult(results);
	}

	/**
	 * @return Information about a Wiki page
	 * @param pageId Id of the page wanted (eg: ns1:ns2:mypage)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public PageInfo getPageInfo(String pageId) throws DokuException{
		try {
			Object result = genericQuery("wiki.getPageInfo",pageId);
			return ObjectConverter.toPageInfo(result);
		} catch(DokuMisConfiguredWikiException e){
			//Because "Adora Belle" (DW-2013-05-10) seems to have a bug with this command when the page doesn't exist
			if ( ! isConfiguredToAcceptXmlRpcQueries() ){
				throw e;
			}
			throw new DokuPageDoesNotExistException(null);
		}
	}

	/**
	 * @return Information about a specific version of a Wiki page
	 * @param pageId Id of the page wanted(eg: ns1:ns2:mypage)
	 * @param timestamp version wanted
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public PageInfo getPageInfoVersion(String pageId, Integer timestamp) throws DokuException {
		Object[] params = new Object[]{pageId, timestamp};
		Object result = genericQuery("wiki.getPageInfoVersion", params);
		return ObjectConverter.toPageInfo(result);
	}

	/**
	 * @return a list of all Wiki pages in the remote Wiki
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public List<Page> getAllPages() throws DokuException {
		Object result = genericQuery("wiki.getAllPages");
		return ObjectConverter.toPage((Object[]) result);
	}

	/**
	 * @return a list of backlinks of a Wiki page
	 * @param pageId Id of the page wanted (eg: ns1:ns2:mypage)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public List<String> getBackLinks(String pageId) throws DokuException{
		Object result = genericQuery("wiki.getBackLinks", pageId);
		return ObjectConverter.toString((Object[]) result);
	}

	/**
	 * @return the rendered XHTML body of a Wiki page
	 * @param pageId Id of the wanted page (eg: ns1:ns2:mypage)
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public String getPageHTML(String pageId) throws DokuException {
		return (String) genericQuery("wiki.getPageHTML", pageId);
	}

	/**
	 * @return the rendered HTML of a specific version of a Wiki page
	 * @param pageId Id of the wanted page (eg: ns1:ns2:mypage)
	 * @param timestamp Version wanted
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public String getPageHTMLVersion(String pageId, Integer timestamp) throws DokuException{
		Object[] params = new Object[]{pageId, timestamp};
		return (String) genericQuery("wiki.getPageHTMLVersion", params);
	}

	/**
	 * @return a list of all links contained in a Wiki page
	 * @param pageId Id of the wanted page (eg: ns1:ns2:mypage)
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return The list of recent changes since a given timestamp
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
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return The list of recent changes since the given date
	 */
	public List<PageChange> getRecentChanges(Date date) throws DokuException {
		return getRecentChanges((int)(date.getTime() / 1000));
	}

	/**
	 * Tries to logoff by expiring auth cookies and the associated PHP session
	 * @throws DokuException If an error occurs when querying the remote wiki
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
	 * Only available for dokuwiki-2013-12-08 (Binky) or newer
	 * @param scope The page or namespace to which permission are added
	 * @param username The name of the user for who this permission is added
	 * @param permission The permission added. See https://www.dokuwiki.org/acl#background_info to get
	 *     the matching between the int and the permissions
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return Whether the rule was correctly added
	 */
	public boolean addAcl(String scope, String username, int permission) throws DokuException{
		try {
			return (Boolean) genericQuery("plugin.acl.addAcl", new Object[]{scope, username, permission});
		} catch (DokuMethodDoesNotExistsException e){
			throw new DokuIncompatibleVersionException("dokuwiki-2013-12-08 (Binky)");
		}
	}

	/**
	 * Only available for dokuwiki-2013-12-08 (Binky) or newer
	 * @param scope The page or namespace to which permission are added
	 * @param username The name of the user for who this permission is added
	 * @throws DokuException If an error occurs when querying the remote wiki
	 * @return Whether the rule was correctly deleted
	 */
	public boolean delAcl(String scope, String username) throws DokuException{
		try {
			return (Boolean) genericQuery("plugin.acl.delAcl", new Object[]{scope, username});
		} catch (DokuMethodDoesNotExistsException e){
			throw new DokuIncompatibleVersionException("dokuwiki-2013-12-08 (Binky)");
		}
	}

	/**
	 * Let execute any xmlrpc query without argument
	 * @param action The name of the xmlrpc method to invoke
	 * @return Whatever the xmlrpc should return, as an Object
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public Object genericQuery(String action) throws DokuException {
		return _client.genericQuery(action);
	}

	/**
	 * Let execute any xmlrpc query with one argument
	 * @param action The name of the xmlrpc method to invoke
	 * @param param The unique parameter, as an Object
	 * @return Whatever the xmlrpc should return, as an Object
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public Object genericQuery(String action, Object param) throws DokuException{
		return _client.genericQuery(action, param);
	}

	/**
	 * Let execute any xmlrpc query with an arbitrary number of arguments
	 * @param action The name of the xmlrpc method to invoke
	 * @param params The parameters, as an array of Objects
	 * @return Whatever the xmlrpc should return, as an Object
	 * @throws DokuException If an error occurs when querying the remote wiki
	 */
	public Object genericQuery(String action, Object[] params) throws DokuException{
		return _client.genericQuery(action, params);
	}

	private boolean isConfiguredToAcceptXmlRpcQueries() throws DokuException{
		try {
			getTitle();
		} catch(DokuMisConfiguredWikiException e){
			return false;
		}
		return true;
	}
}
