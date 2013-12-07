package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.Page;
import dw.xmlrpc.exception.DokuException;

public class AllPageGetter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		List<Page> pages = dokuClient.getAllPages();
		return new Output(pagesToString(pages, config.getBoolean("longFormat")));
	}

	private String pagesToString(List<Page> pages, boolean longFormat) {
		LineConcater concater = new LineConcater();
		for(Page page : pages){
			concater.addLine(pageToString(page, longFormat));
		}
		return concater.toString();
	}

	private String pageToString(Page page, boolean longFormat){
		if ( !longFormat ){
			return page.id();
		} else {
			return page.id()
					+ " " + page.perms()
					+ " " + page.size();
		}
	}
}
