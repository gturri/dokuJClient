package dw.exception;

public class DokuPageLockedException extends DokuException {

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
