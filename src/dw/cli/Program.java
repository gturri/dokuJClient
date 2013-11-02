package dw.cli;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.DokuJClientConfig;

public class Program {

	public static CliOptions _options;

	public static void main(String[] args) throws Exception {
		try {
			Output output = run(args);
			printOutput(output);
			System.exit(output.exitCode);
		} catch (Exception e){
			printException(e);
			System.exit(-1);
		}
	}

	public static Output run(String[] args) throws Exception {
		OptionParser parser = new OptionParser(args);
		Output result = new Output();

		if ( ! parser.success() ){
			result.err = parser.getHelpMessage();
			result.exitCode = -1;
			return result;
		}

		_options = parser.getCliOptions();
		DokuJClientConfig clientConfig = new DokuJClientConfig(_options.url);
		if ( _options.user != null ){
			clientConfig.setUser(_options.user, _options.password);
		}
		DokuJClient dokuClient = null;
		dokuClient = new DokuJClient(clientConfig);

		if ( _options.command.equals("getTitle") ){
			result.out = dokuClient.getTitle();
		} else if ( _options.command.equals("getAttachments")){
			result = new AttachmentReader().getAttachments(dokuClient, _options.commandArguments);
		}

		return result;
	}

	private static void printOutput(Output output) {
		if ( output.err != null && !output.err.isEmpty() ){
			System.err.println(output.err);
		}
		if ( output.out != null && !output.out.isEmpty() ){
			System.out.println(output.out);
		}
	}

	private static void printException(Exception e) {
		System.err.println("Caught exception: " + e.toString());

		for ( StackTraceElement stackElt : e.getStackTrace()){
			System.err.println("\t" + stackElt.toString());
		}
	}
}
