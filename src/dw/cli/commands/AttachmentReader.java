package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.AttachmentDetails;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AttachmentReader extends ItemListToStringCommand<AttachmentDetails> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addLongFormatSwitch(jsap);
		jsap.registerParameter(new UnflaggedOption("namespace").setRequired(true));
	}

	@Override
	protected List<AttachmentDetails> query(DokuJClient dokuClient) throws DokuException{
		return dokuClient.getAttachments(_config.getString("namespace"));
	}

	@Override
	protected String itemToString(AttachmentDetails attachmentsDetails){
		if ( _config.getBoolean("longFormat") ){
			return attachmentDetailsToLongString(attachmentsDetails);
		} else {
			return attachmentDetailsToString(attachmentsDetails);
		}
	}

	private String attachmentDetailsToLongString(AttachmentDetails details){
		return details.perms()
				+ " " + details.size()
				+ " " + details.lastModified().toString()
				+ " " + details.id();
	}

	private String attachmentDetailsToString(AttachmentDetails details){
		return details.id();
	}
}
