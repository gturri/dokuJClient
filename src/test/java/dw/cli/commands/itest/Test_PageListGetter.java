package dw.cli.commands.itest;

import static org.junit.Assert.*;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_PageListGetter extends TestHelper {
	@org.junit.Test
	public void getPagelist() throws Exception {
		Output output = runWithArguments("getPagelist", "ns1");
		assertSuccess("ns1:dummy\nns1:start", output);
	}

	@org.junit.Test
	public void getPageListLongFormat() throws Exception {
		Output output = runWithArguments("getPagelist", "-l", "nswithanotherns:otherns");
		assertSuccess("4 1375372800 1375372800 nswithanotherns:otherns:page", output);
	}

	@org.junit.Test
	public void getPageListWithDepth() throws Exception {
		Output shallowOutput = runWithArguments("getPagelist", "--depth", "2", "nswithanotherns");
		assertGenericSuccess(shallowOutput);
		assertTrue(shallowOutput.out.contains("nswithanotherns:start"));
		assertTrue(shallowOutput.out.contains("nswithanotherns:dummy"));
		assertFalse(shallowOutput.out.contains("nswithanotherns:dummy"));

		Output deepOutput = runWithArguments("getPagelist", "--depth", "3", "nswithanotherns");
		assertGenericSuccess(deepOutput);
		assertTrue(deepOutput.out.contains("nswithanotherns:start"));
		assertTrue(deepOutput.out.contains("nswithanotherns:dummy"));
		assertTrue(deepOutput.out.contains("nswithanotherns:otherns:page"));
	}
}
