package dw.cli.commands;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

abstract public class SimpleCommand extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException { }

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		return new Output(query(dokuClient));
	}

	abstract protected String query(DokuJClient dokuClient) throws DokuException;
}
