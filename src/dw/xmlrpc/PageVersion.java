package dw.xmlrpc;

import java.util.Date;

/**
 * Describe a version of a page.
 *
 * Dokuwiki may not always return every field (because the information is already
 * missing on Dokuwiki's side or because it isn't meant to give such field for a
 * given query).
 * Hence it's better to always check if a given field isn't null before using it.
 */
public class PageVersion {
	private String _pageId;

	/**
	 * Page id (namespace + name)
	 */
	public String pageId(){
		return _pageId;
	}

	private String _author;

	/**
	 * Author of the revision
	 */
	public String author(){
		return _author;
	}

	/**
	 * Alias of {@link #author()}
	 *
	 * Provided because this is how this fields is names by the xmlrpc query
	 */
	public String user(){
		return _author;
	}

	private String _ip;

	/**
	 * Ip who made this revision
	 */
	public String ip(){
		return _ip;
	}

	private String _type;

	/**
	 * Type of revision (creation, edition, ...)
	 */
	public String type(){
		return _type;
	}

	private String _summary;

	/**
	 * Summary of the revision
	 */
	public String summary(){
		return _summary;
	}

	private Date _modified;

	/**
	 * Date of the revision
	 */
	public Date lastModified(){
		return _modified;
	}

	/**
	 * Alias of lastModifie
	 *
	 * Provided because it's called this way by the xmlrpc query
	 */
	public Date modified(){
		return _modified;
	}

	private Integer _version;

	/**
	 * Version of the revision, as a timestamp
	 *
	 * Should correspond to {@link #modified()}
	 */
	public Integer version(){
		return _version;
	}

	public PageVersion(String pageId, String author, String ip, String type, String summary, Date modified, Integer version){
		if ( pageId == null ){
			throw new IllegalArgumentException("Can't build a PageVersion with a null id");
		}

		_pageId = pageId;
		_author = author;
		_ip = ip;
		_type = type;
		_summary = summary;
		_modified = modified;
		_version = version;
	}

	@Override
	public String toString(){
		return "pageId=" + (_pageId == null ? "null" : _pageId)
				+ ", author=" + (_author == null ? "null" : _author)
				+ ", ip=" + (_ip == null ? "null" : _ip)
				+ ", type=" + (_type == null ? "null" : _type)
				+ ", summary=" + (_summary == null ? "null" : _summary)
				+ ", modified=" + (_modified == null ? "null" : _modified.toString())
				+ ", version=" + (_version == null ? "null" : _version);
	}
}
