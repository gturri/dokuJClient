package dw.cli;

import java.util.List;

import dw.xmlrpc.AttachmentDetails;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.DokuJClientConfig;

public class Program {

	public static CliOptions _options;

	public static void main(String[] args) throws Exception {
		try {
			System.out.println(run(args));
		} catch (Exception e){
			System.err.println("Caught exception: " + e.toString());

			for ( StackTraceElement stackElt : e.getStackTrace()){
				System.err.println("\t" + stackElt.toString());
			}
		}
	}

	public static String run(String[] args) throws Exception {
		OptionParser parser = new OptionParser(args);
		if ( ! parser.success() ){
			System.err.println(parser.getHelpMessage());
			System.exit(-1);
		}

		_options = parser.getCliOptions();
		DokuJClientConfig clientConfig = new DokuJClientConfig(_options.url);
		if ( _options.user != null ){
			clientConfig.setUser(_options.user, _options.password);
		}
		DokuJClient dokuClient = null;
		dokuClient = new DokuJClient(clientConfig);

		String result = "";
		if ( _options.command.equals("getTitle") ){
			result = dokuClient.getTitle();
		} else if ( _options.command.equals("getAttachments")){
			String ns = _options.commandArguments[_options.commandArguments.length -1];
			List<AttachmentDetails> attachmentsDetails  = dokuClient.getAttachments(ns);
			boolean firstLine = true;
			for(AttachmentDetails details : attachmentsDetails){
				if ( firstLine ){
					firstLine = false;
				} else {
					result += "\n";
				}
				if ( _options.commandArguments[0].equals("-l") ){
					result += details.perms()
							+ " " + details.size()
							+ " " + details.lastModified().toString()
							+ " " + details.id();
				} else {
					result += details.id();
				}
			}
		}

		return result;
	}
}
