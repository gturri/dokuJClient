package dw.cli.commands.itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import dw.cli.Output;
import dw.cli.itest.TestHelper;
import dw.xmlrpc.itest.TestParams;

public class T_AttachmentPutterAndDeleter extends TestHelper {

	private static String ns = "putAndDelete_ns";
	private final File localFile = new File("localToto.gif");

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
		assertEquals("", outputPut.out);
		assertEquals("", outputPut.err);
		assertEquals(0, outputPut.exitCode);

		assertTrue(runWithArguments("getAttachments", ns).out.contains("toto.gif"));

		Output outputDelete = runWithArguments("deleteAttachment", ns + ":toto.gif");
		assertEquals("", outputDelete.out);
		assertEquals("", outputDelete.err);
		assertEquals(0, outputDelete.exitCode);

		assertFalse(runWithArguments("getAttachments", ns).out.contains("toto.gif"));
	}

	@org.junit.Test
	public void putAttachmentWontOverwriteWithoutTheForceOption() throws Exception{
		//Ensure we start in a clean state
		runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFileToUpload);
		runWithArguments("getAttachment", ns + ":toto.gif", "localToto.gif");
		assertFileEquals(new File(TestParams.localFileToUpload), localFile);

		//Try to override the distant file without providing the flag
		Output output = runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFile2ToUpload);
		assertNotNullOrEmpty(output.err);
		assertEquals("", output.out);
		assertNotZero(output.exitCode);

		//Assert the attachment hasn't been overrided
		runWithArguments("getAttachment", ns + ":toto.gif", "localToto.gif");
		assertFileEquals(new File(TestParams.localFileToUpload), localFile);
	}

	@org.junit.Test
	public void putAttachmentWillOverwriteWithTheForceOption() throws Exception{
		//Ensure we start in a clean state
		runWithArguments("putAttachment", ns + ":toto.gif", TestParams.localFileToUpload);
		runWithArguments("getAttachment", ns + ":toto.gif", "localToto.gif");
		assertFileEquals(new File(TestParams.localFileToUpload), localFile);

		//Try to override the distant file without providing the flag
		Output output = runWithArguments("putAttachment", ns + ":toto.gif", "-f", TestParams.localFile2ToUpload);
		assertEquals("", output.err);
		assertEquals("", output.out);
		assertEquals(0, output.exitCode);

		//Assert the attachment hasn't been overrided
		runWithArguments("getAttachment", ns + ":toto.gif", "localToto.gif");
		assertFileEquals(new File(TestParams.localFile2ToUpload), localFile);
	}

	@org.junit.Test
	public void deletingAnUnexistingFileYieldsAnErrorOnlyIfNoForceFlagHasBeenProvided() throws Exception {
		Output outputForceLess = runWithArguments("deleteAttachment", ns + ":unknown_file.gif");
		assertEquals("", outputForceLess.out);
		assertNotNullOrEmpty(outputForceLess.err);
		assertNotZero(outputForceLess.exitCode);

		Output outputForced = runWithArguments("deleteAttachment", "-f", ns + ":unknown_file.gif");
		assertEquals("", outputForced.out);
		assertEquals("", outputForced.err);
		assertEquals(0, outputForced.exitCode);
	}
}
