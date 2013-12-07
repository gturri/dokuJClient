package dw.cli.commands;

import java.util.List;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

abstract public class ItemListToStringCommand<T> extends Command {

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		List<T> items = query(dokuClient);
		return new Output(itemsToString(items));
	}

	abstract protected List<T> query(DokuJClient dokuClient) throws DokuException;

	private String itemsToString(List<T> items) {
		LineConcater concater = new LineConcater();
		for(T item : items){
			concater.addLine(itemToString(item));
		}
		return concater.toString();
	}

	abstract protected String itemToString(T item);
}
