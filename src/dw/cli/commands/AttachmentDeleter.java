package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AttachmentDeleter extends Command {

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		dokuClient.deleteAttachment(config.getString("attachmentId"));
		return new Output();
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("attachmentId").setRequired(true));
	}

}
