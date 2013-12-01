package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_PageVersionGetter extends TestHelper {
	private final String pageId = "rev:start";

	@org.junit.Test
	public void getPageVersion() throws Exception{
		assertSuccess("version 1", runWithArguments("getPageVersion", pageId, "1356218400"));
		assertSuccess("", runWithArguments("getPageVersion", pageId, "1356218401"));
		assertSuccess("v2", runWithArguments("getPageVersion", pageId, "1356218411"));
	}
}
