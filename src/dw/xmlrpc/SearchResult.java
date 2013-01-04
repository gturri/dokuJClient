package dw.xmlrpc;

/**
 * Result of a text search on the wiki
 */
public class SearchResult {
	private String _id;
	public String id(){
		return _id;
	}
	
	private String _title;
	public String title(){
		return _title;
	}
	
	private Integer _size;
	public Integer size(){
		return _size;
	}
	
	private Integer _rev;
	public Integer rev(){
		return _rev;
	}
	
	private Integer _mtime;
	public Integer mtime(){
		return _mtime;
	}
	
	private Integer _score;

	/**
	 * Score of the result.
	 *
	 * The higher, the more relevant this result is.
	 */
	public Integer score(){
		return _score;
	}

	private String _snippet;

	/**
	 * Snippet of the page matching the search.
	 */
	public String snippet(){
		return _snippet;
	}
	
	public SearchResult(String id, String title, Integer rev, Integer mtime, Integer score, String snippet, Integer size){
		if ( id == null ){
			throw new IllegalArgumentException("Can't build a SearchResult with a null id");
		}
		
		_id = id;
		_title = title;
		_rev = rev;
		_mtime = mtime;
		_score = score;
		_snippet = snippet;
		_size = size;
	}
	
	public String toString(){
		return "id: " + _id
				+ ", title:" + (_title == null ? "null" : _title)
				+ ", rev:" + (_rev == null ? "null" : _rev)
				+ ", mtime:" + (_mtime == null ? "null" : _mtime)
				+ ", score: " + (_score == null ? "null" : _score)
				+ ", snippet: " + (_snippet == null ? "null" : _snippet)
				+ ", size: " + (_size == null ? "null" : _size);
	}
}
