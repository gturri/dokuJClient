package dw.xmlrpc;

import java.util.Date;

/**
 * Describes a revision of a media
 */
public class MediaChange {
	private final String _id;

	/**
	 * @return The id of the media (namespace + name)
	 */
	public String id(){
		return _id;
	}

	private final Date _lastModified;

	/**
	 * @return The date of this modification of the media
	 */
	public Date lastModified(){
		return _lastModified;
	}

	private final String _author;

	/**
	 * @return The author of this modification of the media
	 */
	public String author(){
		return _author;
	}

	private final Integer _version;

	/**
	 * Version of the revision as a timestamp.
	 *
	 * Should correspond to {@link #lastModified()}
	 *
	 * @return The version of the revision as a timestamp
	 */
	public Integer version(){
		return _version;
	}

	private final Integer _perms;

	/**
	 * @return The permissions of the file
	 */
	public Integer perms(){
		return _perms;
	}

	private final Integer _size;

	/**
	 * @return The size of the file in bytes
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
