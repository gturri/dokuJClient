package dw.xmlrpc.exception;

/**
 * Thrown when one tries to edit a page locked by someone else
 */
public class DokuPageLockedException extends DokuException {

	private static final long serialVersionUID = -6402957075601882399L;

	public DokuPageLockedException(Throwable cause) {
		super(cause);
	}
}
