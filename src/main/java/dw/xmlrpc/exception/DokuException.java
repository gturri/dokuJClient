package dw.xmlrpc.exception;

/**
 * Generic exception
 */
public class DokuException extends Exception {

	private static final long serialVersionUID = 7882114034813429168L;

	public DokuException(Throwable cause) {
		super(cause);
	}

	public DokuException(String message, Throwable cause) {
		super(message, cause);
	}

	public DokuException(String message){
		super(message);
	}
}
