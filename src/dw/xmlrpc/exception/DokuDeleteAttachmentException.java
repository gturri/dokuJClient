package dw.xmlrpc.exception;

public class DokuDeleteAttachmentException extends DokuException {

	private static final long serialVersionUID = -5093967783205014204L;

	public DokuDeleteAttachmentException() { }

	public DokuDeleteAttachmentException(String message) {
		super(message);
	}
	
	public DokuDeleteAttachmentException(Throwable cause) {
		super(cause);
	}
	
	public DokuDeleteAttachmentException(String message, Throwable cause) {
		super(message, cause);
	}
}
