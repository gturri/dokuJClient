package dw.xmlrpc;

import java.util.Date;

/**
 * Describes a revision of a media
 */
public class MediaChange {
	private final String _id;

	/**
	 * Id of the media (namespace + name)
	 */
	public String id(){
		return _id;
	}

	private final Date _lastModified;

	/**
	 * Date of this modification of the media
	 */
	public Date lastModified(){
		return _lastModified;
	}

	private final String _author;

	/**
	 * Author of this modification of the media
	 */
	public String author(){
		return _author;
	}

	private final Integer _version;

	/**
	 * Version of the revision as a timestamp.
	 *
	 * Should correspond to {@link #lastModified()}
	 */
	public Integer version(){
		return _version;
	}

	private final Integer _perms;

	/**
	 * Permissions of the file
	 */
	public Integer perms(){
		return _perms;
	}

	private final Integer _size;

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

	@Override
	public String toString(){
		return "id=" + _id
				+ ", lastModified=" + (_lastModified == null ? "null" : _lastModified.toString())
				+ ", author=" + _author
				+ ", version=" + _version
				+ ", perms=" + _perms
				+ ", size=" + _size;
	}
}
