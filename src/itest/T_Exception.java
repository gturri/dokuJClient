package itest;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import dw.DokuJClient;
import dw.exception.DokuPageLockedException;
import dw.exception.DokuUnauthorizedException;

public class T_Exception {
	private static String _url = "http://localhost/dokuwikiITestsForXmlRpcClient/lib/exe/xmlrpc.php";
	private static String _user = "xmlrpcuser";
	private static String _password = "xmlrpc";
	
	private DokuJClient _client;
	

	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(_url, _user, _password);
	}
	
	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorized() throws Exception {
		DokuJClient unauthorizedClient = new DokuJClient(TestParams.url, "wrongUser","wrongPwd");
		unauthorizedClient.getTime();
	}
	
	@org.junit.Test
	public void pageLockedException() throws Exception {
		String pageId = "ns1:start";
		_client.lock(pageId);
		
		DokuJClient otherClient = new DokuJClient(_url, "writeruser", "writer");
		boolean relevantExceptionCaught = false;
		try {
			otherClient.appendPage(pageId, "something");
		} catch ( DokuPageLockedException e){
			relevantExceptionCaught = true;
		}
		finally {
			_client.unlock(pageId);	
		}
		assertTrue(relevantExceptionCaught);
		
	}
}
