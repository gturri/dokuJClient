package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_PageInfoGetter extends TestHelper {
	@org.junit.Test
	public void getPageInfo() throws Exception {
		assertSuccess("rev:start 1356218419 lulu", runWithArguments("getPageInfo", "rev:start"));
	}
}
