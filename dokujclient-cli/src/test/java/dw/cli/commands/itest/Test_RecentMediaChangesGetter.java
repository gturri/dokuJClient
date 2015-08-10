package dw.cli.commands.itest;

import static org.junit.Assert.*;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_RecentMediaChangesGetter extends TestHelper {
	@org.junit.Test
	public void getRecentMediaChanges() throws Exception {
		Output output = runWithArguments("getRecentMediaChanges", "1356383400");

		assertGenericSuccess(output);
		assertTrue(output.out.contains("ro_for_tests:img1.gif fifi 255 67"));
	}
}
