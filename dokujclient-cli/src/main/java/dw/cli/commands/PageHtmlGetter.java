package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class PageHtmlGetter extends Command {
	private final boolean _withVersion;

	public PageHtmlGetter(){
		this(false);
	}

	public PageHtmlGetter(boolean withVersion){
		_withVersion = withVersion;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addPageIdOption(jsap);

		if (_withVersion){
			jsap.registerParameter(new UnflaggedOption("timestamp").setStringParser(JSAP.INTEGER_PARSER).setRequired(true));
		}
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		String pageId = _config.getString("pageId");

		if ( _withVersion ){
			int timestamp = _config.getInt("timestamp");
			return new Output(dokuClient.getPageHTMLVersion(pageId, timestamp));
		} else {
			return new Output(dokuClient.getPageHTML(pageId));
		}
	}
}
