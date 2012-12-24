package itest;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dw.AttachmentInfo;
import dw.DokuJClient;
import dw.MediaChange;

public class T_Attacher {
	private DokuJClient _client;
	private String _localDownloadedFile = "tempFileForTests.gif";
	private String _localFileToUpload = "src/itest/testEnvironment/list-plus.gif";

	Set<String> _uploadedFiles;
	
	@org.junit.Before
	public void setup() throws MalformedURLException {
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
	public void  getAttachments() throws Exception{
		//Set up environment
		_uploadedFiles.add("nswithanotherns:img1.gif");
		_uploadedFiles.add("ns2:img2.gif");
		_uploadedFiles.add("nswithanotherns:img3.gif");
		_uploadedFiles.add("nswithanotherns:img33.gif");
		_uploadedFiles.add("nswithanotherns:otherns:img4.gif");
		
		for ( String fileId : _uploadedFiles ){
			_client.putAttachment(fileId, _localFileToUpload, true);
		}
		
		//actually test
		//Filtering on a PREG
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pattern", "/3.gif/");
		List<AttachmentInfo> res = _client.getAttachments("nswithanotherns", params);
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
	public void getRecentMediaChanges() throws Exception{
		List<MediaChange> changes = _client.getRecentMediaChanges(1356383460);
		assertTrue(changes.size() > 0);
		
		for(MediaChange c : changes){
			System.out.println(c.toString());
		}
	}
	
	@org.junit.Test
	public void putGetAndDeleteAttachment() throws Exception{
		String fileId = "ns1:img2.gif";
		File file = new File(_localFileToUpload);

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
