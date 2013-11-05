package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuDeleteAttachmentException;
import dw.xmlrpc.exception.DokuException;

public class AttachmentDeleter extends Command {

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		Output output = new Output();
		try {
			dokuClient.deleteAttachment(config.getString("attachmentId"));
		} catch (DokuDeleteAttachmentException e){
			if ( ! config.getBoolean("force") ){
				output.err = e.getMessage();
				output.exitCode = -1;
			}
		}
		return output;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("force").setShortFlag('f').setLongFlag("force"));
		jsap.registerParameter(new UnflaggedOption("attachmentId").setRequired(true));
	}

}
