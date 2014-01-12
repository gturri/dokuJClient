package dw.xmlrpc.itest;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

@RunWith(value = Parameterized.class)
public class Test_Cookies {
	private TestParams _params;

	public Test_Cookies(TestParams params) throws MalformedURLException, DokuException{
		_params = params;
	}

	@Parameters
	 public static Collection<Object[]> data() {
		 return TestParams.data();
	 }

	@org.junit.Test
	public void cookies() throws Exception {
		DokuJClient client = new DokuJClient(_params.url);
		assertEquals(0, client.cookies().size());
		assertFalse(client.hasDokuwikiCookies());

		client.login(TestParams.writerLogin, TestParams.writerPwd);
		assertTrue(client.cookies().size() > 0);
		assertTrue(client.hasDokuwikiCookies());
	}
}
