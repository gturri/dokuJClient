package dw.xmlrpc.exception;

/**
 * Thrown when one attemps to work on a file which doesn't exist on the wiki
 */
public class DokuDistantFileDoesntExistException extends DokuException {

	private static final long serialVersionUID = -5014826815320361555L;

	public DokuDistantFileDoesntExistException(Throwable cause) {
		super(cause);
	}
}
