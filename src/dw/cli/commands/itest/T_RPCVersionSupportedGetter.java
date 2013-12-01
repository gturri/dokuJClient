package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class T_RPCVersionSupportedGetter extends TestHelper {
	@org.junit.Test
	public void getRPCVersionSupported() throws Exception {
		assertSuccess(params.rpcVersionSupported.toString(), runWithArguments("getRPCVersionSupported"));
	}
}
