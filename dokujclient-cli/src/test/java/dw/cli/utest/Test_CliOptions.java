package dw.cli.utest;

import static org.junit.Assert.*;
import dw.cli.CliOptions;

public class Test_CliOptions {
	@org.junit.Test
	public void toStringDoesntThrowNullPointerException(){
		CliOptions cliWithNullMembers = new CliOptions();
		assertNotNull(cliWithNullMembers.toString());
	}
}
