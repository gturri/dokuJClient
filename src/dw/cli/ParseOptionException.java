package dw.cli;

public class ParseOptionException extends Exception {

	private static final long serialVersionUID = -3917728150229940890L;

	public ParseOptionException(String message) {
		super(message);
	}

	public ParseOptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
