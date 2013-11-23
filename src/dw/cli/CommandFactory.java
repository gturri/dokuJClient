package dw.cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dw.cli.commands.AttachmentDeleter;
import dw.cli.commands.AttachmentGetter;
import dw.cli.commands.AttachmentPutter;
import dw.cli.commands.AttachmentReader;
import dw.cli.commands.HelpPrinter;
import dw.cli.commands.PageListGetter;
import dw.cli.commands.TitleGetter;
import dw.cli.commands.VersionGetter;

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

		return mapping;
	}

	public static Set<String> CommandList(){
		return BuildMapping().keySet();
	}
}
