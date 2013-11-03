package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.AttachmentDetails;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AttachmentReader extends Command {

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException{
		Output result = new Output();
		List<AttachmentDetails> attachmentsDetails  = dokuClient.getAttachments(config.getString("namespace"));
		result.out = attachmentDetailsToString(attachmentsDetails, config.getBoolean("longFormat"));
		return result;
	}

	private String attachmentDetailsToString(List<AttachmentDetails> attachmentsDetails, boolean longFormat){
		String result = "";
		boolean firstLine = true;
		for(AttachmentDetails details : attachmentsDetails){
			if ( firstLine ){
				firstLine = false;
			} else {
				result += "\n";
			}
			if ( longFormat ){
				result += attachmentDetailsToLongString(details);
			} else {
				result += attachmentDetailsToString(details);
			}
		}
		return result;
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

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
		jsap.registerParameter(new UnflaggedOption("namespace").setRequired(true));
	}
}
