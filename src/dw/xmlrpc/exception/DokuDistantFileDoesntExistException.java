package dw.xmlrpc.exception;

public class DokuDistantFileDoesntExistException extends DokuException {

	private static final long serialVersionUID = -5014826815320361555L;

	public DokuDistantFileDoesntExistException() {	}

	public DokuDistantFileDoesntExistException(String message) {
		super(message);
	}

	public DokuDistantFileDoesntExistException(Throwable cause) {
		super(cause);
	}

	public DokuDistantFileDoesntExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
