package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_PageVersionsGetter extends TestHelper {
	@org.junit.Test
	public void getPageVersionsWithExplicitOffset() throws Exception{
		String expected = "rev:start 1356218411 127.0.0.1 E fifi - edit 1"
				+ "\nrev:start 1356218400 127.0.0.1 C riri - created";

		assertSuccess(expected, runWithArguments("getPageVersions", "rev:start", "--offset", "1"));
	}

	@org.junit.Test
	public void getPageVersionsWithoutExplicitOffset() throws Exception{
		String expected = "rev:start 1356218419 127.0.0.1 E lulu - edit 2"
				+ "\nrev:start 1356218411 127.0.0.1 E fifi - edit 1"
				+ "\nrev:start 1356218400 127.0.0.1 C riri - created";
		assertSuccess(expected, runWithArguments("getPageVersions", "rev:start"));
	}
}
