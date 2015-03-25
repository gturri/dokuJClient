package dw.xmlrpc;

import org.xml.sax.SAXParseException;

import de.timroes.axmlrpc.XMLRPCException;
import dw.xmlrpc.exception.DokuAttachmentStillReferenced;
import dw.xmlrpc.exception.DokuAttachmentUploadException;
import dw.xmlrpc.exception.DokuBadUrlException;
import dw.xmlrpc.exception.DokuDeleteAttachmentException;
import dw.xmlrpc.exception.DokuDistantFileDoesntExistException;
import dw.xmlrpc.exception.DokuEmptyNewPageException;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuInvalidTimeStampException;
import dw.xmlrpc.exception.DokuMethodDoesNotExistsException;
import dw.xmlrpc.exception.DokuMisConfiguredWikiException;
import dw.xmlrpc.exception.DokuNoChangesException;
import dw.xmlrpc.exception.DokuPageDoesNotExistException;
import dw.xmlrpc.exception.DokuPageLockedException;
import dw.xmlrpc.exception.DokuTimeoutException;
import dw.xmlrpc.exception.DokuUnauthorizedException;
import dw.xmlrpc.exception.DokuUnknownException;
import dw.xmlrpc.exception.DokuWordblockException;

//! @cond

/**
 * Converts an XmlRpcException into a the most relevant DokuException.
 */
class ExceptionConverter {
	public static DokuException Convert(XMLRPCException e, String url, String action){
		String message = e.getMessage();
		if ( message.contains("The page is currently locked") ){
			return new DokuPageLockedException(e);
		}
		if ( message.contains("The XMLRPC call timed out.")){
			return new DokuTimeoutException(e);
		}
		if ( message.contains("not authorized to call method")
				|| message.contains("forbidden to call the method")
				|| message.contains("don't have permissions to")
				|| message.contains("You are not allowed to") //for DW up to 2012-01-25b
				|| message.contains("You don't have permissions to delete files") //for DW up to 2012-01-25b
				){
			return new DokuUnauthorizedException(e);
		}
		if ( message.contains("java.io.FileNotFoundException")
				|| message.contains("The server responded with a http 301 or 302 status code")){
			return buildGenericBadUrlException(e);
		}
		if ( e.getCause() != null && e.getCause().getClass() == java.net.UnknownHostException.class ){
			String mess = "Host doesn't exist. Check url";
			return new DokuBadUrlException(mess, e);
		}

		if ( message.contains("Positive wordblock check")){
			return new DokuWordblockException(e);
		}
		if ( message.contains("HTTP server returned unexpected status: Forbidden")){
			return new DokuUnauthorizedException(e);
		}
		if ( message.contains("Could not delete file")){
			return new DokuDeleteAttachmentException(e);
		}
		if ( message.contains("The requested file does not exist")){
			return new DokuDistantFileDoesntExistException(e);
		}
		if ( message.contains("The requested page does not exist")){
			return new DokuPageDoesNotExistException(e);
		}
		if ( message.contains("File is still referenced")){
			return new DokuAttachmentStillReferenced(e);
		}
		if ( message.contains("The provided value is not a valid timestamp")){
			return new DokuInvalidTimeStampException(e);
		}
		if ( message.contains("There are no changes in the specified timeframe")){
			return new DokuNoChangesException(e);
		}
		if ( message.contains("Refusing to write an empty new wiki page")){
			return new DokuEmptyNewPageException(e);
		}
		if ((message.contains("requested method") && message.contains("not specified"))
				|| message.contains("Method does not exist")){
			return new DokuMethodDoesNotExistsException("Method does not exists: " + action, e);
		}

		//Won't match if the wiki's locale isn't 'en'
		if ( message.contains("Upload denied. This file extension is forbidden!")
				|| ( message.contains("The uploaded content did not match the ") && message.contains("file extension."))
				|| message.contains("File already exists. Nothing done.")){
			return new DokuAttachmentUploadException(message, e);
		}

		if ( e.getCause() != null && e.getCause().getClass() == SAXParseException.class){
			return new DokuMisConfiguredWikiException("The wiki doesn't seem to be configured to accept incoming xmlrpc requests." +
					" Check the 'remote' option in Dokuwiki's configuration manager.", e);
		}

		//If we reach this point, we don't know what went wrong.
		//We try a final educated guess before giving up
		if ( ! url.endsWith("/lib/exe/xmlrpc.php") ){
			return buildGenericBadUrlException(e);
		}

		return new DokuUnknownException(e);
	}

	private static DokuBadUrlException buildGenericBadUrlException(Throwable e){
		String mess = "Couldn't find the xmlrpc interface. Make sure url looks like http[s]://server/mywiki/lib/exe/xmlrpc.php";
		return new DokuBadUrlException(mess, e);
	}
//! @endcond
}
