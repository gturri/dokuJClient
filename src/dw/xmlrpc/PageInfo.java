package dw.xmlrpc;

import java.util.Date;

/**
 * Information about a wiki page
 */
public class PageInfo {
	private String _id;

	/**
	 * Id of the page (namespace + name)
	 */
	public String id(){
		return _id;
	}

	private Date _modified;

	/**
	 * Date of the modification of this version
	 */
	public Date modified(){
		return _modified;
	}

	private String _author;

	/**
	 * Author of this version
	 */
	public String author(){
		return _author;
	}

	private Integer _version;

	/**
	 * Version as a timestamp
	 *
	 * Should correspond to {@link #modified()}
	 */
	public Integer version(){
		return _version;
	}

	public PageInfo(String id, Date modified, String author, Integer version){
		_id = id;
		_modified = modified;
		_author = author;
		_version = version;
	}

	@Override
	public String toString(){
		return "id:" + _id
				+ ", modified:" + (_modified == null ? "null" : _modified.toString())
				+ ", author:" + _author
				+ ", version:" + _version;
	}
}
