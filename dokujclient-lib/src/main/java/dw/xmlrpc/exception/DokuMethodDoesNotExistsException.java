package dw.xmlrpc.exception;

public class DokuMethodDoesNotExistsException extends DokuException {

	private static final long serialVersionUID = 2005563951980285304L;

	public DokuMethodDoesNotExistsException(String message, Throwable cause){
		super(message, cause);
	}

}
