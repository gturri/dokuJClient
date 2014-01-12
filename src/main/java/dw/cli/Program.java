package dw.cli;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.DokuJClientConfig;
import dw.xmlrpc.exception.DokuException;

public class Program {
	public static void main(String[] args) {
		try {
			Output output = run(args);
			printOutput(output);
			System.exit(output.exitCode);
		} catch (Exception e){
			printException(e);
			System.exit(-1);
		}
	}

	public static Output run(String[] args) throws DokuException {
		OptionParser parser = new OptionParser(args);
		Output result = new Output();

		if ( ! parser.success() ){
			result.err = parser.getHelpMessage();
			result.exitCode = parser.success() ? 0 : -1;
			return result;
		}

		CliOptions options = parser.getCliOptions();
		DokuJClient dokuClient = buildDokuClient(options);
		Command command = new CommandFactory().Build(options.command);
		return command.run(dokuClient, options.commandArguments);
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
		System.err.println("Error: " + e.getMessage());
	}

	private static DokuJClient buildDokuClient(CliOptions options) throws DokuException{
		if ( ! options.url.toString().endsWith("lib/exe/xmlrpc.php") ){
			throw new RuntimeException("Url should look like http[s]://server/mywiki/lib/exe/xmlrpc.php");
		}
		DokuJClientConfig clientConfig = new DokuJClientConfig(options.url);
		if ( options.user != null ){
			clientConfig.setUser(options.user, options.password);
		}
		DokuJClient dokuClient = new DokuJClient(clientConfig);
		dokuClient.setLogger(null);
		return dokuClient;
	}

}
