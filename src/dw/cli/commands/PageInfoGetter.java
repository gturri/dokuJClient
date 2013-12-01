package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageInfo;
import dw.xmlrpc.exception.DokuException;

public class PageInfoGetter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		PageInfo pageInfo = dokuClient.getPageInfo(config.getString("pageId"));
		return new Output(pageInfoToString(pageInfo));
	}

	private String pageInfoToString(PageInfo pageInfo){
		return pageInfo.id()
				+ " " + pageInfo.version()
				+ " " + pageInfo.author();
	}
}
