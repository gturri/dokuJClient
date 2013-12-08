package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageChange;
import dw.xmlrpc.exception.DokuException;

public class RecentChangesGetter extends ItemListToStringCommand<PageChange> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("timestamp").setStringParser(JSAP.INTEGER_PARSER).setRequired(true));
	}

	@Override
	protected List<PageChange> query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getRecentChanges(_config.getInt("timestamp"));
	}

	@Override
	protected String itemToString(PageChange pageChange) {
		return pageChange.name()
				+ " " + pageChange.version()
				+ " " + pageChange.author()
				+ " " + pageChange.perms()
				+ " " + pageChange.size();
	}
}
