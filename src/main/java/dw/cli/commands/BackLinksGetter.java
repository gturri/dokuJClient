package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class BackLinksGetter extends ItemListToStringCommand<String> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addPageIdOption(jsap);
	}

	@Override
	protected List<String> query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getBackLinks(_config.getString("pageId"));
	}

	@Override
	protected String itemToString(String item) {
		return item;
	}
}
