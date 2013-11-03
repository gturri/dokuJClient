package dw.cli.commands.itest;

import static org.junit.Assert.assertEquals;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class T_TitleGetter extends TestHelper {

	@org.junit.Test
	public void getTitle() throws Exception{
		Output output = runWithArguments("getTitle");
		assertEquals("test xmlrpc", output.out);
		assertEquals("", output.err);
		assertEquals(0, output.exitCode);
	}
}
