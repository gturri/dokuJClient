package dw.cli.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageDW;
import dw.xmlrpc.exception.DokuException;

public class PageListGetter extends ItemListToStringCommand<PageDW> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addLongFormatSwitch(jsap);
		jsap.registerParameter(new UnflaggedOption("namespace").setRequired(true));
		jsap.registerParameter(new FlaggedOption("depth")
			.setStringParser(JSAP.INTEGER_PARSER)
			.setRequired(false)
			.setLongFlag("depth"));
	}

	@Override
	protected List<PageDW> query(DokuJClient dokuClient) throws DokuException {
		Map<String, Object> clientOptions = buildClientOption();
		return dokuClient.getPagelist(_config.getString("namespace"), clientOptions);
	}

	private Map<String, Object> buildClientOption() {
		Map<String, Object> result = new TreeMap<String, Object>();

		if ( _config.contains("depth") ){
			result.put("depth",  _config.getInt("depth"));
		}

		return result;
	}

	@Override
	protected String itemToString(PageDW page) {
		if ( _config.getBoolean("longFormat") ){
			return pageToLongString(page);
		} else {
			return pageToString(page);
		}
	}

	private String pageToLongString(PageDW page) {
		return page.size()
				+ " " + page.mtime()
				+ " " + page.version()
				+ " " + page.id();
	}

	private String pageToString(PageDW page) {
		return page.id();
	}

}
