package dw.cli.itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import dw.cli.Output;
import dw.cli.Program;
import dw.xmlrpc.itest.TestParams;

public class TestHelper {
	protected final TestParams  params = new TestParams("dokuwiki-2020-07-29", "Release 2020-07-29 \"Hogfather\"", 10, 2);

	protected final String dateRegex = "[A-Za-z]{3} [A-Za-z]{3} [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} [A-Z]{3}? [0-9]{4}";

	protected void assertNotNullOrEmpty(String str){
		assertNotNull(str);
		assertFalse(str.equals(""));
	}

	protected void assertNotZero(int number){
		assertFalse(number == 0);
	}

	protected Output runWithArguments(String command, String... extraArguments) throws Exception{
		String[] arguments = buildArguments(TestParams.user, TestParams.password, command, extraArguments);
		return Program.run(arguments);
	}

	protected Output runWithUrl(String url, String command, String... extraArguments) throws Exception {
		String[] arguments = buildArguments(TestParams.user, TestParams.password, command, url, extraArguments);
		return Program.run(arguments);
	}

	protected Output runWithoutGenericArguments(String command, String... extraArguments) throws Exception {
		List<String> args = new ArrayList<String>();
		args.add(command);
		for(String extraArg : extraArguments){
			args.add(extraArg);
		}
		return Program.run(args.toArray(new String[]{}));
	}

	protected Output runWithArgumentAsWriterUser(String command, String... extraArguments) throws Exception {
		String[] arguments = buildArguments(TestParams.writerLogin, TestParams.writerPwd, command, extraArguments);
		return Program.run(arguments);
	}

	protected Output runWithArgumentAsPermissionLessUser(String command, String...extraArguments) throws Exception {
		String[] arguments = buildArguments("badUser", "badPassword", command, extraArguments);
		return Program.run(arguments);
	}

	private String[] buildArguments(String user, String password, String command, String... extraArguments){
		return buildArguments(user, password, command, params.url, extraArguments);
	}

	private String[] buildArguments(String user, String password, String command, String url, String... extraArguments){
		List<String> args = new ArrayList<String>();
		args.add("-u"); args.add(user);
		args.add("-p"); args.add(password);
		args.add("--url"); args.add(url);
		args.add(command);
		for(String extraArg : extraArguments){
			args.add(extraArg);
		}
		return args.toArray(new String[]{});
	}

	protected void assertSuccess(String expectedOutput, Output actualOutput){
		assertEquals(expectedOutput, actualOutput.out);
		assertGenericSuccess(actualOutput);
	}

	protected void assertSuccessRegex(String expectedOutput, Output actualOutput){
		assertTrue(actualOutput.out.matches(expectedOutput));
		assertGenericSuccess(actualOutput);
	}

	protected void assertGenericSuccess(Output actualOutput){
		assertEquals("", actualOutput.err);
		assertEquals(0, actualOutput.exitCode);
	}

	protected void assertGenericError(Output actualOutput){
		assertNotNullOrEmpty(actualOutput.err);
		assertEquals("", actualOutput.out);
		assertNotZero(actualOutput.exitCode);
	}

	protected void assertLastModificationSummary(String expectedSummary, Output actualOutput) {
		String lastModification = actualOutput.out.split("\n")[0];
		String actualSummary = lastModification.split(" - ")[1];
		assertEquals(expectedSummary, actualSummary);
	}
}
