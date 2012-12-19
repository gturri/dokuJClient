package dw.exception;

import org.apache.xmlrpc.XmlRpcException;

@SuppressWarnings("serial")
public class DokuUnknownException extends DokuException {

	private XmlRpcException _innerException = null;
	
	public DokuUnknownException() {	}

	public DokuUnknownException(XmlRpcException e){
		super(e.getMessage());
		_innerException = e;
	}
	
	public XmlRpcException innerException(){
		return _innerException;
	}
}
