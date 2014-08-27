package dw.cli.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;

import dw.cli.Command;
import dw.cli.Output;
import dw.cli.Program;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageDW;
import dw.xmlrpc.exception.DokuException;

public class Syncer extends Command {

	private DokuJClient _dwClientOrigin;
	private DokuJClient _dwClientDestination;
	private boolean _verbose;
	
	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("verbose").setShortFlag('v').setLongFlag("verbose"));
		jsap.registerParameter(new FlaggedOption("urlDest").setStringParser(JSAP.URL_PARSER).setRequired(true).setLongFlag("urlDest"));
		jsap.registerParameter(new FlaggedOption("loginDest").setStringParser(JSAP.STRING_PARSER).setRequired(false).setLongFlag("loginDest"));
		jsap.registerParameter(new FlaggedOption("passwordDest").setStringParser(JSAP.STRING_PARSER).setRequired(false).setLongFlag("passwordDest"));
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		_dwClientOrigin = dokuClient;
		_dwClientDestination = Program.buildDokuClient(_config.getURL("urlDest"), _config.getString("loginDest"), _config.getString("passwordDest"));
		_verbose = _config.getBoolean("verbose");
		List<PageDW> pagesToPush = getPagesToPush();
		pushPages(pagesToPush);
		return new Output();
	}
	
	private void pushPages(List<PageDW> pagesToPush) throws DokuException {
		for(PageDW page : pagesToPush){
			if ( _verbose ){
				System.out.println("Handling " + page.id() + " ...");
			}
			String rawWikiText = _dwClientOrigin.getPage(page.id());
			_dwClientDestination.putPage(page.id(), rawWikiText);
			if ( _verbose ){
				System.out.println("Done with " + page.id());
			}
		}
	}

	private List<PageDW> getPagesToPush() throws DokuException{
		Map<String, PageDW> pagesWikiOrigin = getPages(_dwClientOrigin);
		Map<String, PageDW> pagesWikiDestination = getPages(_dwClientDestination);
		List<PageDW> result = new ArrayList<PageDW>();
		
		for(Entry<String, PageDW> itemInWikiOrigin : pagesWikiOrigin.entrySet()){
			PageDW pageInWikiDestination = pagesWikiDestination.get(itemInWikiOrigin.getKey());
			if ( pageInWikiDestination == null || !pageInWikiDestination.hash().equals(itemInWikiOrigin.getValue().hash()) ){
				result.add(itemInWikiOrigin.getValue());
			}
		}
		
		return result;
	}
	
	private Map<String, PageDW> getPages(DokuJClient dwClient) throws DokuException{
		List<PageDW> pages = dwClient.getPagelist("");
		Map<String, PageDW> result = new HashMap<String, PageDW>();
		for(PageDW page : pages){
			result.put(page.id(), page);
		}
		return result;
	}

}
