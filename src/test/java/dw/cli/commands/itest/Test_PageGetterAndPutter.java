package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_PageGetterAndPutter extends TestHelper {
	private final String pageId = "ns1:dummy";

	@org.junit.Before
	public void setUp() throws Exception{
		runWithArguments("putPage", pageId, "");
	}


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
		runWithArguments("putPage",  pageId, "toto", "tata");
		assertSuccess("toto tata", runWithArguments("getPage", pageId));
	}

	@org.junit.Test
	public void canPutAPageWithASummary() throws Exception {
		//Sleep because DW behaves badly wrt revisions,when there are more than 1 version per second
		Thread.sleep(1000, 0);
		assertSuccess("", runWithArguments("putPage", pageId, "--summary", "my summary 1", "some content 2"));
		assertLastModificationSummary("my summary 1", runWithArguments("getPageVersions", pageId));

		//Sleep because DW behaves badly wrt revisions,when there are more than 1 version per second
		Thread.sleep(1000, 0);
		assertSuccess("", runWithArguments("putPage", pageId, "--summary", "my other summary", "some other content"));
		assertLastModificationSummary("my other summary", runWithArguments("getPageVersions", pageId));
	}
}
