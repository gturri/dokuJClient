package dw.xmlrpc.exception;

import org.apache.xmlrpc.XmlRpcException;

public class ExceptionConverter {
	public static DokuException Convert(XmlRpcException e){
		String message = e.getMessage();
		if ( message.equals("The page is currently locked") ){
			return new DokuPageLockedException(e);
		}
		if ( message.equals("HTTP server returned unexpected status: Unauthorized")){
			return new DokuUnauthorizedException(e);
		}
		if ( message.equals("HTTP server returned unexpected status: Not Found")){
			String mess = "Server couldn't find the xmlrpc interface."
					+ "Make sure url looks like http[s]://server/mywiki/lib/exe/xmlrpc.php";
			return new DokuBadUrlException(mess, e);
		}
		if ( message.equals("Positive wordblock check")){
			return new DokuWordblockException(e);
		}
		if ( message.equals("HTTP server returned unexpected status: Forbidden")){
			return new DokuUnauthorizedException(e);
		}
		if ( message.equals("Could not delete file")){
			return new DokuDeleteAttachmentException(e);
		}

		return new DokuUnknownException(e);
	}
}
