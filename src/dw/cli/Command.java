package dw.cli;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuBadUrlException;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuUnauthorizedException;

public abstract class Command {
	protected JSAPResult _config;

	public Output run(DokuJClient dokuClient, String[] commandArguments){
		try {
			parseArguments(commandArguments);
		} catch (ParseOptionException e){
			return new Output(e.getMessage(), -1);
		}

		try {
			return run(dokuClient);
		} catch (DokuUnauthorizedException e){
			String helpMessage = getCauseMessage(e)
					+ "\nYou can check permissions with command 'aclCheck'";
			return new Output(helpMessage, -1);
		} catch(DokuBadUrlException e){
			//No need to retrieve the inner message: the user just need to know he got the bad url
			return new Output(e.getMessage(), -1);
		} catch (DokuException e){
			return new Output(getCauseMessage(e), -1);
		}
	}

	protected void addLongFormatSwitch(JSAP jsap) throws JSAPException{
		jsap.registerParameter(new Switch("longFormat").setShortFlag('l'));
	}

	protected void addPageIdOption(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true));
	}

	private String getCauseMessage(Throwable e){
		if ( e.getCause() != null ){
			return e.getCause().getMessage();
		} else {
			return e.getMessage();
		}
	}

	protected void parseArguments(String[] arguments) throws ParseOptionException{
		JSAP jsap = new JSAP();

		try {
			registerParameters(jsap);
		} catch (JSAPException e) {
			throw new ParseOptionException(e.toString(), e);
		}

		_config = jsap.parse(arguments);
		if ( ! _config.success() ){
			String helpMessage = "";
            for (@SuppressWarnings("rawtypes") java.util.Iterator errs = _config.getErrorMessageIterator();
            		errs.hasNext();) {
            	helpMessage += errs.next() + "\n";
            }
            helpMessage += jsap.getUsage();
            throw new ParseOptionException(helpMessage);
		}
	}

	protected abstract void registerParameters(JSAP jsap) throws JSAPException;

	protected abstract Output run(DokuJClient dokuClient) throws DokuException;

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
