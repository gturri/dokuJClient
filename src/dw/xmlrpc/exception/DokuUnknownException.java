package dw.xmlrpc.exception;

/**
 * Thrown when an xmlrpc query failed for an unknown reason.
 *
 * To have more information on the failure you may access the original exception
 * using getCause()
 */
public class DokuUnknownException extends DokuException {

	private static final long serialVersionUID = -4230515595018490484L;

	public DokuUnknownException(Throwable cause) {
		super(cause);
	}

	public DokuUnknownException(String message, Throwable cause) {
		super(message, cause);
	}

}
