package dw.cli;

import java.io.IOException;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AttachmentPutter {

	public Output putAttachment(DokuJClient dokuClient, String[] commandArguments) throws DokuException {
		Output result = new Output();
		JSAPResult config;
		try {
			config = parseArguments(commandArguments);
		} catch (ParseOptionException e){
			result.err = e.getMessage();
			result.exitCode = -1;
			return result;
		}

		try {
			dokuClient.putAttachment(config.getString("attachmentId"), config.getString("localFile"), config.getBoolean("overwrite"));
		} catch (IOException e) {
			e.printStackTrace();
			result.err = e.getMessage();
			result.exitCode = -1;
		}
		return result;
	}

	private JSAPResult parseArguments(String[] arguments) throws ParseOptionException {
		JSAP jsap = new JSAP();

		try {
			jsap.registerParameter(new Switch("overwrite").setShortFlag('f').setLongFlag("force"));
			jsap.registerParameter(new UnflaggedOption("attachmentId").setRequired(true));
			jsap.registerParameter(new UnflaggedOption("localFile").setRequired(true));
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
