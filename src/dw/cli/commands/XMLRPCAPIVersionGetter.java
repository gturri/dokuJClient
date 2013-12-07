package dw.cli.commands;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class XMLRPCAPIVersionGetter extends SimpleCommand {
	@Override
	protected String query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getXMLRPCAPIVersion().toString();
	}
}