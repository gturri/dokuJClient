package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_AclChanger extends TestHelper {
	@org.junit.Before
	public void clean() throws Exception {
		//It stings a bit since the setup rely on the tested code. However:
		// * without it, we can't launch two mutation runs in a row
		// * if there's a bug in the tested code, it will be caught anyway
		runWithArguments("addAcl", "--scope", "*", "--username", "@user", "--permission", "8");
	}

	@org.junit.Test
	public void canAddAndRemoveAcl() throws Exception {
		assertSuccess("8", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));

		assertSuccess("", runWithArguments("delAcl", "--scope", "*", "--username", "@user"));
		assertSuccess("0", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));

		assertSuccess("", runWithArguments("addAcl", "--scope", "*", "--username", "@user", "--permission", "8"));
		assertSuccess("8", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));
	}
}
