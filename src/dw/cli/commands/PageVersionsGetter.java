package dw.cli.commands;

import java.util.List;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageVersion;
import dw.xmlrpc.exception.DokuException;

public class PageVersionsGetter extends Command {

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
		jsap.registerParameter(new UnflaggedOption("pageId").setRequired(true));
		jsap.registerParameter(new FlaggedOption("offset")
			.setStringParser(JSAP.INTEGER_PARSER)
			.setRequired(false)
			.setLongFlag("offset"));
	}

	@Override
	protected Output run(DokuJClient dokuClient, JSAPResult config) throws DokuException {
		String pageId = config.getString("pageId");
		List<PageVersion> versions;

		if ( config.contains("offset" ) ){
			versions = dokuClient.getPageVersions(pageId, config.getInt("offset"));
		} else {
			versions = dokuClient.getPageVersions(pageId);
		}

		return new Output(pageVersionsToString(versions));
	}

	private String pageVersionsToString(List<PageVersion> versions) {
		LineConcater concater = new LineConcater();

		for(PageVersion version : versions){
			concater.addLine(pageVersionToString(version));
		}

		return concater.toString();
	}

	private String pageVersionToString(PageVersion version) {
		return version.pageId()
				+ " " + version.version()
				+ " " + version.ip()
				+ " " + version.type()
				+ " " + version.author()
				+ " - " + version.summary();
	}

}
