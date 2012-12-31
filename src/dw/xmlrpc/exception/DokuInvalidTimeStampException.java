package dw.xmlrpc.exception;

import dw.xmlrpc.exception.DokuException;

/**
 * Thrown when an invalid timestamp is provided
 */
public class DokuInvalidTimeStampException extends DokuException {

	private static final long serialVersionUID = -2041584428271122864L;

	public DokuInvalidTimeStampException() { }

	public DokuInvalidTimeStampException(String message) {
		super(message);
	}

	public DokuInvalidTimeStampException(Throwable cause) {
		super(cause);
	}

	public DokuInvalidTimeStampException(String message, Throwable cause) {
		super(message, cause);
	}

}
