package dw.xmlrpc;

import org.apache.xmlrpc.XmlRpcException;
import dw.xmlrpc.exception.*;

//! @cond

/**
 * Converts an XmlRpcException into a the most relevant DokuException.
 */
class ExceptionConverter {
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
		if ( message.equals("The requested file does not exist")){
			return new DokuDistantFileDoesntExistException(e);
		}
		if ( message.equals("File is still referenced")){
			return new DokuAttachmentStillReferenced(e);
		}
		if ( message.equals("The provided value is not a valid timestamp")){
			return new DokuInvalidTimeStampException(e);
		}
		if ( message.equals("There are no changes in the specified timeframe")){
			return new DokuInvalidTimeStampException(e);
		}

		//Won't match if the wiki's locale isn't 'en'
		if ( message.equals("Upload denied. This file extension is forbidden!")
				|| ( message.contains("The uploaded content did not match the ") && message.contains("file extension."))
				|| message.equals("File already exists. Nothing done.")){
			return new DokuAttachmentUploadException(message, e);
		}

		return new DokuUnknownException(e);
	}
//! @endcond
}
