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
	private boolean _success;
	private CliOptions _cliOptions;
	private String _helpMessage;
	private final PasswordReader _pwdReader;

	private List<String> _genericArguments = new ArrayList<String>();
	private List<String> _commandArguments = new ArrayList<String>();
	private String _command;

	public String[] getCommandArguments(){
		return _commandArguments.toArray(new String[]{});
	}

	private static final String PASSWORD_OPTION = "password";
	private static final String PASSWORD_INTERACTIVE_OPTION = "password-interactive";
	private static final String DEBUG_OPTION = "debug";

	private JSAP _genericOptionsJSAP = buildJsap();

	public OptionParser(String[] args){
		this(args, new PasswordReader());
	}

	public OptionParser(String[] args, PasswordReader pwdReader){
		_pwdReader = pwdReader;
		feedGenericAndCommandOptions(args);
	}

	public void parse(){
		boolean success;
		String helpMessage;
		CliOptions cliOptions;

		JSAPResult config = _genericOptionsJSAP.parse(_genericArguments.toArray(new String[]{}));
		if ( ! config.success() || !checkOptionsAreConsistent(config, _command) ){
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
			if ( _command == null && config.getBoolean("help")){
				_command = "help";
			}
			if ( _command == null && config.getBoolean("version")){
				_command = "version";
			}

			cliOptions = new CliOptions();
			cliOptions.password = getPassword(config);
			cliOptions.user = config.getString("user");
			cliOptions.url = config.getURL("url");
			cliOptions.debug = config.getBoolean("debug");
			cliOptions.command = _command;
			cliOptions.commandArguments = _commandArguments.toArray(new String[]{});
		}

		_success = success;
		_helpMessage = helpMessage;
		_cliOptions = cliOptions;
	}

	private void feedGenericAndCommandOptions(String[] args){
		boolean alreadyReadCommand = false;
		for ( int i=0 ; i < args.length ; i++ ){
			String arg = args[i];

			if ( alreadyReadCommand){
				_commandArguments.add(arg);
			} else {
				if ( arg.startsWith("-") ){
					_genericArguments.add(arg);
					if ( ! arg.equals("--" + PASSWORD_INTERACTIVE_OPTION) && !arg.equals("--" + DEBUG_OPTION)){
						i++;
						if ( i < args.length ){
							_genericArguments.add(args[i]);
						}
					}
				} else {
					_command = arg;
					alreadyReadCommand = true;
				}
			}
		}


	}

	public boolean userAskForHelp(){
		JSAP jsap = new JSAP();
		try {
			registerHelpParameter(jsap);
		} catch (JSAPException e) {
			throw new RuntimeException("Something went really wrong", e);
		}
		JSAPResult parsed = jsap.parse(_genericArguments.toArray(new String[]{}));
		return (_command != null && _command.equals("help")) || parsed.getBoolean("help");
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

			jsap.registerParameter(new FlaggedOption(PASSWORD_OPTION)
			.setStringParser(JSAP.STRING_PARSER)
			.setRequired(false)
			.setShortFlag('p')
			.setLongFlag(PASSWORD_OPTION));

			jsap.registerParameter(new Switch("version")
			.setLongFlag("version"));

			jsap.registerParameter(new Switch(PASSWORD_INTERACTIVE_OPTION)
			.setLongFlag(PASSWORD_INTERACTIVE_OPTION));

			jsap.registerParameter(new Switch("debug")
			.setLongFlag(DEBUG_OPTION));

			registerHelpParameter(jsap);
		} catch (JSAPException e){
			throw new RuntimeException("Something went really wrong", e);
		}

		registerDefaultSource(jsap);

		return jsap;
	}

	private static void registerHelpParameter(JSAP jsap) throws JSAPException{
			jsap.registerParameter(new Switch("help")
			.setShortFlag('h')
			.setLongFlag("help"));
	}

	private static void registerDefaultSource(JSAP jsap) {
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
		for(String command : CommandFactory.commandList()){
			message += "\n\t" + command;
		}

		message += "\n\nTo get help for a given command: help <command>";

		return message;
	}

	private boolean checkOptionsAreConsistent(JSAPResult config, String command){
		if (command == null && !config.getBoolean("help") && !config.getBoolean("version")){
			return false;
		}
		if (config.contains(PASSWORD_OPTION) && config.getBoolean(PASSWORD_INTERACTIVE_OPTION)) {
			return false;
		}
		return true;
	}

	private String getPassword(JSAPResult config){
		if (config.contains(PASSWORD_OPTION)){
			return config.getString(PASSWORD_OPTION);
		}
		if (config.getBoolean(PASSWORD_INTERACTIVE_OPTION)){
			return _pwdReader.readPassword();
		}
		return "";
	}
}
