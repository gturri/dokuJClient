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

		mapping.put("getTitle", new TitleGetter());
		mapping.put("getAttachments", new AttachmentReader());
		mapping.put("putAttachment", new AttachmentPutter());
		mapping.put("deleteAttachment", new AttachmentDeleter());
		mapping.put("getAttachment", new AttachmentGetter());
		mapping.put("getPagelist", new PageListGetter());
		mapping.put("help", new HelpPrinter(true));
		mapping.put("getVersion", new VersionGetter());
		mapping.put("version",  new CliVersion());
		mapping.put("getTime", new TimeGetter());

		return mapping;
	}

	public static Set<String> CommandList(){
		return BuildMapping().keySet();
	}
}
