package dw.xmlrpc.itest;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import dw.xmlrpc.AttachmentDetails;
import dw.xmlrpc.AttachmentInfo;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.MediaChange;

public class T_Attacher {
	private DokuJClient _client;
	private String _localDownloadedFile = "tempFileForTests.gif";


	Set<String> _uploadedFiles;

	@org.junit.Before
	public void setup() throws MalformedURLException {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
		_uploadedFiles = new HashSet<String>();
		clean();
	}

	@org.junit.After
	public void clean(){
		File f = new File(_localDownloadedFile);
		f.delete();

		for ( String fileId : _uploadedFiles ){
			try {
				_client.deleteAttachment(fileId);
			} catch ( Exception e ){
				//Too bad we missed one... Hope we'll have better luck for the next...
				System.out.println("Failed to delete distant attachment " + fileId + " during tear down");
			}
		}
	}

	@org.junit.Test
	public void getAttachmentsUseParameters() throws Exception{
		//Set up environment
		_uploadedFiles.add("nswithanotherns:img1.gif");
		_uploadedFiles.add("ns2:img2.gif");
		_uploadedFiles.add("nswithanotherns:img3.gif");
		_uploadedFiles.add("nswithanotherns:img33.gif");
		_uploadedFiles.add("nswithanotherns:otherns:img4.gif");

		for ( String fileId : _uploadedFiles ){
			_client.putAttachment(fileId, TestParams.localFileToUpload, true);
		}

		//actually test
		//Filtering on a PREG
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pattern", "/3.gif/");
		List<AttachmentDetails> res = _client.getAttachments("nswithanotherns", params);
		assertEquals(2, res.size());

		//without special parameters
		params = new HashMap<String, Object>();
		res = _client.getAttachments("nswithanotherns", params);
		assertEquals(4, res.size());

		//Limiting max depth
		params = new HashMap<String, Object>();
		params.put("depth", 1);
		res = _client.getAttachments("nswithanotherns", params);
		assertEquals(3, res.size());
	}

	@org.junit.Test
	public void getAttachmentsReturnsCorrectStructure() throws Exception {
		List<AttachmentDetails> res = _client.getAttachments("ro_for_tests");
		assertEquals(1, res.size());

		AttachmentDetails details = res.get(0);
		System.out.println("Details: " + details.toString());
		//Details: isImg: true, writable: true, perms:null
		assertEquals("ro_for_tests:img1.gif", details.id());
		assertEquals((Integer) 67, details.size());
		//TODO: study timezones more in depth to strenghten this assertion
		assertNotNull(details.lastModified());
		assertEquals(true, details.isImg());
		assertEquals(true, details.writable());
		assertEquals((Integer) 255, details.perms());
	}

	@org.junit.Test
	public void getRecentMediaChanges() throws Exception{
		List<MediaChange> changes = _client.getRecentMediaChanges(1356383400);

		String mediaId = "ro_for_tests:img1.gif";
		MediaChange change = findOneMediaChange(changes, mediaId);
		assertEquals(mediaId, change.id());
		TestHelper.assertDatesNear(2012, 11, 24, 21, 11, 0, change.lastModified());
		assertEquals("fifi", change.author());
		assertEquals((Integer) 1356383460, change.version());
		assertEquals((Integer) 255, change.perms());
		assertEquals((Integer) 67, change.size());
	}

	@org.junit.Test
	public void getRecentMediaChangesRespectMaxTimestamp() throws Exception {
		String oldMediaChange = "ro_for_tests:img1.gif";
		String recentMediaChange = "ro_for_tests:img2.gif";

		List<MediaChange> changes = _client.getRecentMediaChanges(1356383400);
		assertNotNull(findOneMediaChange(changes,oldMediaChange));
		assertNotNull(findOneMediaChange(changes, recentMediaChange));

		changes = _client.getRecentMediaChanges(1356383461);
		assertNull(findOneMediaChange(changes,oldMediaChange));
		assertNotNull(findOneMediaChange(changes, recentMediaChange));
	}

	@org.junit.Test
	public void getRecentMediaChangesRespectMaxDate() throws Exception {
		String oldMediaChange = "ro_for_tests:img1.gif";
		String recentMediaChange = "ro_for_tests:img2.gif";

		List<MediaChange> changes = _client.getRecentMediaChanges(TestHelper.buildDate(2012, 11, 24, 21, 10, 59));
		assertNotNull(findOneMediaChange(changes,oldMediaChange));
		assertNotNull(findOneMediaChange(changes, recentMediaChange));

		changes = _client.getRecentMediaChanges(TestHelper.buildDate(2012, 11, 24, 21, 11, 1));
		assertNull(findOneMediaChange(changes,oldMediaChange));
		assertNotNull(findOneMediaChange(changes, recentMediaChange));
	}

	private MediaChange findOneMediaChange(List<MediaChange> changes, String mediaId){
		MediaChange res = null;
		boolean foundOne = false;
		for(MediaChange change : changes){
			if ( change.id().equals(mediaId) ){
				if ( foundOne ){
					fail("Found two media changes for the same mediaId " + mediaId);
				}
				foundOne = true;
				res = change;
			}
		}
		return res;
	}

	@org.junit.Test
	public void putGetAndDeleteAttachment() throws Exception{
		String fileId = "ns1:img2.gif";
		File file = new File(TestParams.localFileToUpload);

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

	@org.junit.Test
	public void getAttachmentInfo() throws Exception {
		String id = "ro_for_tests:img1.gif";
		AttachmentInfo info = _client.getAttachmentInfo(id);

		assertEquals(id, info.id());
		assertEquals((Integer) 67, info.size());
		TestHelper.assertDatesNear(2013, 0, 15, 21, 59, 21, info.lastModified());
	}
}
