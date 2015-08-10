package dw.xmlrpc.exception;

/**
 * Thrown when an attempt to delete a file on the wiki failed
 */
public class DokuDeleteAttachmentException extends DokuException {

	private static final long serialVersionUID = -5093967783205014204L;

	public DokuDeleteAttachmentException(Throwable cause) {
		super(cause);
	}
}
