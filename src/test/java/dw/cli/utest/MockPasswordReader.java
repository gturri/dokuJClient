package dw.cli.utest;

import dw.cli.PasswordReader;

public class MockPasswordReader extends PasswordReader {
	private final String _string;

	public MockPasswordReader(String string){
		_string = string;
	}

	@Override
	public String readPassword(){
		return _string;
	}
}
