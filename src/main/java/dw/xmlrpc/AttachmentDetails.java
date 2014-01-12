package dw.xmlrpc;

import java.util.Date;

/**
 * Describes a file uploaded in the wiki as getAttachments would
 */
public class AttachmentDetails {
	private final String _id;

	/**
	 * Media id (namespace + name)
	 */
	public String id(){
		return _id;
	}

	private final Boolean _isImg;

	/**
	 * Whether the file is an image or not
	 */
	public final Boolean isImg(){
		return _isImg;
	}

	private final Boolean _writable;

	/**
	 * Whether the file is writable or not
	 */
	public Boolean writable(){
		return _writable;
	}

	private final Integer _perms;

	/**
	 * Permissions of file
	 */
	public Integer perms(){
		return _perms;
	}

	private final Integer _size;

	/**
	 * Size in bytes
	 */
	public Integer size(){
		return _size;
	}

	private final Date _lastModified;

	/**
	 * Date of last modification of the file
	 */
	public final Date lastModified(){
		return _lastModified;
	}

	public AttachmentDetails(String id, Integer size, Date lastModified, Boolean isImg, Boolean writable, Integer perms){
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
