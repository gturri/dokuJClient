package dw.xmlrpc;

import dw.xmlrpc.exception.DokuException;

/**
 * Result of a text search on the wiki
 */
public class SearchResult {
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

	private Page _page;

	/**
	 * Corresponding page
	 */
	public Page page(){
		return _page;
	}
	
	public SearchResult(Page page, Integer score, String snippet) throws DokuException{
		if ( page == null ){
			throw new DokuException("Can't build a SearchResult with a null page");
		}

		_score = score;
		_snippet = snippet;
		_page = page;
	}

	public String toString(){
		return "page: " + (_page.id() == null ? "null" : _page.id())
				+ ", score: " + (_score == null ? "null" : _score)
				+ ", snippet: " + (_snippet == null ? "null" : _snippet);
	}
}
