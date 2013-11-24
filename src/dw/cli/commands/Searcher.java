package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.SearchResult;
import dw.xmlrpc.exception.DokuException;

public class Searcher extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("searchQuery").setRequired(true));
		jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
		jsap.registerParameter(new Switch("snippet").setLongFlag("snippet"));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		List<SearchResult> searchResults = dokuClient.search(config.getString("searchQuery"));

		Output result = new Output();
		result.out = searchResultsToString(searchResults, config.getBoolean("longFormat"), config.getBoolean("snippet"));
		return result;
	}

	private String searchResultsToString(List<SearchResult> searchResults,	boolean longFormat, boolean withSnippet) {
		String result = "";
		boolean firstLine = true;
		for(SearchResult searchResult : searchResults){
			if ( firstLine ){
				firstLine = false;
			} else {
				result += "\n";
			}
			if ( longFormat ){
				result += searchResultToLongString(searchResult);
			} else {
				result += searchResultToString(searchResult);
			}

			if ( withSnippet ){
				result += "\n\t" + searchResult.snippet();
			}
		}
		return result;
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
