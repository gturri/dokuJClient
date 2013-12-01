package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

abstract public class SimpleCommand extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException { }

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		Output output = new Output();
		output.out = run(dokuClient);
		return output;
	}

	abstract protected String run(DokuJClient dokuClient) throws DokuException;
}
