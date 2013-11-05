package dw.cli.itest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import dw.cli.Output;
import dw.cli.Program;
import dw.xmlrpc.itest.TestParams;

public class TestHelper extends dw.xmlrpc.itest.TestHelper {
	protected void assertNotNullOrEmpty(String str){
		assertNotNull(str);
		assertFalse(str.equals(""));
	}

	protected void assertNotZero(int number){
		assertFalse(number == 0);
	}
	protected Output runWithArguments(String command, String... extraArguments) throws Exception{
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
