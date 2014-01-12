package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.LinkInfo;
import dw.xmlrpc.exception.DokuException;

public class LinksLister extends ItemListToStringCommand<LinkInfo> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addLongFormatSwitch(jsap);
		addPageIdOption(jsap);
	}

	@Override
	protected List<LinkInfo> query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.listLinks(_config.getString("pageId"));
	}

	@Override
	protected String itemToString(LinkInfo link){
		if ( !_config.getBoolean("longFormat") ){
			return link.page();
		} else {
			return link.type()
					+ " " + link.page()
					+ " " + link.href();
		}
	}
}
