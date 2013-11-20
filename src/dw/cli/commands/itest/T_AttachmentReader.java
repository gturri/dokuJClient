package dw.cli.commands.itest;

import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class T_AttachmentReader extends TestHelper {
	@org.junit.Test
	public void getAttachments() throws Exception {
		Output output = runWithArguments("getAttachments", "ro_for_tests");
		assertSuccess("ro_for_tests:img1.gif", output);
	}

	@org.junit.Test
	public void getAttachmentsLongListingFormat() throws Exception {
		Output output = runWithArguments("getAttachments", "-l", "ro_for_tests");
		assertSuccess("255 67 Mon Dec 24 20:11:00 CET 2012 ro_for_tests:img1.gif", output);
	}

	@org.junit.Test
	public void getAttachmentsMissingNamespaceShouldPrintError() throws Exception {
		Output output = runWithArguments("getAttachments");
		assertGenericError(output);
	}
}
