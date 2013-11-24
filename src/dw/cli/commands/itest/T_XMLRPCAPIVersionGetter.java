package dw.cli.commands.itest;

import dw.cli.Output;
import dw.cli.itest.TestHelper;


public class T_XMLRPCAPIVersionGetter extends TestHelper {
	@org.junit.Test
	public void getXMLRPCAPIVersion() throws Exception{
		Output output = runWithArguments("getXMLRPCAPIVersion");
		assertSuccess(params.apiVersion.toString(), output);
	}
}
