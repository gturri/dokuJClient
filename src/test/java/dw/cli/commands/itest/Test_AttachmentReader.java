package dw.cli.commands.itest;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class Test_AttachmentReader extends TestHelper {
	@org.junit.Test
	public void getAttachments() throws Exception {
		Output output = runWithArguments("getAttachments", "ro_for_tests");
		assertSuccess("ro_for_tests:img1.gif", output);
	}

	@org.junit.Test
	public void getAttachmentsLongListingFormat() throws Exception {
		Output output = runWithArguments("getAttachments", "-l", "ro_for_tests");
		assertSuccessRegex("255 67 " + dateRegex + " ro_for_tests:img1.gif", output);
	}

	@org.junit.Test
	public void getAttachmentsMissingNamespaceShouldPrintError() throws Exception {
		Output output = runWithArguments("getAttachments");
		assertGenericError(output);
	}
}
