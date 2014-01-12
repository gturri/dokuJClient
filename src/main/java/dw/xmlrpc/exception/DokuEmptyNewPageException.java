package dw.xmlrpc.exception;

/**
 * Thrown by Dokuwiki when one try to use putPage with an empty page content
 * if the page doesn't exist yet (or has alreay been deleted)
 */
public class DokuEmptyNewPageException extends DokuException {

	private static final long serialVersionUID = 3088375008770606694L;

	public DokuEmptyNewPageException(Throwable cause) {
		super(cause);
	}
}
