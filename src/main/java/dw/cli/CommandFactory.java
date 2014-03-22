package dw.cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dw.cli.commands.*;

public class CommandFactory {
	public Command build(String command){
		Map<String, Command> mapping = buildMapping();
		if ( mapping.containsKey(command) ){
			return mapping.get(command);
		}

		return new HelpPrinter(false);
	}

	private static Map<String, Command> buildMapping(){
		Map<String, Command> mapping = new HashMap<String, Command>();

		mapping.put("aclCheck", new AclChecker());
		mapping.put("appendPage", new PagePutter(true));
		mapping.put("deleteAttachment", new AttachmentDeleter());
		mapping.put("getAllPages", new AllPageGetter());
		mapping.put("getAttachment", new AttachmentGetter());
		mapping.put("getAttachmentInfo", new AttachmentInfoGetter());
		mapping.put("getAttachments", new AttachmentReader());
		mapping.put("getBackLinks", new BackLinksGetter());
		mapping.put("getPage",  new PageGetter());
		mapping.put("getPageHTML", new PageHtmlGetter());
		mapping.put("getPageHTMLVersion", new PageHtmlGetter(true));
		mapping.put("getPageInfo", new PageInfoGetter());
		mapping.put("getPageInfoVersion", new PageInfoGetter(true));
		mapping.put("getPageVersion", new PageVersionGetter());
		mapping.put("getPageVersions", new PageVersionsGetter());
		mapping.put("getPagelist", new PageListGetter());
		mapping.put("getRecentChanges", new RecentChangesGetter());
		mapping.put("getRecentMediaChanges", new RecentMediaChangesGetter());
		mapping.put("getRPCVersionSupported", new RPCVersionSupportedGetter());
		mapping.put("getTime", new TimeGetter());
		mapping.put("getTitle", new TitleGetter());
		mapping.put("getVersion", new VersionGetter());
		mapping.put("getXMLRPCAPIVersion", new XMLRPCAPIVersionGetter());
		mapping.put("help", new HelpPrinter(true));
		mapping.put("listLinks", new LinksLister());
		mapping.put("lock", new LocksSetter(false));
		mapping.put("putAttachment", new AttachmentPutter());
		mapping.put("putPage", new PagePutter());
		mapping.put("search", new Searcher());
		mapping.put("unlock", new LocksSetter(true));
		mapping.put("version",  new CliVersion());

		return mapping;
	}

	public static Set<String> commandList(){
		return buildMapping().keySet();
	}
}
