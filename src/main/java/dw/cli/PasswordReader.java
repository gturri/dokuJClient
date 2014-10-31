package dw.cli;

import java.io.Console;

public class PasswordReader {
	public String readPassword(){
		Console console = System.console();
		if ( console != null ){
			char[] password = console.readPassword("Enter password: ");
			return new String(password);
		} else {
			throw new NullPointerException("Can't get an instance of console to read the password");
		}
	}
}
