package dw.cli;

import dw.cli.commands.AttachmentDeleter;
import dw.cli.commands.AttachmentGetter;
import dw.cli.commands.AttachmentPutter;
import dw.cli.commands.AttachmentReader;
import dw.cli.commands.TitleGetter;

public class CommandFactory {
	public Command Build(String command){
		if ( command.equals("getTitle") ){
			return new TitleGetter();
		} else if ( command.equals("getAttachments")){
			return new AttachmentReader();
		} else if ( command.equals("putAttachment")){
			return new AttachmentPutter();
		} else if ( command.equals("deleteAttachment")){
			return new AttachmentDeleter();
		} else if ( command.equals("getAttachment")){
			return new AttachmentGetter();
		}
		throw new IllegalArgumentException("unknown command " + command);
	}
}
