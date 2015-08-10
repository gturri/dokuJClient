package dw.cli.commands.itest;

import static org.junit.Assert.*;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_RecentChangesGetter extends TestHelper {
	@org.junit.Test
	public void getRecentChanges() throws Exception {
		Output output = runWithArguments("getRecentChanges", "1356218401");
		assertGenericSuccess(output);
		assertTrue(output.out.contains("rev:start 1356218419 someuser 255 11"));
	}
}
