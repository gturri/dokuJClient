package dw.xmlrpc.exception;

public class DokuUnknownException extends DokuException {

	private static final long serialVersionUID = -4230515595018490484L;

	public DokuUnknownException(Throwable cause) {
		super(cause);
	}

	public DokuUnknownException(String message, Throwable cause) {
		super(message, cause);
	}

}
