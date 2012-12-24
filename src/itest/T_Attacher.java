package itest;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;

import dw.AttachmentInfo;
import dw.DokuJClient;

public class T_Attacher {
	private DokuJClient _client;
	
	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
	}
	
	@org.junit.Test
	public void putAttachment() throws Exception{
		String fileId = "ns1:img2.gif";
		File file = new File("src/itest/testEnvironment/list-plus.gif");

		_client.putAttachment(fileId, file, true);
		AttachmentInfo info = _client.getAttachmentInfo(fileId);
		assertEquals((Integer)(int) file.length(), info.size());
		
		_client.deleteAttachment(fileId);
		info = _client.getAttachmentInfo(fileId);
		assertEquals((Integer)0, info.size());
	}
}
