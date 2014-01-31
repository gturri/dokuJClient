package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_RPCVersionSupportedGetter extends TestHelper {
	@org.junit.Test
	public void getRPCVersionSupported() throws Exception {
		assertSuccess(params.rpcVersionSupported.toString(), runWithArguments("getRPCVersionSupported"));
	}
}
