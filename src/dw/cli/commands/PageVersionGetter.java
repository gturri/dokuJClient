package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class PageVersionGetter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addPageIdOption(jsap);
		jsap.registerParameter(new UnflaggedOption("timestamp").setStringParser(JSAP.INTEGER_PARSER).setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		return new Output(dokuClient.getPageVersion(_config.getString("pageId"), _config.getInt("timestamp")));
	}
}
