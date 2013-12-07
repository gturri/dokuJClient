package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.Page;
import dw.xmlrpc.exception.DokuException;

public class AllPageGetter extends ItemListToStringCommand<Page> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addLongFormatSwitch(jsap);
	}

	@Override
	protected List<Page> query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getAllPages();
	}

	@Override
	protected String itemToString(Page page) {
		if ( !_config.getBoolean("longFormat") ){
			return page.id();
		} else {
			return page.id()
					+ " " + page.perms()
					+ " " + page.size();
		}
	}
}
