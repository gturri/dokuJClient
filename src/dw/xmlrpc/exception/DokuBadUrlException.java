package dw.xmlrpc.exception;

public class DokuBadUrlException extends DokuException {

	private static final long serialVersionUID = 7969663858357866492L;
	
	public DokuBadUrlException() { }

	public DokuBadUrlException(String message) {
		super(message);
	}
	
	public DokuBadUrlException(Throwable cause) {
		super(cause);
	}
	
	public DokuBadUrlException(String message, Throwable cause) {
		super(message, cause);
	}
}
