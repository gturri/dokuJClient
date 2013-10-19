package dw.cli;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CliOptions {
	public URL url;
	public String user;
	public String password;
	public String command;
	public List<String> commandArguments = new ArrayList<String>();

	@Override
	public String toString(){
		return "user: " + (user == null ? "null" : user)
				+ ", password: " + (password == null ? "null" : password)
				+ ", url: " + (url == null ? "null" : url.toString()
				+ ", command: " + (command == null ? "null" : command));
	}
}
