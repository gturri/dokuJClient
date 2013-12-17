package dw.xmlrpc.exception;

/**
 * Thrown when a call timeout
 */
public class DokuTimeoutException extends DokuException {

	private static final long serialVersionUID = -1156444596841763628L;

	public DokuTimeoutException(Throwable cause) {
		super(cause);
	}
}
