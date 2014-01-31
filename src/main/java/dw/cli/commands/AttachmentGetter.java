package dw.cli.commands;

import java.io.IOException;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuDistantFileDoesntExistException;
import dw.xmlrpc.exception.DokuException;

public class AttachmentGetter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("fileId").setRequired(true));
		jsap.registerParameter(new UnflaggedOption("localFile").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient)	throws DokuException {
		try {
			dokuClient.getAttachment(_config.getString("fileId"), _config.getString("localFile"));
		} catch (IOException e) {
			return new Output(e.getMessage(), -1);
		} catch (DokuDistantFileDoesntExistException e){
			return new Output(e.getMessage(), -1);
		}
		return new Output();
	}

}
