package dw.cli.commands;

import java.io.IOException;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuAttachmentUploadException;
import dw.xmlrpc.exception.DokuException;

public class AttachmentPutter extends Command {

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		Output output = new Output();
		try {
			dokuClient.putAttachment(config.getString("attachmentId"), config.getString("localFile"), config.getBoolean("overwrite"));
		} catch (IOException e) {
			output.err = e.getMessage();
			output.exitCode = -1;
		} catch (DokuAttachmentUploadException e){
			output.err = e.getMessage();
			output.exitCode = -1;
		}
		return output;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("overwrite").setShortFlag('f').setLongFlag("force"));
		jsap.registerParameter(new UnflaggedOption("attachmentId").setRequired(true));
		jsap.registerParameter(new UnflaggedOption("localFile").setRequired(true));
	}
}
