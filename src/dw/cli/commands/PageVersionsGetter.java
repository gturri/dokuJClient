package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageVersion;
import dw.xmlrpc.exception.DokuException;

public class PageVersionsGetter extends ItemListToStringCommand<PageVersion> {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		addPageIdOption(jsap);
		jsap.registerParameter(new FlaggedOption("offset")
			.setStringParser(JSAP.INTEGER_PARSER)
			.setRequired(false)
			.setLongFlag("offset"));
	}

	@Override
	protected List<PageVersion> query(DokuJClient dokuClient) throws DokuException {
		String pageId = _config.getString("pageId");

		if ( _config.contains("offset" ) ){
			return dokuClient.getPageVersions(pageId, _config.getInt("offset"));
		} else {
			return dokuClient.getPageVersions(pageId);
		}
	}

	@Override
	protected String itemToString(PageVersion version) {
		return version.pageId()
				+ " " + version.version()
				+ " " + version.ip()
				+ " " + version.type()
				+ " " + version.author()
				+ " - " + version.summary();
	}

}
