package dw.cli;

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
		}

		return result;
	}
}
