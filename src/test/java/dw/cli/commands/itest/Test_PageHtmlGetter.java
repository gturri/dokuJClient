package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_PageHtmlGetter extends TestHelper {
	private final String pageId = "rev:start";

	@org.junit.Test
	public void getPageHTML() throws Exception {
		assertSuccess("\n<p>\n3rd version\n</p>\n", runWithArguments("getPageHTML", pageId));
	}

	@org.junit.Test
	public void getPageHTMLVersion() throws Exception {
		assertSuccess("\n<p>\nv2\n</p>\n", runWithArguments("getPageHTMLVersion", pageId, "1356218411"));
	}
}
