package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuDeleteAttachmentException;
import dw.xmlrpc.exception.DokuException;

public class AttachmentDeleter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("force").setShortFlag('f').setLongFlag("force"));
		jsap.registerParameter(new UnflaggedOption("attachmentId").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		try {
			dokuClient.deleteAttachment(_config.getString("attachmentId"));
		} catch (DokuDeleteAttachmentException e){
			if ( ! _config.getBoolean("force") ){
				return new Output(e.getMessage(), -1);
			}
		}
		return new Output();
	}
}
