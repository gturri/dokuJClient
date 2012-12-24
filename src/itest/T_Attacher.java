package itest;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;

import dw.AttachmentInfo;
import dw.DokuJClient;

public class T_Attacher {
	private DokuJClient _client;
	private String _localDownloadedFile = "tempFileForTests.gif";
	
	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
		clean();
	}
	
	@org.junit.After
	public void clean(){
		File f = new File(_localDownloadedFile);
		f.delete();
	}
	
	@org.junit.Test
	public void putAttachment() throws Exception{
		String fileId = "ns1:img2.gif";
		File file = new File("src/itest/testEnvironment/list-plus.gif");

		_client.putAttachment(fileId, file, true);
		AttachmentInfo info = _client.getAttachmentInfo(fileId);
		assertEquals((Integer)(int) file.length(), info.size());
		
		File fileRetrieved = _client.getAttachment(fileId, _localDownloadedFile);
		assertTrue(fileRetrieved.exists());
		//Ideally I should check the content of both files are identical.
		//It seems it would be easy with Java7's Files.readAllBytes(myFile),
		//but since I'm stuck with java6, this will do the trick
		assertEquals(file.length(), fileRetrieved.length());

		_client.deleteAttachment(fileId);
		info = _client.getAttachmentInfo(fileId);
		assertEquals((Integer)0, info.size());
	}
}
