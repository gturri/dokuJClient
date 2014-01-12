package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.SearchResult;
import dw.xmlrpc.exception.DokuException;

public class Searcher extends ItemListToStringCommand<SearchResult> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("searchQuery").setRequired(true));
		addLongFormatSwitch(jsap);
		jsap.registerParameter(new Switch("snippet").setLongFlag("snippet"));
	}

	@Override
	protected List<SearchResult> query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.search(_config.getString("searchQuery"));
	}

	@Override
	protected String itemToString(SearchResult searchResult) {
		String result;
		if ( _config.getBoolean("longFormat") ){
			result = searchResultToLongString(searchResult);
		} else {
			result = searchResultToString(searchResult);
		}

		if ( _config.getBoolean("snippet") ){
			result += "\n" + addSnippet( searchResult);
		}

		return result;
	}

	private String addSnippet(SearchResult searchResult) {
		LineConcater concater = new LineConcater();
		for ( String line : searchResult.snippet().split("\n")){
			concater.addLine("> " + line);
		}
		concater.addLine("");
		return concater.toString();
	}

	private String searchResultToString(SearchResult searchResult) {
		return searchResult.id();
	}

	private String searchResultToLongString(SearchResult searchResult) {
		return searchResult.score()
				+ " " + searchResult.mtime()
			    + " " + searchResult.rev()
			    + " " + searchResult.title()
			    + " " + searchResult.size()
			    + " " + searchResult.id();
	}

}
