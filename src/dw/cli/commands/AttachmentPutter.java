package dw.cli.commands;

import java.io.IOException;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuAttachmentUploadException;
import dw.xmlrpc.exception.DokuException;

public class AttachmentPutter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("overwrite").setShortFlag('f').setLongFlag("force"));
		jsap.registerParameter(new UnflaggedOption("attachmentId").setRequired(true));
		jsap.registerParameter(new UnflaggedOption("localFile").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		try {
			dokuClient.putAttachment(_config.getString("attachmentId"), _config.getString("localFile"), _config.getBoolean("overwrite"));
		} catch (IOException e) {
			return new Output(e.getMessage(), -1);
		} catch (DokuAttachmentUploadException e){
			return new Output(e.getMessage(), -1);
		}
		return new Output();
	}
}
