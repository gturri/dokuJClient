package dw.cli;

import java.net.URL;

public class CliOptions {
	public URL url;
	public String user;
	public String password;
	public String command;
	public String[] commandArguments;
	public boolean debug;

	@Override
	public String toString(){
		return "user: " + user
				+ ", password: " + password
				+ ", debug: " + debug
				+ ", url: " + (url == null ? "null" : url.toString()
				+ ", command: " + command);
	}
}
