package dw.xmlrpc.exception;

/**
 * Thrown when a bad url has been provided
 */
public class DokuBadUrlException extends DokuException {

	private static final long serialVersionUID = 7969663858357866492L;

	public DokuBadUrlException(String message, Throwable cause) {
		super(message, cause);
	}
}
