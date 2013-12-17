package dw.xmlrpc.exception;

/**
 * Thrown if a page can't be edited because of a wordblock.
 *
 * The list of forbidden words for a given wiki are defined in its wordblock.conf file.
 * If one try to edit a page addind one of this words, DokuWiki won't allow the edition,
 * and this exception will be thrown.
 */
public class DokuWordblockException extends DokuException {

	private static final long serialVersionUID = -3281103378061961240L;

	public DokuWordblockException(Throwable cause) {
		super(cause);
	}
}
