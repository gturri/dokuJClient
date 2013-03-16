package dw.xmlrpc.itest;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuAttachmentStillReferenced;
import dw.xmlrpc.exception.DokuAttachmentUploadException;
import dw.xmlrpc.exception.DokuBadUrlException;
import dw.xmlrpc.exception.DokuDeleteAttachmentException;
import dw.xmlrpc.exception.DokuDistantFileDoesntExistException;
import dw.xmlrpc.exception.DokuEmptyNewPageException;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuInvalidTimeStampException;
import dw.xmlrpc.exception.DokuPageLockedException;
import dw.xmlrpc.exception.DokuUnauthorizedException;
import dw.xmlrpc.exception.DokuWordblockException;

@RunWith(value = Parameterized.class)
public class T_Exception {
	private DokuJClient _client;
	private DokuJClient _unauthorizedClient;
	private TestParams _params;

	public T_Exception(TestParams params) throws MalformedURLException, DokuException {
		_params = params;
		_client = new DokuJClient(params.url, TestParams.user, TestParams.password);
		_unauthorizedClient = new DokuJClient(params.url, TestParams.unauthorizedLogin, TestParams.unauthorizedPwd);
	}

	@Parameters
	 public static Collection<Object[]> data() {
		 return TestParams.data();
	 }

	@org.junit.Test(expected=DokuUnauthorizedException.class)
	public void unauthorizedToUseXmlRpc() throws Exception {
		DokuJClient unauthorizedClient = new DokuJClient(_params.url, "wrongUser","wrongPwd");
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

	@org.junit.Test(expected=DokuDeleteAttachmentException.class)
	public void mediaToDeleteDoesntExist() throws Exception {
		String attachmentId = "unexistingFile.gif";
		_client.deleteAttachment(attachmentId);
	}

	@org.junit.Test(expected=DokuDistantFileDoesntExistException.class)
	public void mediaToGetDoesntExist() throws Exception {
		_client.getAttachment("unexistingFile.gif", "file.gif");
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

	@org.junit.Test(expected=DokuAttachmentStillReferenced.class)
	public void stillReferencedMedia() throws Exception {
		String distantFile = "referencedMedia.gif";
		_client.putAttachment(distantFile, TestParams.localFileToUpload, true);
		_client.putPage("referencing", "here is a {{" + distantFile + "}}|file");
		_client.deleteAttachment(distantFile);
	}

	@org.junit.Test(expected=DokuInvalidTimeStampException.class)
	public void invalidTimestamp() throws Exception {
		_client.getRecentChanges(123456);
	}

	@org.junit.Test(expected=DokuPageLockedException.class)
	public void pageLockedException() throws Exception {
		String pageId = "ns1:start";
		_client.lock(pageId);

		DokuJClient otherClient = new DokuJClient(_params.url, TestParams.writerLogin, TestParams.writerPwd);
		otherClient.appendPage(pageId, "something");
	}

	@org.junit.Test(expected=DokuBadUrlException.class)
	public void badUrlExceptionWhenPathIsWrong() throws Exception {
		DokuJClient client = new DokuJClient(_params.url + "azerty", TestParams.user, TestParams.password);
		client.getTitle();
	}

	@org.junit.Test(expected=DokuWordblockException.class)
	public void wordblockException() throws Exception {
		String pageId = "ns1:start";
		_client.appendPage(pageId, "try to write a forbiddenword");
	}

	@org.junit.Test(expected=DokuAttachmentUploadException.class)
	public void uploadForbiddenBecauseOfForbiddenExtension() throws Exception {
		File file = new File(TestParams.localFileToUpload);
		_client.putAttachment("file.sh", file, true);
	}

	@org.junit.Test(expected=DokuAttachmentUploadException.class)
	public void uploadForbiddenBecauseOfBadExtension() throws Exception {
		File file = new File(TestParams.localFileToUpload);

		//jpg is authorized, but the file is in fact a gif
		_client.putAttachment("file.jpg", file, true);
	}

	@org.junit.Test(expected=DokuAttachmentUploadException.class)
	public void uploadBecauseFileAlreadyExists() throws Exception {
		File file = new File(TestParams.localFileToUpload);
		String attachmentId = "file.gif";
		try {
			_client.putAttachment(attachmentId, file, true);
		} catch (Exception e){
			fail();
		}
		_client.putAttachment(attachmentId, file, false);
	}

	@org.junit.Test(expected=DokuEmptyNewPageException.class)
	public void emptyNewPageException() throws Exception {
		String pageId = "toDelete";

		//We try to delete it twice because to be sure we try it at least once
		//when the page is actually already deleted
		_client.putPage(pageId, "");
		_client.putPage(pageId, "");
	}
}
