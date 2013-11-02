package dw.cli.itest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import dw.cli.Program;
import dw.xmlrpc.itest.TestParams;

public class T_Command {

	@org.junit.Test
	public void getTitle() throws Exception{
		String[] arguments = buildArguments("getTitle");
		assertEquals("test xmlrpc", Program.run(arguments));
	}

	@org.junit.Test
	public void getAttachments() throws Exception {
		String[] arguments = buildArguments("getAttachments", "ro_for_tests");
		assertEquals("ro_for_tests:img1.gif", Program.run(arguments));
	}

	@org.junit.Test
	public void getAttachmentsLongListingFormat() throws Exception {
		String[] arguments = buildArguments("getAttachments", "l", "ro_for_tests");
		assertEquals("255 67 Mon Dec 24 20:11:00 CET 2012 ro_for_tests:img1.gif", Program.run(arguments));
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