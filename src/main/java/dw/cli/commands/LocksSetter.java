package dw.cli.commands;

import java.util.ArrayList;
import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.LockResult;
import dw.xmlrpc.exception.DokuException;

public class LocksSetter extends Command {
	private final boolean _shouldUnlock;

	public LocksSetter(boolean shouldUnlock){
		_shouldUnlock = shouldUnlock;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true).setGreedy(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		List<String> pages = getPageList();

		LockResult result;
		if ( _shouldUnlock ){
			result = dokuClient.setLocks(new ArrayList<String>(), pages);
		} else {
			result = dokuClient.setLocks(pages, new ArrayList<String>());
		}

		return buildOutput(result);
	}

	private List<String> getPageList() {
		List<String> pages = new ArrayList<String>();

		for(String page : _config.getStringArray("pageId")){
			pages.add(page);
		}

		return pages;
	}

	private Output buildOutput(LockResult result) {
		Output output = new Output();
		if ( result.hasFailure() ){
			output.err = "A lock or unlock operation, failed";
			output.exitCode = -1;
		}

		return output;
	}
}
