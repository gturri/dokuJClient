package dw.xmlrpc;

import java.util.Date;

public class PageVersion {
	private String _pageId;
	public String pageId(){
		return _pageId;
	}
	
	private String _user;
	public String user(){
		return _user;
	}
	
	private String _ip;
	public String ip(){
		return _ip;
	}
	
	private String _type;
	public String type(){
		return _type;
	}
	
	private String _summary;
	public String summary(){
		return _summary;
	}
	
	private Date _modified;
	public Date modified(){
		return _modified;
	}
	
	private Integer _version;
	public Integer version(){
		return _version;
	}
	
	public PageVersion(String pageId, String user, String ip, String type, String summary, Date modified, Integer version){
		_pageId = pageId;
		_user = user;
		_ip = ip;
		_type = type;
		_summary = summary;
		_modified = modified;
		_version = version;
	}
	
	public String toString(){
		return "pageId=" + (_pageId == null ? "null" : _pageId)
				+ ", user=" + (_user == null ? "null" : _user)
				+ ", ip=" + (_ip == null ? "null" : _ip)
				+ ", type=" + (_type == null ? "null" : _type)
				+ ", summary=" + (_summary == null ? "null" : _summary)
				+ ", modified=" + (_modified == null ? "null" : _modified.toString())
				+ ", version=" + (_version == null ? "null" : _version);
	}
}
