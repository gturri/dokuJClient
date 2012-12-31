package dw.xmlrpc;

import java.util.Date;

public class PageInfo {
	private String _id;
	public String id(){
		return _id;
	}
	
	private Date _modified;
	public Date modified(){
		return _modified;
	}
	
	private String _author;
	public String author(){
		return _author;
	}
	
	private Integer _version;
	public Integer version(){
		return _version;
	}
	
	public PageInfo(String id, Date modified, String author, Integer version){
		_id = id;
		_modified = modified;
		_author = author;
		_version = version;
	}
	
	public String toString(){
		return "id:" + _id
				+ ", modified:" + (_modified == null ? "null" : _modified.toString())
				+ ", author:" + _author
				+ ", version:" + _version;
	}
}
