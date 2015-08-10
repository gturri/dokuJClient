package dw.xmlrpc.exception;

/**
 * Thrown when one tries to delete a distant media still referenced.
 *
 * Dokuwiki won't let delete a media if a page still uses it.
 * If one tries to delete such a media, Dokuwiki won't allow it, and this exception
 * will be thrown
 */
public class DokuAttachmentStillReferenced extends DokuException {

	private static final long serialVersionUID = -1349452363587746526L;

	public DokuAttachmentStillReferenced(Throwable cause) {
		super(cause);
	}
}
