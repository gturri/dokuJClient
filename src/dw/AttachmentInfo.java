package dw;

import java.util.Date;

public class AttachmentInfo {
	private Integer _size;
	public Integer size(){
		return _size;
	}
	
	private Date _lastModified;
	public Date  lastModified(){
		return _lastModified;
	}
	
	public AttachmentInfo(Integer size, Date lastModified){
		_size = size;
		_lastModified = lastModified;
	}
	
	public String toString(){
		return "size:" + (_size == null ? "unknown" : _size)
				+ ", lastModified:" + (_lastModified == null ? "null" : _lastModified.toString());
	}
}
