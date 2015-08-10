package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.MediaChange;
import dw.xmlrpc.exception.DokuException;

public class RecentMediaChangesGetter extends ItemListToStringCommand<MediaChange>{
	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("timestamp").setStringParser(JSAP.INTEGER_PARSER).setRequired(true));
	}

	@Override
	protected List<MediaChange> query(DokuJClient dokuClient) throws DokuException {
		return dokuClient.getRecentMediaChanges(_config.getInt("timestamp"));
	}

	@Override
	protected String itemToString(MediaChange mediaChange) {
		return mediaChange.id()
				+ " " + mediaChange.author()
				+ " " + mediaChange.perms()
				+ " " + mediaChange.size();
	}
}
