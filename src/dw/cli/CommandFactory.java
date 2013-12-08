package dw.cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dw.cli.commands.*;

public class CommandFactory {
	public Command Build(String command){
		Map<String, Command> mapping = BuildMapping();
		if ( mapping.containsKey(command) ){
			return mapping.get(command);
		}

		return new HelpPrinter(false);
	}

	private static Map<String, Command> BuildMapping(){
		Map<String, Command> mapping = new HashMap<String, Command>();

		mapping.put("aclCheck", new AclChecker());
		mapping.put("appendPage", new PagePutter(true));
		mapping.put("deleteAttachment", new AttachmentDeleter());
		mapping.put("getAttachment", new AttachmentGetter());
		mapping.put("getAttachments", new AttachmentReader());
		mapping.put("getPage",  new PageGetter());
		mapping.put("getPageHTML", new PageHtmlGetter());
		mapping.put("getPageHTMLVersion", new PageHtmlGetter(true));
		mapping.put("getPageInfo", new PageInfoGetter());
		mapping.put("getPageInfoVersion", new PageInfoGetter(true));
		mapping.put("getPageVersion", new PageVersionGetter());
		mapping.put("getPageVersions", new PageVersionsGetter());
		mapping.put("getPagelist", new PageListGetter());
		mapping.put("getRPCVersionSupported", new RPCVersionSupportedGetter());
		mapping.put("getTime", new TimeGetter());
		mapping.put("getTitle", new TitleGetter());
		mapping.put("getVersion", new VersionGetter());
		mapping.put("getXMLRPCAPIVersion", new XMLRPCAPIVersionGetter());
		mapping.put("help", new HelpPrinter(true));
		mapping.put("listLinks", new LinksLister());
		mapping.put("putAttachment", new AttachmentPutter());
		mapping.put("putPage", new PagePutter());
		mapping.put("search", new Searcher());
		mapping.put("version",  new CliVersion());
		mapping.put("getAllPages", new AllPageGetter());
		mapping.put("getBackLinks", new BackLinksGetter());
		mapping.put("getRecentChanges", new RecentChangesGetter());
		mapping.put("getRecentMediaChanges", new RecentMediaChangesGetter());

		return mapping;
	}

	public static Set<String> CommandList(){
		return BuildMapping().keySet();
	}
}
