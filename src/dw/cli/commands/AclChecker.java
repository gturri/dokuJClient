package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AclChecker extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config)	throws DokuException {
		Output output = new Output();
		String pageId = config.getString("pageId");
		output.out = dokuClient.aclCheck(pageId).toString();
		return output;
	}
}
