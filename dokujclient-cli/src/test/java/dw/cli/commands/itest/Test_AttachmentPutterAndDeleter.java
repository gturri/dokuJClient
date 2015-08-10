package dw.cli.commands.itest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static junitx.framework.FileAssert.assertBinaryEquals;

import java.io.File;

import dw.cli.Output;
import dw.cli.itest.TestHelper;
import dw.cli.itest.TestParams;

public class Test_AttachmentPutterAndDeleter extends TestHelper {

	private static String ns = "putAndDelete_ns";
	private static String localFileName = "localToto.gif";
	private final File localFile = new File(localFileName);

	@org.junit.Before
	@org.junit.After
	public void clean() throws Exception {
		localFile.delete();
		runWithArguments("deleteAttachment", ns + ":toto.gif");
	}

	@org.junit.Test
	public void putAndDeleteAttachment() throws Exception {
		assertFalse(runWithArguments("getAttachments", ns).out.contains("toto.gif"));

		Output outputPut = runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFileToUpload);
		assertSuccess("", outputPut);
		assertTrue(runWithArguments("getAttachments", ns).out.contains("toto.gif"));

		Output outputDelete = runWithArguments("deleteAttachment", ns + ":toto.gif");
		assertSuccess("", outputDelete);
		assertFalse(runWithArguments("getAttachments", ns).out.contains("toto.gif"));
	}

	@org.junit.Test
	public void putAttachmentWontOverwriteWithoutTheForceOption() throws Exception{
		//Ensure we start in a clean state
		runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFileToUpload);
		runWithArguments("getAttachment", ns + ":toto.gif", localFileName);
		assertBinaryEquals(new File(TestParams.localFileToUpload), localFile);

		//Try to override the distant file without providing the flag
		Output output = runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFile2ToUpload);
		assertGenericError(output);

		//Assert the attachment hasn't been overrided
		runWithArguments("getAttachment", ns + ":toto.gif", localFileName);
		assertBinaryEquals(new File(TestParams.localFileToUpload), localFile);
	}

	@org.junit.Test
	public void putAttachmentWillOverwriteWithTheForceOption() throws Exception{
		//Ensure we start in a clean state
		runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFileToUpload);
		runWithArguments("getAttachment", ns + ":toto.gif", localFileName);
		assertBinaryEquals(new File(TestParams.localFileToUpload), localFile);

		//Try to override the distant file without providing the flag
		Output output = runWithArguments("putAttachment", ns + ":toto.gif", "-f", TestParams.localFile2ToUpload);
		assertSuccess("", output);

		//Assert the attachment hasn't been overrided
		runWithArguments("getAttachment", ns + ":toto.gif", localFileName);
		assertBinaryEquals(new File(TestParams.localFile2ToUpload), localFile);
	}

	@org.junit.Test
	public void deletingAnUnexistingFileYieldsAnErrorOnlyIfNoForceFlagHasBeenProvided() throws Exception {
		Output outputForceLess = runWithArguments("deleteAttachment", ns + ":unknown_file.gif");
		assertGenericError(outputForceLess);

		Output outputForced = runWithArguments("deleteAttachment", "-f", ns + ":unknown_file.gif");
		assertSuccess("", outputForced);
	}

	@org.junit.Test
	public void getAFileToANonWritablePlaceDoesntCrash() throws Exception{
		//Ensure we start in a clean state
		runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFileToUpload);
		runWithArguments("getAttachment", ns + ":toto.gif", localFileName);
		assertTrue(runWithArguments("getAttachments", ns).out.contains("toto.gif"));

		//Actually test
		Output output = runWithArguments("getAttachment", ns + ":toto.gif", "/home/whatever/icantwrite/here.jpg");
		assertGenericError(output);
	}

	@org.junit.Test
	public void putAFileWhichDoesntExistDoesntCrash() throws Exception {
		Output output = runWithArguments("putAttachment", ns + ":toto.gif", "unexistingFile.jpg");
		assertGenericError(output);
	}
}
