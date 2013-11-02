package dw.cli.utest;

import static org.junit.Assert.*;

import java.net.URL;

import dw.cli.CliOptions;
import dw.cli.OptionParser;

public class T_OptionParser {

	@org.junit.Test
	public void canParseGenericOptions() throws Exception{
		String[] args = new String[]{"-u", "toto", "-p", "123456", "--url", "http://myUrl", "getTitle", "arg0"};
		OptionParser parser = new OptionParser(args);

		System.out.println(parser.getHelpMessage());
		assertTrue(parser.success());
		assertNotNull(parser.getHelpMessage());

		CliOptions options = parser.getCliOptions();
		assertEquals("toto", options.user);
		assertEquals("123456", options.password);
		assertEquals(new URL("http://myUrl"), options.url);
		assertEquals("getTitle", options.command);
		assertEquals(1, options.commandArguments.length);
		assertEquals("arg0", options.commandArguments[0]);
	}

	@org.junit.Test
	public void canParseCommandFlags() throws Exception {
		String[] args = new String[]{"--url", "http://myUrl", "someCommand", "arg0", "-l", "3"};

		OptionParser parser = new OptionParser(args);

		assertTrue(parser.success());
		CliOptions options = parser.getCliOptions();
		assertEquals("someCommand", options.command);
		assertEquals(3, options.commandArguments.length);
		assertEquals("arg0", options.commandArguments[0]);
		assertEquals("-l", options.commandArguments[1]);
		assertEquals("3", options.commandArguments[2]);
	}

	@org.junit.Test
	public void failsIfAnArgumentIsMissingItsValue() throws Exception{
		String[] args = new String[]{"-u", "toto", "-p", "123456", "--url"};
		OptionParser parser = new OptionParser(args);
		assertFalse(parser.success());
	}
}
