package dw.xmlrpc;

/**
 * Page as describe by DokuWiki's xmlrpc method getPageList
 */
public class PageDW {
	private String _id;
	public String id(){
		return _id;
	}

	private Integer _size;
	public Integer size(){
		return _size;
	}

	private Integer _version;
	public Integer version(){
		return _version;
	}

	private Integer _mtime;
	public Integer mtime(){
		return _mtime;
	}

	public PageDW(String id, Integer size, Integer version, Integer mtime){
		if ( id == null ){
			throw new IllegalArgumentException("Can't build a PageDW with a null id");
		}

		_id = id;
		_size = size;
		_version = version;
		_mtime = mtime;
	}

	@Override
	public String toString(){
		return "id=" + _id
				+ ", size=" + (_size == null ? "null" : _size)
				+ ", version=" + (_version == null ? "null" : _version)
				+ ", mtime=" + (_mtime == null ? "null" : _mtime);
	}
}
