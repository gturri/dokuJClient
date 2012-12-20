package dw.exception;

@SuppressWarnings("serial")
public class DokuException extends Exception {

	public DokuException() { }

	public DokuException(String message) {
		super(message);
	}
	
	public DokuException(Throwable cause) {
		super(cause);
	}
	
	public DokuException(String message, Throwable cause) {
		super(message, cause);
	}
}
