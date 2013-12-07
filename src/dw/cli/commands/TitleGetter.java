package dw.cli.commands;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class TitleGetter extends SimpleCommand {
	@Override
	protected String query(DokuJClient dokuClient)	throws DokuException {
		return dokuClient.getTitle();
	}
}
