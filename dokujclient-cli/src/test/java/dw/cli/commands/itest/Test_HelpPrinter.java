package dw.cli.commands.itest;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import dw.cli.Output;
import dw.cli.Program;
import dw.cli.itest.TestHelper;

public class Test_HelpPrinter extends TestHelper {

	@org.junit.Test
	public void commandHelpPrintsGenericHelp() throws Exception {
		Output output = runWithoutGenericArguments("help");
		assertGenericSuccess(output);
		assertIsGenericHelpMessage(output.out);
	}

	@org.junit.Test
	public void commandHelpCanPrintHelpForAGivenCommand() throws Exception {
		Output output = runWithoutGenericArguments("help", "getPagelist");
		assertGenericSuccess(output);
		assertTrue(output.out.contains("[-l] <namespace> [--depth <depth>]"));
	}

	@org.junit.Test
	public void commandHelpPrintsGenericHelpIfQueriedWithInvalidCommand() throws Exception {
		Output output = runWithoutGenericArguments("help", "doesntExist");
		assertGenericSuccess(output);
		assertIsGenericHelpMessage(output.out);
	}

	@org.junit.Test
	public void optionHelpPrintsHelp() throws Exception {
		Output output = runWithoutGenericArguments("--help");
		assertGenericSuccess(output);
		assertIsGenericHelpMessage(output.out);
	}

	@org.junit.Test
	public void unexistingCommandPrintsHelp() throws Exception {
		Output output = runWithArguments("doesntExist");
		assertGenericError(output);
		assertIsGenericHelpMessage(output.err);
	}

	@org.junit.Test
	public void incorrectOptionsPrintsHelp() throws Exception {
		List<String> args = new ArrayList<String>();
		args.add("--urll"); args.add("http://localhost/myWiki/lib/exe/xmlrpc.php");
		Output output = Program.run(args.toArray(new String[]{}));

		assertGenericError(output);
		assertIsGenericHelpMessage(output.err);
	}


	private void assertIsGenericHelpMessage(String message) {
		assertMessageContainsGenericOptions(message);
		assertMessageContainsListOfCommands(message);
		assertMessageTellHowToGetHelpForAGivenCommand(message);
	}

	private void assertMessageContainsGenericOptions(String message){
		assertTrue(message.contains("--user"));
		assertTrue(message.contains("--url"));
		assertTrue(message.contains("--help"));
		assertTrue(message.contains("--password"));
	}

	private void assertMessageContainsListOfCommands(String message){
		assertTrue(message.contains("getTitle"));
		assertTrue(message.contains("getAttachments"));
		assertTrue(message.contains("deleteAttachment"));
		assertTrue(message.contains("getPagelist"));
	}

	private void assertMessageTellHowToGetHelpForAGivenCommand(String message) {
		assertTrue(message.contains("help <command>"));
	}
}
