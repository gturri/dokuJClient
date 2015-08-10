package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AclChecker extends Command {
	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addPageIdOption(jsap);
	}

	@Override
	protected Output run(DokuJClient dokuClient)	throws DokuException {
		String pageId = _config.getString("pageId");
		return new Output(dokuClient.aclCheck(pageId).toString());
	}
}
