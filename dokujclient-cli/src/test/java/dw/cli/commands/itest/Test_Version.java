package dw.cli.commands.itest;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_Version extends TestHelper {

	@org.junit.Test
	public void optionVersion() throws Exception{
		Output output = runWithArguments("--version");
		assertGenericSuccess(output);
		assertNotNullOrEmpty(output.out);
	}
}
