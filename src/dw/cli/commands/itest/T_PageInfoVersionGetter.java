package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_PageInfoVersionGetter extends TestHelper {
	@org.junit.Test
	public void getPageInfoVersion() throws Exception {
		String expected = "rev:start 1356218411 fifi";
		assertSuccess(expected, runWithArguments("getPageInfoVersion", "rev:start", "1356218411"));

	}
}
