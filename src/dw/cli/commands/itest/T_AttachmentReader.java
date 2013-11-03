package dw.cli.commands.itest;

import static org.junit.Assert.assertEquals;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class T_AttachmentReader extends TestHelper {
	@org.junit.Test
	public void getAttachments() throws Exception {
		Output output = runWithArguments("getAttachments", "ro_for_tests");
		assertEquals("ro_for_tests:img1.gif", output.out);
		assertEquals("", output.err);
		assertEquals(0, output.exitCode);
	}

	@org.junit.Test
	public void getAttachmentsLongListingFormat() throws Exception {
		Output output = runWithArguments("getAttachments", "-l", "ro_for_tests");
		assertEquals("255 67 Mon Dec 24 20:11:00 CET 2012 ro_for_tests:img1.gif", output.out);
		assertEquals("", output.err);
		assertEquals(0, output.exitCode);
	}

	@org.junit.Test
	public void getAttachmentsMissingNamespaceShouldPrintError() throws Exception {
		Output output = runWithArguments("getAttachments");
		assertNotNullOrEmpty(output.err);
		assertEquals("", output.out);
		assertNotZero(output.exitCode);
	}
}
