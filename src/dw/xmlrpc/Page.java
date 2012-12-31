package dw.xmlrpc;

/**
 * Describes a page of the wiki
 */
public class Page {
	private String _id;
	
	/**
	 * id of the page, that is namespace + name
	 */
	public String id(){
		return _id;
	}

	private String _name;
	
	/**
	 * name of the page, without its namespace
	 */
	public String name(){
		return _name;
	}
	
	private String _namespace;
	
	/**
	 * namespace of the page, without its name
	 */
	public String nameSpace(){
		return _namespace;
	}
	
	private Integer _revision;
	
	/**
	 * Revision of the page as a timestamp
	 */
	public Integer revision(){
		return _revision;
	}
	
	private Integer _mtime;
	
	/**
	 * Modification time, as a timestamp
	 */
	public Integer mtime(){
		return _mtime;
	}
	
	private Integer _size;
	
	/**
	 * Size of the page
	 */
	public Integer size(){
		return _size;
	}
	
	public Page(String id){
    	this(id, null, null, null);
	}
	
	public Page(String id, Integer revision, Integer mtime, Integer size){
		_id = id;
		_revision = revision;
		_mtime = mtime;
		_size = size;
		
		_namespace = "";
		String[] names = id.split(":");
		for ( int idx=0 ; idx < names.length-1 ; idx++ ){
			_namespace += names[idx];
			if ( idx < names.length-2){
				_namespace += ":";
			}
		}
		_name = names[names.length-1];
	}
	
	public String toString(){
		return "id:" + _id
				+ ", revision:" + _revision
				+ ", mtime:" + _mtime
				+ ", size:" + _size;
	}
}
