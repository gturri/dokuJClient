package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_PageAppender extends TestHelper {
	private final String pageId = "ns1:dummy";
	private final String initialContent = "put page in a clean state.";

	@org.junit.Before
	public void setUp() throws Exception{
		runWithArguments("putPage", pageId, initialContent);
	}

	@org.junit.Test
	public void appendPage() throws Exception {
		String append1 = "text appened";
		assertSuccess("", runWithArguments("appendPage", pageId, append1));
		assertSuccess(initialContent + append1, runWithArguments("getPage", pageId));

		String append2 = "final text";
		assertSuccess("", runWithArguments("appendPage", pageId, append2));
		assertSuccess(initialContent + append1 + append2, runWithArguments("getPage", pageId));
	}

	@org.junit.Test
	public void appendPageWithSeveralParts() throws Exception {
		String part1 = "toto";
		String part2 = "tata";
		assertSuccess("", runWithArguments("appendPage", pageId, part1, part2));
		assertSuccess(initialContent + part1 + " " + part2, runWithArguments("getPage", pageId));

		String part3 = "tutu";
		assertSuccess("", runWithArguments("appendPage", pageId, part3));
		assertSuccess(initialContent + part1 + " " + part2 + part3, runWithArguments("getPage", pageId));
	}

	@org.junit.Test
	public void appendPageWithSummary() throws Exception{
		//Sleep because DW behaves badly wrt revisions,when there are more than 1 version per second
		Thread.sleep(1000, 0);
		String summary1 = "I needed this change";
		assertSuccess("", runWithArguments("appendPage", "--summary", summary1, pageId, "some text"));
		assertLastModificationSummary(summary1, runWithArguments("getPageVersions", pageId));

		Thread.sleep(1000, 0);
		String summary2 = "fixed a typo";
		assertSuccess("", runWithArguments("appendPage", "--summary", summary2, pageId, "some other text"));
		assertLastModificationSummary(summary2, runWithArguments("getPageVersions", pageId));
	}
}
