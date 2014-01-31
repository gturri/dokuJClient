package dw.cli.commands;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

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
		Function<T, String> converter = new Function<T, String>(){
			@Override
			public String apply(T item) { return itemToString(item); }
		};

		return Joiner.on("\n").join(Lists.transform(items, converter));
	}

	abstract protected String itemToString(T item);
}
