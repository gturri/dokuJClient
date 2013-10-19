package dw.cli;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

public class OptionParser {
	private final boolean _success;
	private final CliOptions _cliOptions;
	private final String _helpMessage;

	public OptionParser(String[] args){
		JSAP jsap = new JSAP();
		boolean success;
		String helpMessage;
		CliOptions cliOptions;

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

			jsap.registerParameter(new UnflaggedOption("command")
					.setStringParser(JSAP.STRING_PARSER)
					.setRequired(true)
					.setGreedy(true));

			JSAPResult config = jsap.parse(args);
			if ( ! config.success() ){
				success = false;
				helpMessage = "";
	            for (@SuppressWarnings("rawtypes") java.util.Iterator errs = config.getErrorMessageIterator();
	            		errs.hasNext();) {
	            	helpMessage += errs.next() + "\n";
	            }
	            helpMessage += jsap.getUsage();
	            cliOptions = null;
			} else {
				success = true;
				helpMessage = jsap.getUsage();
				cliOptions = new CliOptions();
				cliOptions.password = config.getString("password");
				cliOptions.user = config.getString("user");
				cliOptions.url = config.getURL("url");
				cliOptions.command = config.getStringArray("command")[0];

				for ( int i = 1 ; i < config.getStringArray("command").length ; i++){
					cliOptions.commandArguments.add(config.getStringArray("command")[i]);
				}
			}
		} catch (JSAPException e) {
			success = false;
			helpMessage = e.toString();
			cliOptions = null;
		}

		_success = success;
		_helpMessage = helpMessage;
		_cliOptions = cliOptions;
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
}
