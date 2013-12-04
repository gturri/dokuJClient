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
import dw.xmlrpc.LinkInfo;
import dw.xmlrpc.exception.DokuException;

public class LinksLister extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		List<LinkInfo> links = dokuClient.listLinks(config.getString("pageId"));
		return new Output(linkInfosToString(links, config.getBoolean("longFormat")));
	}

	private String linkInfosToString(List<LinkInfo> links, boolean longFormat) {
		LineConcater concater = new LineConcater();
		for(LinkInfo link : links){
			concater.addLine(linkInfoToString(link, longFormat));
		}
		return concater.toString();
	}

	private String linkInfoToString(LinkInfo link, boolean longFormat){
		if ( !longFormat ){
			return link.page();
		} else {
			return link.type()
					+ " " + link.page()
					+ " " + link.href();
		}
	}

}
