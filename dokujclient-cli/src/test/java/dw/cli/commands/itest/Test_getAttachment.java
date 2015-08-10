package dw.cli.commands.itest;

import static org.junit.Assert.*;
import static junitx.framework.FileAssert.assertBinaryEquals;

import java.io.File;

import dw.cli.Output;
import dw.cli.itest.TestHelper;
import dw.cli.itest.TestParams;

public class Test_getAttachment extends TestHelper {
	private final File localFile = new File("myFile.gif");

	@org.junit.Before
	@org.junit.After
	public void clean(){
		localFile.delete();
	}

	@org.junit.Test
	public void canGetAttachment() throws Exception {
		//Make sure we're in a clean state
		assertFalse(localFile.exists());

		Output output = runWithArguments("getAttachment", "ro_for_tests:img1.gif", "myFile.gif");
		assertSuccess("", output);
		assertTrue(localFile.exists());
		assertBinaryEquals(new File(TestParams.localFileToUpload), new File("myFile.gif"));
	}

	@org.junit.Test
	public void attachmentDoesntExist() throws Exception {
		Output output = runWithArguments("getAttachment", "ro_for_tests:unexisting_file.gif", "myFile.gif");
		assertGenericError(output);
	}
}
