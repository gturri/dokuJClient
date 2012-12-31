package dw.xmlrpc.exception;

/**
 * Thrown when a user attempts to make a query he isn't allowed to.
 * 
 * If this exception is unexpected you may want to check:
 * * The credentials given to the #dw.xmlrpc.DokuJClient
 * * The wiki configuration (xmlrpc interface must be enabled, and this user must be allowed to use it)
 */
public class DokuUnauthorizedException extends DokuException {

	private static final long serialVersionUID = -1970601945755526735L;

	public DokuUnauthorizedException() {}

	public DokuUnauthorizedException(String message) {
		super(message);
	}

	public DokuUnauthorizedException(Throwable cause) {
		super(cause);
	}

	public DokuUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

}
