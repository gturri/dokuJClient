package dw.xmlrpc;

import java.util.Date;

public class MediaChange {
	private String _id;
	public String id(){
		return _id;
	}
	
	private Date _lastModified;
	public Date lastModified(){
		return _lastModified;
	}
	
	private String _author;
	public String author(){
		return _author;
	}
	
	private Integer _version;
	public Integer version(){
		return _version;
	}

	private Integer _perms;
	public Integer perms(){
		return _perms;
	}
	
	private Integer _size;
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
