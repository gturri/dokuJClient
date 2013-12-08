package dw.cli.commands.itest;

import static org.junit.Assert.*;
import dw.cli.Output;
import dw.cli.itest.TestHelper;

public class T_AttachmentInfoGetter extends TestHelper {
	@org.junit.Test
	public void getAttachmentInfo() throws Exception {
		Output output = runWithArguments("getAttachmentInfo", "ro_for_tests:img1.gif");

		assertGenericSuccess(output);
		assertTrue(output.out.contains("ro_for_tests:img1.gif 67"));
	}
}
