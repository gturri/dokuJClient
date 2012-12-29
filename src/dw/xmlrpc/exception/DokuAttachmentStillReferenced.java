package dw.xmlrpc.exception;


public class DokuAttachmentStillReferenced extends DokuException {

	private static final long serialVersionUID = -1349452363587746526L;

	public DokuAttachmentStillReferenced() { }

	public DokuAttachmentStillReferenced(String message) {
		super(message);
	}

	public DokuAttachmentStillReferenced(Throwable cause) {
		super(cause);
	}

	public DokuAttachmentStillReferenced(String message, Throwable cause) {
		super(message, cause);
	}
}
