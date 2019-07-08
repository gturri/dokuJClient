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
	private final String _pageId;

	/**
	 * @return The page id (namespace + name)
	 */
	public String pageId(){
		return _pageId;
	}

	private final String _author;

	/**
	 * @return The author of the revision
	 */
	public String author(){
		return _author;
	}

	/**
	 * Alias of {@link #author()}
	 *
	 * Provided because this is how this fields is named by the xmlrpc query
	 * @return The page id
	 */
	public String user(){
		return _author;
	}

	private final String _ip;

	/**
	 * @return The ip who made this revision
	 */
	public String ip(){
		return _ip;
	}

	private final String _type;

	/**
	 * @return The type of revision (creation, edition, ...)
	 */
	public String type(){
		return _type;
	}

	private final String _summary;

	/**
	 * @return The summary of the revision
	 */
	public String summary(){
		return _summary;
	}

	private final Date _modified;

	/**
	 * @return The date of the revision
	 */
	public Date lastModified(){
		return _modified;
	}

	/**
	 * Alias of lastModifie
	 *
	 * Provided because it's called this way by the xmlrpc query
	 *
	 * @return The date of the revision
	 */
	public Date modified(){
		return _modified;
	}

	private final Integer _version;

	/**
	 * Version of the revision, as a timestamp
	 *
	 * Should correspond to {@link #modified()}
	 *
	 * @return The version of the revision
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
		return "pageId=" + _pageId
				+ ", author=" + _author
				+ ", ip=" + _ip
				+ ", type=" + _type
				+ ", summary=" + _summary
				+ ", modified=" + (_modified == null ? "null" : _modified.toString())
				+ ", version=" + _version;
	}
}
