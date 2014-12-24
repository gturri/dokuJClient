package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_AclChanger extends TestHelper {
	@org.junit.Test
	public void canAddAndRemoveAcl() throws Exception {
		assertSuccess("8", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));

		assertSuccess("", runWithArguments("delAcl", "--scope", "*", "--username", "@user"));
		assertSuccess("0", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));

		assertSuccess("", runWithArguments("addAcl", "--scope", "*", "--username", "@user", "--permission", "8"));
		assertSuccess("8", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));
	}
}
