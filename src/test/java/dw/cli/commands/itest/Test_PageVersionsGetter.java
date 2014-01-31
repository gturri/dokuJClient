package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_PageVersionsGetter extends TestHelper {
	@org.junit.Test
	public void getPageVersionsWithExplicitOffset() throws Exception{
		//Might seems counter-intuitive since we could expect the same output as the other test
		//with just one line removed
		//
		//In fact:
		// * since provide "--offset 1", we skip the "last non current" revision (ie:  "fifi - edit 1")
		// * since we provide a non zero offset, the current revision isn't added (ie: "lulu - edit 2")
		//hence, only one line remains
		String expected = "rev:start 1356218400 127.0.0.1 C riri - created";

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
