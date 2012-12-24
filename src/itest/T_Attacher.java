package itest;

import java.io.File;
import java.net.MalformedURLException;

import dw.DokuJClient;

public class T_Attacher {
	private DokuJClient _client;
	
	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
	}
	
	@org.junit.Test
	public void putAttachment() throws Exception{
		File f = new File("src/itest/testEnvironment/list-plus.gif");
		_client.putAttachment("ns1:img2.gif", f, true);
	}
}
