package dw.xmlrpc;

public class LinkInfo {
	public enum Type {
		local,
		extern
	}
	
	/**
	 * Local / extern
	 */
	private Type _type;
	public Type type(){
		return _type;
	}
	
	/**
	 * The wiki page (or the complete URL if extern)
	 */
	private String _page;
	public String page(){
		return _page;
	}
	
	/**
	 * The complete URL
	 */
	private String _href;
	public String href(){
		return _href;
	}
	
	public LinkInfo(String type, String page, String href){
		this(Type.valueOf(type), page, href);
	}
	
	public boolean equals(Object other){
		if ( this == other ){
			return true;
		}
		
		if ( other == null ){
			return false;
		}
		
		if ( !(other instanceof LinkInfo)){
			return false;
		}
		
		LinkInfo link = (LinkInfo) other;
		
		return _type == link._type
				&& _page.equals(link._page)
				&& _href.equals(link._href);
	}
	
	public LinkInfo(Type type, String page, String href){
		_type = type;
		_page = page;
		_href = href;
	}
	
	public String toString(){
		return "type:" + _type.toString()
				+ ", page:" + _page
				+ ", href:" + _href;
	}
}
