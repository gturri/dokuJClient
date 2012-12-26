package dw.xmlrpc.itest;

import java.net.MalformedURLException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuPageLockedException;
import dw.xmlrpc.exception.DokuUnauthorizedException;

public class T_Exception {
	private DokuJClient _client;
	

	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
	}
	
	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorized() throws Exception {
		DokuJClient unauthorizedClient = new DokuJClient(TestParams.url, "wrongUser","wrongPwd");
		unauthorizedClient.getTime();
	}
	
	@org.junit.Test(expected=DokuPageLockedException.class)
	public void pageLockedException() throws Exception {
		String pageId = "ns1:start";
		_client.lock(pageId);
		
		DokuJClient otherClient = new DokuJClient(TestParams.url, TestParams.writerLogin, TestParams.writerPwd);
		otherClient.appendPage(pageId, "something");
	}
}
