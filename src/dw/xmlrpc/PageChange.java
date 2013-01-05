package dw.xmlrpc;

import java.util.Date;

/**
 * Describe a page change, as returned by the getRecentChanges xmlrpc method
 *
 */
public class PageChange {
	private String _pageId;
	
	/**
	 * Id of the page changed
	 */
	public String pageId(){
		return _pageId;
	}
	
	/**
	 * Alias for {@link #pageId()}
	 * 
	 * This alias is provided because it is the way it is called by the xmlrpc method
	 */
	public String name(){
		return _pageId;
	}
	
	private Integer _perms;
	
	/**
	 * Integer representing the permissions on the file
	 */
	public Integer perms(){
		return _perms;
	}
	
	private Date _lastModified;
	
	/**
	 * Date of the modification
	 */
	public Date lastModified(){
		return _lastModified;
	}
	
	private Integer _size;
	public Integer size(){
		return _size;
	}
	
	private String _author;
	
	/**
	 * Author of the page
	 */
	public String author(){
		return _author;
	}
	
	private Integer _version;
	
	/**
	 * Version of the page
	 */
	public Integer version(){
		return _version;
	}
	
	public PageChange(String pageId, Integer perms, Date lastModified, Integer size, String author, Integer version){
		if ( pageId == null ){
			throw new IllegalArgumentException("Can't build a PageChange with a null pageId");
		}
		
		_pageId = pageId;
		_perms = perms;
		_lastModified = lastModified;
		_size = size;
		_author = author;
		_version = version;
	}
	
	public String toString(){
		return "pageId:" + _pageId
				+ ", perms:" + (_perms == null ? "null" : _perms)
				+ ", lastModified:" + (_lastModified == null ? "null" : _lastModified.toString())
				+ ", size: " + (_size == null ? "null" : _size)
				+ ", author: " + (_author == null ? "null" : _author)
				+ ", version: " + (_version == null ? "null" : _version);
	}
}
