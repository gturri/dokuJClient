package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_PageGetterAndPutter extends TestHelper {
	private final String pageId = "ns1:dummy";


	@org.junit.Test
	public void putPageAndgetPage() throws Exception{
		final String content1 = "content1";
		final String content2 = "content2";

		assertSuccess("", runWithArguments("putPage", pageId, content1));
		assertSuccess(content1, runWithArguments("getPage", pageId));

		assertSuccess("", runWithArguments("putPage", pageId, content2));
		assertSuccess(content2, runWithArguments("getPage", pageId));
	}

	@org.junit.Test
	public void putPageWithSeveralParts() throws Exception {
		//Put page in a known state
		runWithArguments("putPage", pageId, "");
		assertSuccess("", runWithArguments("getPage", pageId));

		//Actually test
		runWithArguments("putPage",  pageId, "toto", "tata");
		assertSuccess("toto tata", runWithArguments("getPage", pageId));
	}

	//TODO: test 'sum' and 'minor' options
}
