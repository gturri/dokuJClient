package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class CliVersion extends Command {
	//version automatically updated by ant
	public static final String CliVersion = "2.1.0-dev";

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config)	throws DokuException {
		Output output = new Output();
		output.out = CliVersion;
		return output;
	}

}
