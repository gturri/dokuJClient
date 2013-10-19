package dw.cli.utest;

import static org.junit.Assert.*;

import java.net.URL;

import dw.cli.CliOptions;
import dw.cli.OptionParser;

public class T_OptionParser {

	@org.junit.Test
	public void canParseOptions() throws Exception{
		String[] args = new String[]{"-u", "toto", "-p", "123456", "--url", "http://myUrl", "getTitle", "arg0"};
		OptionParser parser = new OptionParser(args);

		assertTrue(parser.success());
		assertNotNull(parser.getHelpMessage());

		CliOptions options = parser.getCliOptions();
		assertEquals("toto", options.user);
		assertEquals("123456", options.password);
		assertEquals(new URL("http://myUrl"), options.url);
		assertEquals("getTitle", options.command);
		assertEquals(1, options.commandArguments.size());
		assertEquals("arg0", options.commandArguments.get(0));
	}

	@org.junit.Test
	public void failsIfAnArgumentIsMissingItsValue() throws Exception{
		String[] args = new String[]{"-u", "toto", "-p", "123456", "--url"};
		OptionParser parser = new OptionParser(args);
		assertFalse(parser.success());
	}
}
