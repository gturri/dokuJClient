package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.AttachmentInfo;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AttachmentInfoGetter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("mediaId").setRequired(true));
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		AttachmentInfo info = dokuClient.getAttachmentInfo(_config.getString("mediaId"));
		return new Output(attachmentInfoToString(info));
	}

	private String attachmentInfoToString(AttachmentInfo info) {
		return info.id()
				+ " " + info.size()
				+ " " + info.lastModified();
	}
}
