package dw.xmlrpc.itest;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuBadUrlException;
import dw.xmlrpc.exception.DokuPageLockedException;
import dw.xmlrpc.exception.DokuUnauthorizedException;
import dw.xmlrpc.exception.DokuWordblockException;

public class T_Exception {
	private DokuJClient _client;
	private DokuJClient _unauthorizedClient;
	

	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
		_unauthorizedClient = new DokuJClient(TestParams.url, TestParams.unauthorizedLogin, TestParams.unauthorizedPwd);
	}
	
	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorizedToUseXmlRpc() throws Exception {
		DokuJClient unauthorizedClient = new DokuJClient(TestParams.url, "wrongUser","wrongPwd");
		unauthorizedClient.getTime();
	}
	
	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorizedToRead() throws Exception {
		_unauthorizedClient.getPage("ns1:start");
	}
	
	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorizedToWrite() throws Exception {
		_unauthorizedClient.putPage("ns1:start", "text");
	}
	
	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorizedToListMedia() throws Exception {
		_unauthorizedClient.getAttachments("");
	}

	@org.junit.Test
	public void unauthorizedToDeleteMedia() throws Exception {
		String attachmentId = "forTestUnauthorizedWithMedia.gif";
		_client.putAttachment(attachmentId, TestParams.localFileToUpload, true);

		boolean getRelevantException = false;
		try {
			_unauthorizedClient.deleteAttachment(attachmentId);
		} catch (DokuUnauthorizedException e){
			getRelevantException = true;
		}
		
		_client.deleteAttachment(attachmentId);
		
		assertTrue(getRelevantException);
	}
	
	@org.junit.Test
	public void unauthorizedToGetMedia() throws Exception {
		String attachmentId = "forTestUnauthorizedWithMedia.gif";
		_client.putAttachment(attachmentId, TestParams.localFileToUpload, true);

		boolean getRelevantException = false;
		try {
			_unauthorizedClient.getAttachment(attachmentId, "file.gif");
		} catch (DokuUnauthorizedException e){
			getRelevantException = true;
		}
		
		_client.deleteAttachment(attachmentId);
		
		assertTrue(getRelevantException);
	}	
	
	@org.junit.Test(expected=DokuPageLockedException.class)
	public void pageLockedException() throws Exception {
		String pageId = "ns1:start";
		_client.lock(pageId);
		
		DokuJClient otherClient = new DokuJClient(TestParams.url, TestParams.writerLogin, TestParams.writerPwd);
		otherClient.appendPage(pageId, "something");
	}

	@org.junit.Test(expected=DokuBadUrlException.class)
	public void badUrlExceptionWhenPathIsWrong() throws Exception {
		DokuJClient client = new DokuJClient(TestParams.url + "azerty", TestParams.user, TestParams.password);
		client.getTitle();
	}

	@org.junit.Test(expected=DokuWordblockException.class)
	public void wordblockException() throws Exception {
		String pageId = "ns1:start";
		_client.appendPage(pageId, "try to write a forbiddenword");
	}
}
