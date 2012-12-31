package dw.xmlrpc;

import java.util.Date;

/**
 * Describes a revision of a media
 */
public class MediaChange {
	private String _id;
	
	/**
	 * Id of the media (namespace + name)
	 */
	public String id(){
		return _id;
	}
	
	private Date _lastModified;
	
	/**
	 * Date of this modification of the media
	 */
	public Date lastModified(){
		return _lastModified;
	}
	
	private String _author;
	
	/**
	 * Author of this modification of the media 
	 */
	public String author(){
		return _author;
	}
	
	private Integer _version;
	
	/**
	 * Version of the revision as a timestamp.
	 * 
	 * Should correspond to {@link #lastModified()}
	 */
	public Integer version(){
		return _version;
	}

	private Integer _perms;
	
	/**
	 * Permissions of the file
	 */
	public Integer perms(){
		return _perms;
	}
	
	private Integer _size;
	
	/**
	 * Size of the file in bytes
	 */
	public Integer size(){
		return _size;
	}

	public MediaChange(String id, Date lastModified, String author, Integer version, Integer perms, Integer size){
		_id = id;
		_lastModified = lastModified;
		_author = author;
		_version = version;
		_perms = perms;
		_size = size;
	}
	
	public String toString(){
		return "id=" + (_id == null ? "null" : _id)
				+ ", lastModified=" + (_lastModified == null ? "null" : _lastModified.toString())
				+ ", author=" + (_author == null ? "null" : _author)
				+ ", version=" + (_version == null ? "null" : _version)
				+ ", perms=" + (_perms == null ? "null" : _perms)
				+ ", size=" + (_size == null ? "null" : _size);
	}
}
