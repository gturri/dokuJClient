package dw;

public class Page {
	private String _id;
	public String id(){
		return _id;
	}

	private String _name;
	public String name(){
		return _name;
	}
	
	private String _namespace;
	public String namespace(){
		return _namespace;
	}
	
	private Integer _revision;
	public Integer revision(){
		return _revision;
	}
	
	private Integer _mtime;
	public Integer mtime(){
		return _mtime;
	}
	
	private Integer _size;
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

}
