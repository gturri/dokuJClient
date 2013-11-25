package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class PagePutter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true));
		jsap.registerParameter(new UnflaggedOption("rawWikiText").setRequired(true).setGreedy(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config)throws DokuException {
		String pageId = config.getString("pageId");
		String rawWikiText = buildContent(config);
		dokuClient.putPage(pageId, rawWikiText);
		return new Output();
	}

	private String buildContent(JSAPResult config) {
		LineConcater concater = new LineConcater(" ");
		for(String text : config.getStringArray("rawWikiText")){
			concater.addLine(text);
		}
		return concater.toString();
	}
}
