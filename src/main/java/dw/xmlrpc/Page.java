package dw.xmlrpc;

import java.util.Date;

/**
 * Page as describe by the method getAllPage
 */
public class Page {
	private final String _id;

	/**
	 * id of the page, that is namespace + name
	 */
	public String id(){
		return _id;
	}

	private final Integer _perms;

	/**
	 * Integer denoting the permissions on the page
	 */
	public Integer perms(){
		return _perms;
	}

	private final Date _lastModified;

	/**
	 * Last modification date
	 */
	public Date lastModified(){
		return _lastModified;
	}

	private final Integer _size;

	/**
	 * Size of the page
	 */
	public Integer size(){
		return _size;
	}

	public Page(String id, Integer perms, Date lastModified, Integer size){
		if ( id == null ){
			throw new IllegalArgumentException("Can't build a Page with a null id");
		}

		_id = id;
		_perms = perms;
		_lastModified = lastModified;
		_size = size;
	}

	@Override
	public String toString(){
		return "id:" + _id
				+ ", perms:" + _perms
				+ ", lastModified:" + (_lastModified == null ? "null" : _lastModified.toString())
				+ ", size:" + _size;
	}
}
