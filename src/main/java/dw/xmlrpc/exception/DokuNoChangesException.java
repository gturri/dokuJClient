package dw.xmlrpc.exception;

/**
 * Thrown when one looked out for changes when there are none
 */
public class DokuNoChangesException extends DokuException {
	private static final long serialVersionUID = 6200266566237242596L;

	public DokuNoChangesException(Throwable cause) {
		super(cause);
	}
}
