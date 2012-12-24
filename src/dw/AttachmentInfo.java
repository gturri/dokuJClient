package dw;

import java.util.Date;

public class AttachmentInfo {
	/**
	 * Media id
	 */
	private String _id;
	public String id(){
		return _id;
	}

	/**
	 * true if file is an image, false otherwise
	 */
	private Boolean _isImg;
	public Boolean isImg(){
		return _isImg;
	}
	
	/**
	 * true if file is writable, false otherwise
	 */
	private Boolean _writable;
	public Boolean writable(){
		return _writable;
	}
	
	
	/**
	 * permissions of file
	 */
	private Integer _perms;
	public Integer perms(){
		return _perms;
	}

	/**
	 * size in bytes
	 */
	private Integer _size;
	public Integer size(){
		return _size;
	}
	
	private Date _lastModified;
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
	
	public String toString(){
		return "id: " + (_id == null ? "null" : _id)
				+ ", size:" + (_size == null ? "unknown" : _size)
				+ ", lastModified:" + (_lastModified == null ? "null" : _lastModified.toString())
				+ ", isImg: " + (_isImg == null ? "null" : _isImg)
				+ ", writable: " + (_writable == null ? "null" : _writable)
				+ ", perms:" + (_perms == null ? "null" : _perms);
	}
}
