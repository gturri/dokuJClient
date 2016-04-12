package dw.cli;

import java.net.MalformedURLException;

import dw.cli.commands.HelpPrinter;
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
		if ( parser.userAskForHelp() ){
			return new HelpPrinter(true).run(buildNullDokuJClient(), parser.getCommandArguments());
		}

		parser.parse();
		if ( ! parser.success() ){
			Output result = new Output();
			result.err = parser.getHelpMessage();
			result.exitCode = parser.success() ? 0 : -1;
			return result;
		}

		CliOptions options = parser.getCliOptions();
		DokuJClient dokuClient = buildDokuClient(options);
		Command command = new CommandFactory().build(options.command);
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
		clientConfig.setDebug(options.debug);
		DokuJClient dokuClient = new DokuJClient(clientConfig);
		dokuClient.setLogger(null);
		return dokuClient;
	}

	private static DokuJClient buildNullDokuJClient() throws DokuException{
		try {
			return new DokuJClient("http://whatever");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
