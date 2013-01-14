package dw.xmlrpc.exception;

public class DokuAttachmentUploadException extends DokuException {

	private static final long serialVersionUID = 3307739440250260529L;

	public DokuAttachmentUploadException() { }

	public DokuAttachmentUploadException(String message) {
		super(message);
	}

	public DokuAttachmentUploadException(Throwable cause) {
		super(cause);
	}

	public DokuAttachmentUploadException(String message, Throwable cause) {
		super(message, cause);
	}

}
