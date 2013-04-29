package dw.xmlrpc;

/**
 * Describes a link in a wiki page
 */
public class LinkInfo {
	public enum Type {
		local,
		extern
	}

	private final Type _type;

	/**
	 * Whether it is a local or extern link
	 */
	public Type type(){
		return _type;
	}

	private final String _page;

	/**
	 * The wiki page (or the complete URL if extern link)
	 */
	public String page(){
		return _page;
	}

	private final String _href;

	/**
	 * The complete URL
	 */
	public String href(){
		return _href;
	}

	public LinkInfo(String type, String page, String href){
		this(Type.valueOf(type), page, href);
	}

	@Override
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

	@Override
	public String toString(){
		return "type:" + _type.toString()
				+ ", page:" + _page
				+ ", href:" + _href;
	}
}
