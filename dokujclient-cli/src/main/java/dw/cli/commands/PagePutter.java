package dw.cli.commands;

import java.io.IOException;

import com.google.common.base.Joiner;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.cli.StdinReader;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class PagePutter extends Command {
	private final boolean _appendInsteadOfPut;
	private final StdinReader _stdinReader;

	public PagePutter(){
		this(false);
	}

	public PagePutter(boolean appendInsteadOfPut){
		this(appendInsteadOfPut, new StdinReader());
	}

	public PagePutter(boolean appendInsteadOfPut, StdinReader stdinReader){
		_appendInsteadOfPut = appendInsteadOfPut;
		_stdinReader = stdinReader;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new FlaggedOption("summary")
			.setLongFlag("summary")
			.setDefault("")
			.setStringParser(JSAP.STRING_PARSER));

		addPageIdOption(jsap);
		jsap.registerParameter(new UnflaggedOption("rawWikiText").setRequired(false).setGreedy(true));

		jsap.registerParameter(new Switch("minor").setLongFlag("minor"));
	}

	@Override
	protected Output run(DokuJClient dokuClient)throws DokuException {
		String pageId = _config.getString("pageId");
		String rawWikiText;
		try {
			rawWikiText = buildContent();
		} catch (IOException e) {
			return new Output("Failed to read stdin: " + e.getMessage(), 1);
		}
		String summary = _config.getString("summary");
		boolean minor = _config.getBoolean("minor");

		if ( _appendInsteadOfPut ){
			dokuClient.appendPage(pageId, rawWikiText, summary, minor);
		} else {
			dokuClient.putPage(pageId, rawWikiText, summary, minor);
		}

		return new Output();
	}

	private String buildContent() throws IOException {
		if ( _config.contains("rawWikiText")){
			return Joiner.on(" ").join(_config.getStringArray("rawWikiText"));
		} else {
			return _stdinReader.readStdin();
		}
	}
}
