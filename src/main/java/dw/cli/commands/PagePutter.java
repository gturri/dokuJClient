package dw.cli.commands;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class PagePutter extends Command {
	private final boolean _appendInsteadOfPut;

	public PagePutter(){
		this(false);
	}

	public PagePutter(boolean appendInsteadOfPut){
		_appendInsteadOfPut = appendInsteadOfPut;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new FlaggedOption("summary")
			.setLongFlag("summary")
			.setDefault("")
			.setStringParser(JSAP.STRING_PARSER));

		addPageIdOption(jsap);
		jsap.registerParameter(new UnflaggedOption("rawWikiText").setRequired(true).setGreedy(true));

		jsap.registerParameter(new Switch("minor").setLongFlag("minor"));
	}

	@Override
	protected Output run(DokuJClient dokuClient)throws DokuException {
		String pageId = _config.getString("pageId");
		String rawWikiText = buildContent();
		String summary = _config.getString("summary");
		boolean minor = _config.getBoolean("minor");

		if ( _appendInsteadOfPut ){
			dokuClient.appendPage(pageId, rawWikiText, summary, minor);
		} else {
			dokuClient.putPage(pageId, rawWikiText, summary, minor);
		}

		return new Output();
	}

	private String buildContent() {
		LineConcater concater = new LineConcater(" ");
		for(String text : _config.getStringArray("rawWikiText")){
			concater.addLine(text);
		}
		return concater.toString();
	}
}
