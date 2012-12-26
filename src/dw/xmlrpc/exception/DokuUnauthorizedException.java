package dw.xmlrpc.exception;

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
