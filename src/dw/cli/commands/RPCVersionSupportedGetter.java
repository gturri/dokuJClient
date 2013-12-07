package dw.cli.commands;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class RPCVersionSupportedGetter extends SimpleCommand {

	@Override
	protected String query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getRPCVersionSupported().toString();
	}
}
