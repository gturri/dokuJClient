package dw.xmlrpc;

/**
 * Page as describe by DokuWiki's xmlrpc method getPageList
 */
public class PageDW {
	private final String _id;
	public String id(){
		return _id;
	}

	private final Integer _size;
	public Integer size(){
		return _size;
	}

	private final Integer _version;
	public Integer version(){
		return _version;
	}

	private final Integer _mtime;
	public Integer mtime(){
		return _mtime;
	}

	private final String _hash;
	public String hash(){
		return _hash;
	}

	public PageDW(String id, Integer size, Integer version, Integer mtime, String hash){
		if ( id == null ){
			throw new IllegalArgumentException("Can't build a PageDW with a null id");
		}

		_id = id;
		_size = size;
		_version = version;
		_mtime = mtime;
		_hash = hash;
	}

	@Override
	public String toString(){
		return "id=" + _id
				+ ", size=" + (_size == null ? "null" : _size)
				+ ", version=" + (_version == null ? "null" : _version)
				+ ", mtime=" + (_mtime == null ? "null" : _mtime)
				+ ", hash=" + (_hash == null ? "null" : _hash);
	}
}
