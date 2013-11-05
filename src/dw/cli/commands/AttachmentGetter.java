package dw.cli.commands;

import java.io.IOException;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
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
	protected Output run(DokuJClient dokuClient, JSAPResult config)	throws DokuException {
		Output output = new Output();
		try {
			dokuClient.getAttachment(config.getString("fileId"), config.getString("localFile"));
		} catch (IOException e) {
			output.err = e.getMessage();
			output.exitCode = -1;
		} catch (DokuDistantFileDoesntExistException e){
			output.err = e.getMessage();
			output.exitCode = -1;
		}
		return output;
	}

}
