package dw.cli.itest;

import static org.junit.Assert.*;
import dw.cli.Program;
import dw.xmlrpc.itest.TestParams;

public class T_Command {

	@org.junit.Test
	public void T_getTitle() throws Exception{
		String[] arguments = buildArguments("getTitle");
		assertEquals("test xmlrpc", Program.run(arguments));
	}

	private String[] buildArguments(String command){
		TestParams params = new TestParams("dokuwiki-2013-05-10", "Release 2013-05-10 \"Weatherwax\"", 8, 2);

		return new String[]{
				"-u", TestParams.user,
				"-p", TestParams.password,
				"--url", params.url,
				command
		};
	}
}
