package dw.cli;

import java.util.ArrayList;
import java.util.List;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.defaultsources.PropertyDefaultSource;

public class OptionParser {
	private final boolean _success;
	private final CliOptions _cliOptions;
	private final String _helpMessage;

	public OptionParser(String[] args){
		List<String> genericOptions = new ArrayList<String>();
		List<String> commandOptions = new ArrayList<String>();

		String command = null;
		boolean alreadyReadCommand = false;
		for ( int i=0 ; i < args.length ; i++ ){
			String arg = args[i];

			if ( alreadyReadCommand){
				commandOptions.add(arg);
			} else {
				if ( arg.startsWith("-") ){
					genericOptions.add(arg);
					i++;
					if ( i < args.length ){
						genericOptions.add(args[i]);
					}
				} else {
					command = arg;
					alreadyReadCommand = true;
				}
			}
		}

		JSAP jsap = buildJsap();
		boolean success;
		String helpMessage;
		CliOptions cliOptions;

		registerDefaultSource(jsap);

		JSAPResult config = jsap.parse(genericOptions.toArray(new String[]{}));
		if ( ! config.success() || (command == null && !config.getBoolean("help")) ){
			success = false;
			helpMessage = "";
			for (@SuppressWarnings("rawtypes") java.util.Iterator errs = config.getErrorMessageIterator();
					errs.hasNext();) {
				helpMessage += errs.next() + "\n";
			}
			helpMessage += getUsage();
			cliOptions = null;
		} else {
			success = true;
			helpMessage = getUsage();
			if ( command == null && config.getBoolean("help")){
				command = "help";
			}
			cliOptions = new CliOptions();
			cliOptions.password = config.getString("password");
			cliOptions.user = config.getString("user");
			cliOptions.url = config.getURL("url");
			cliOptions.command = command;
			cliOptions.commandArguments = commandOptions.toArray(new String[]{});
		}

		_success = success;
		_helpMessage = helpMessage;
		_cliOptions = cliOptions;
	}

	private static JSAP buildJsap(){
		JSAP jsap = new JSAP();

		try {
			jsap.registerParameter(new FlaggedOption("user")
			.setStringParser(JSAP.STRING_PARSER)
			.setRequired(false)
			.setShortFlag('u')
			.setLongFlag("user"));

			jsap.registerParameter(new FlaggedOption("url")
			.setStringParser(JSAP.URL_PARSER)
			.setRequired(true)
			.setShortFlag(JSAP.NO_SHORTFLAG)
			.setLongFlag("url"));

			jsap.registerParameter(new FlaggedOption("password")
			.setStringParser(JSAP.STRING_PARSER)
			.setRequired(false)
			.setShortFlag('p')
			.setLongFlag("password"));

			jsap.registerParameter(new Switch("help")
			.setShortFlag('h')
			.setLongFlag("help"));
		} catch (JSAPException e){
			throw new RuntimeException("Something went really wrong", e);
		}

		return jsap;
	}

	private void registerDefaultSource(JSAP jsap) {
		String home = System.getProperty("user.home");
		PropertyDefaultSource source = new PropertyDefaultSource(home + "/.dokujclientrc", false);
		jsap.registerDefaultSource(source);
	}

	public boolean success(){
		return _success;
	}

	public CliOptions getCliOptions(){
		return _cliOptions;
	}

	public String getHelpMessage(){
		return _helpMessage;
	}

	public static String getUsage(){
		String message = buildJsap().getUsage() + " command";

		message += "\n\nAvailable commands:";
		for(String command : CommandFactory.CommandList()){
			message += "\n\t" + command;
		}

		message += "\n\nTo get help for a given command: help <command>";

		return message;
	}
}
