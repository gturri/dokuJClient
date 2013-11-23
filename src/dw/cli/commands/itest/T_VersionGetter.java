package dw.cli.commands.itest;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class T_VersionGetter extends TestHelper {
	@org.junit.Test
	public void getVersion() throws Exception {
		Output output = runWithArguments("getVersion");
		assertSuccess(params.wikiVersion, output);
	}
}
