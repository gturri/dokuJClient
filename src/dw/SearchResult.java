package dw;

public class SearchResult {
	private Integer _score;
	public Integer score(){
		return _score;
	}
	
	private String _snippet;
	public String snippet(){
		return _snippet;
	}
	
	private Page _page;
	public Page page(){
		return _page;
	}
	
	public SearchResult(Page page, Integer score, String snippet){
		_score = score;
		_snippet = snippet;
		_page = page;
	}
}
