package dw.xmlrpc;

import java.util.Date;

/**
 * Describes a file uploaded in the wiki
 */
public class AttachmentInfo {
	private String _id;

	/**
	 * Media id (namespace + name)
	 */
	public String id(){
		return _id;
	}

	private Boolean _isImg;

	/**
	 * Whether the file is an image or not
	 */
	public Boolean isImg(){
		return _isImg;
	}

	private Boolean _writable;

	/**
	 * Whether the file is writable or not
	 */
	public Boolean writable(){
		return _writable;
	}

	private Integer _perms;

	/**
	 * Permissions of file
	 */
	public Integer perms(){
		return _perms;
	}

	private Integer _size;

	/**
	 * Size in bytes
	 */
	public Integer size(){
		return _size;
	}

	private Date _lastModified;

	/**
	 * Date of last modification of the file
	 */
	public Date  lastModified(){
		return _lastModified;
	}

	public AttachmentInfo(String id, Integer size, Date lastModified, Boolean isImg, Boolean writable, Integer perms){
		_id = id;
		_size = size;
		_lastModified = lastModified;
		_isImg = isImg;
		_writable = writable;
		_perms = perms;
	}

	@Override
	public String toString(){
		return "id: " + (_id == null ? "null" : _id)
				+ ", size:" + (_size == null ? "unknown" : _size)
				+ ", lastModified:" + (_lastModified == null ? "null" : _lastModified.toString())
				+ ", isImg: " + (_isImg == null ? "null" : _isImg)
				+ ", writable: " + (_writable == null ? "null" : _writable)
				+ ", perms:" + (_perms == null ? "null" : _perms);
	}
}
