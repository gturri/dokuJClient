package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_AclChecker extends TestHelper {
	@org.junit.Test
	public void aclCheck() throws Exception {
		assertSuccess("255", runWithArguments("aclCheck", "ns1:start"));
		assertSuccess("8", runWithArgumentAsWriterUser("aclCheck", "ns1:start"));
	}
}
