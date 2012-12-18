package dw.exception;

import org.apache.xmlrpc.XmlRpcException;

public class ExceptionConverter {
	public static DokuException Convert(XmlRpcException e){
		return new DokuUnknownException(e);
	}
}
