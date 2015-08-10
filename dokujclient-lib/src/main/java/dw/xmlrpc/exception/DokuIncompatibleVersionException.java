package dw.xmlrpc.exception;

public class DokuIncompatibleVersionException extends DokuException {
	private static final long serialVersionUID = 7903625706488374779L;

	public DokuIncompatibleVersionException(String oldestCompatibleVersion){
		super(getMessage(oldestCompatibleVersion));
	}

	private static String getMessage(String oldestCompatibleVersion){
		return "The version of Dokuwiki we're querying doesn't support the action we're trying to perform. "
				+ "The older compatible version is " + oldestCompatibleVersion + ". "
				+ "Try to update your Dokuwiki server.";
	}

}
