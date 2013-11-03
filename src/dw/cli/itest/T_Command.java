package dw.cli.itest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import dw.cli.Output;
import dw.cli.Program;
import dw.xmlrpc.itest.TestParams;

public class T_Command {

	@org.junit.Test
	public void getTitle() throws Exception{
		Output output = runWithArguments("getTitle");
		assertEquals("test xmlrpc", output.out);
		assertEquals("", output.err);
		assertEquals(0, output.exitCode);
	}

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
	public void putAndDeleteAttachment() throws Exception {
		String ns = "putAndDelete_ns";

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
	public void getAttachmentsMissingNamespaceShouldPrintError() throws Exception {
		String[] arguments = buildArguments("getAttachments");
		Output output = Program.run(arguments);
		assertNotNullOrEmpty(output.err);
		assertEquals("", output.out);
		assertNotZero(output.exitCode);
	}

	private void assertNotNullOrEmpty(String str){
		assertNotNull(str);
		assertFalse(str.equals(""));
	}

	private void assertNotZero(int number){
		assertFalse(number == 0);
	}

	private Output runWithArguments(String command, String... extraArguments) throws Exception{
		String[] arguments = buildArguments(command, extraArguments);
		return Program.run(arguments);
	}

	private String[] buildArguments(String command, String... extraArguments){
		TestParams params = new TestParams("dokuwiki-2013-05-10", "Release 2013-05-10 \"Weatherwax\"", 8, 2);

		List<String> args = new ArrayList<String>();
		args.add("-u"); args.add(TestParams.user);
		args.add("-p"); args.add(TestParams.password);
		args.add("--url"); args.add(params.url);
		args.add(command);
		for(String extraArg : extraArguments){
			args.add(extraArg);
		}
		return args.toArray(new String[]{});
	}
}
