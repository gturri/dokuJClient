package dw.cli.commands.itest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_AllPagesGetter extends TestHelper {
	@org.junit.Test
	public void getAllPages() throws Exception {
		Output output = runWithArguments("getAllPages");
		assertGenericSuccess(output);

		String[] pages = output.out.split("\n");
		assertTrue(pages.length >= 11); //Don't assertEquals because other tests may create new pages
		assertHasExactlyOnceThisLine("nssearch:page3", pages);
	}

	@org.junit.Test
	public void getAllPagesWithLongFlag() throws Exception {
		Output output = runWithArguments("getAllPages", "-l");
		assertGenericSuccess(output);

		String[] pages = output.out.split("\n");
		assertTrue(pages.length >= 11); //Don't assertEquals because other tests may create new pages
		assertHasExactlyOnceThisLine("nssearch:page3 255 197", pages);
	}

	private void assertHasExactlyOnceThisLine(String expected, String[] actual){
		boolean found = false;
		for(String line : actual){
			if ( line.equals(expected) ){
				if ( found  ){
					fail("line found twice: " + expected);
				}
				found = true;
			}
		}
		if ( ! found ){
			fail("line not found: " + expected);
		}
	}
}
