package dw.exception;

import org.apache.xmlrpc.XmlRpcException;

public class ExceptionConverter {
	public static DokuException Convert(XmlRpcException e){
		String message = e.getMessage();
		if ( message.equals("The page is currently locked") ){
			return new DokuPageLockedException(e);
		}
		return new DokuUnknownException(e);
	}
}
