package dw.cli;

import java.util.List;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.AttachmentDetails;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AttachmentReader {
	public Output getAttachments(DokuJClient dokuClient, String[] commandArguments) throws DokuException{
		Output result = new Output();
		JSAPResult config;
		try {
			config = parseArguments(commandArguments);
		} catch (ParseOptionException e){
			result.err = e.getMessage();
			result.exitCode = -1;
			return result;
		}

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

	private JSAPResult parseArguments(String[] arguments) throws ParseOptionException {
		JSAP jsap = new JSAP();

		try {
			jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
			jsap.registerParameter(new UnflaggedOption("namespace").setRequired(true));
		} catch (JSAPException e) {
			throw new ParseOptionException(e.toString(), e);
		}

		JSAPResult config = jsap.parse(arguments);
		if ( ! config.success() ){
			String helpMessage = "";
            for (@SuppressWarnings("rawtypes") java.util.Iterator errs = config.getErrorMessageIterator();
            		errs.hasNext();) {
            	helpMessage += errs.next() + "\n";
            }
            helpMessage += jsap.getUsage();
            throw new ParseOptionException(helpMessage);
		}
		return config;
	}
}
