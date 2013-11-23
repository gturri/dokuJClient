package dw.cli;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public abstract class Command {
	public Output run(DokuJClient dokuClient, String[] commandArguments) throws DokuException{
		JSAPResult config;
		try {
			config = parseArguments(commandArguments);
		} catch (ParseOptionException e){
			Output output = new Output();
			output.err = e.getMessage();
			output.exitCode = -1;
			return output;
		}

		return run(dokuClient, config);
	}

	protected JSAPResult parseArguments(String[] arguments) throws ParseOptionException{
		JSAP jsap = new JSAP();

		try {
			registerParameters(jsap);
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

	protected abstract void registerParameters(JSAP jsap) throws JSAPException;

	protected abstract Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException;

	public String getUsage() {
		JSAP jsap = new JSAP();

		try {
			registerParameters(jsap);
		} catch (JSAPException e) {
			throw new RuntimeException("Something wrong happened", e);
		}

		return jsap.getUsage();

	}
}
