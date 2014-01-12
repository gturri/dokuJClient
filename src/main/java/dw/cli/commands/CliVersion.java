package dw.cli.commands;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class CliVersion extends SimpleCommand {
	//version automatically updated by ant
	public static final String CliVersion = "3.0.0";

	@Override
	protected String query(DokuJClient dokuClient) throws DokuException {
		return CliVersion;
	}
}
