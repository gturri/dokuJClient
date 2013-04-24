package dw.xmlrpc.exception;

/**
 * Thrown when a call timeout
 */
public class DokuTimeoutException extends DokuException {

	private static final long serialVersionUID = -1156444596841763628L;

	public DokuTimeoutException() {
	}

	public DokuTimeoutException(String message) {
		super(message);
	}

	public DokuTimeoutException(Throwable cause) {
		super(cause);
	}

	public DokuTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

}
