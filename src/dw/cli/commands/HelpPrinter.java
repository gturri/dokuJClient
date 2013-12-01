package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.CommandFactory;
import dw.cli.OptionParser;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class HelpPrinter extends Command {

	private final boolean _helpExplicitlyWanted;

	public HelpPrinter(boolean helpExplicitlyWanted){
		_helpExplicitlyWanted = helpExplicitlyWanted;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("command")
		.setStringParser(JSAP.STRING_PARSER)
		.setRequired(false));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config)	throws DokuException {
		if ( ! _helpExplicitlyWanted ){
			return new Output(OptionParser.getUsage(), -1);
		} else {
			if ( config.contains("command") ){
				String commandName = config.getString("command");
				Command command = new CommandFactory().Build(commandName);
				return new Output("Syntax for " + commandName + ": " + command.getUsage());
			} else {
				return new Output(OptionParser.getUsage());
			}
		}
	}
}
