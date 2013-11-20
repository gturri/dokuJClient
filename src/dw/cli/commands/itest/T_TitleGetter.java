package dw.cli.commands.itest;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class T_TitleGetter extends TestHelper {

	@org.junit.Test
	public void getTitle() throws Exception{
		Output output = runWithArguments("getTitle");
		assertSuccess("test xmlrpc", output);
	}
}
