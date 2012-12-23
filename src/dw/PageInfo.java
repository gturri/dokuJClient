package dw;

import java.util.Date;

public class PageInfo {
	private String _name;
	public String name(){
		return _name;
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
	
	public PageInfo(String name, Date modified, String author, Integer version){
		_name = name;
		_modified = modified;
		_author = author;
		_version = version;
	}
	
	public String toString(){
		return "name:" + _name
				+ ", modified:" + (_modified == null ? "null" : _modified.toString())
				+ ", author:" + _author
				+ ", version:" + _version;
	}
}
