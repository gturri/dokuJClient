package dw.exception;

public class DokuPageLockedException extends DokuException {

	private static final long serialVersionUID = -6402957075601882399L;

	public DokuPageLockedException() {	}

	public DokuPageLockedException(String message) {
		super(message);
	}

	public DokuPageLockedException(Throwable cause) {
		super(cause);
	}

	public DokuPageLockedException(String message, Throwable cause) {
		super(message, cause);
	}

}
