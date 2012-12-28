package dw.xmlrpc.exception;

public class DokuWordblockException extends DokuException {

	private static final long serialVersionUID = -3281103378061961240L;

	public DokuWordblockException() { }

	public DokuWordblockException(String message) {
		super(message);
	}
	
	public DokuWordblockException(Throwable cause) {
		super(cause);
	}
	
	public DokuWordblockException(String message, Throwable cause) {
		super(message, cause);
	}
}
