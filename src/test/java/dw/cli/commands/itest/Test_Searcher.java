package dw.cli.commands.itest;

import static org.junit.Assert.*;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_Searcher extends TestHelper {
	@org.junit.Test
	public void search() throws Exception{
		Output output = runWithArguments("search", "amet");
		assertSuccess("nssearch:page3\nnssearch:start", output);
	}

	@org.junit.Test
	public void searchWithLongSwitch() throws Exception {
		Output output = runWithArguments("search", "-l", "amet");
		assertSuccess("2 1375376400 1375376400 Page 3 title 197 nssearch:page3\n1 1383544045 1383544045  56 nssearch:start", output);
	}

	@org.junit.Test
	public void searchWithSnippetSwitch() throws Exception {
		Output output = runWithArguments("search", "--snippet", "amet");
		assertGenericSuccess(output);
		assertTrue(output.out.contains("nssearch:page3\n> ======Page 3 title======"));
	}
}
