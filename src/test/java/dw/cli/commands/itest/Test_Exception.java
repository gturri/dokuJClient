package dw.cli.commands.itest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import dw.cli.itest.TestHelper;

public class Test_Exception extends TestHelper {
	@org.junit.Test
	public void knownBadUrlYieldErrorWithUsefulHint() throws Exception {
		try {
			runWithUrl("http://localhost/badUrl", "getVersion");
		} catch(Exception e){
			assertTrue(e.getMessage().contains("lib/exe/xmlrpc.php"));
			return;
		}
		fail("Should have thrown");
	}
}
