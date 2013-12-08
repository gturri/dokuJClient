package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_BackLinksGetter extends TestHelper {

	@org.junit.Test
	public void getBackLinks() throws Exception {
		assertSuccess("links:start", runWithArguments("getBackLinks", "ns1:dummy"));
	}
}
