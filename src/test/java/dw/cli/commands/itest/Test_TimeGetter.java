package dw.cli.commands.itest;

import static org.junit.Assert.*;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_TimeGetter extends TestHelper {

	@org.junit.Test
	public void getTime() throws Exception{
		Output output = runWithArguments("getTime");
		assertGenericSuccess(output);
		assertIsTimestamp(output.out);
	}

	private void assertIsTimestamp(String string) {
		assertEquals(10, string.length());
	}
}
