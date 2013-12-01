package dw.cli;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuUnauthorizedException;

public abstract class Command {
	public Output run(DokuJClient dokuClient, String[] commandArguments){
		JSAPResult config;
		try {
			config = parseArguments(commandArguments);
		} catch (ParseOptionException e){
			return new Output(e.getMessage(), -1);
		}

		try {
			return run(dokuClient, config);
		} catch (DokuUnauthorizedException e){
			String helpMessage = getCauseMessage(e)
					+ "\nYou can check permissions with command 'aclCheck'";
			return new Output(helpMessage, -1);
		} catch (DokuException e){
			return new Output(getCauseMessage(e), -1);
		}
	}

	private String getCauseMessage(Throwable e){
		if ( e.getCause() != null ){
			return e.getCause().getMessage();
		} else {
			return e.getMessage();
		}
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
