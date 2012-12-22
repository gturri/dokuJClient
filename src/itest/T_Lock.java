package itest;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import dw.DokuJClient;

public class T_Lock {
	private DokuJClient _client;

	@org.junit.Before
	public void setup() throws MalformedURLException {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
	}
	
	@org.junit.Test
	public void iCanPlayWihLockToAllowYouToWriteOrNot() throws Exception{
		String pageId  = "ns1:start";		
		_client.lock(pageId);
		
		//Make sure you can't write
		DokuJClient otherClient = new DokuJClient(TestParams.url, TestParams.writerLogin, TestParams.writerPwd);
		TestHelper.assertPageIsLockForMe(pageId, otherClient);
				
		_client.unlock("ns1:start");
		TestHelper.assertPageIsUnlockForMe(pageId, otherClient);
	}
	
	@org.junit.Test
	public void iCanLockOrUnlockSeveralPagesAtOnce() throws Exception{
		DokuJClient otherClient = new DokuJClient(TestParams.url, TestParams.writerLogin, TestParams.writerPwd);

		//1st round: lock some pages and unlock some already unlock pages
		List<String> pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p2");
		List<String> pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p3");
		pagesToUnlock.add("ns2:p4");
		
		
		_client.setLock(pagesToLock, pagesToUnlock);		
		TestHelper.assertPagesAreLockForMe(pagesToLock, otherClient);
		TestHelper.assertPagesAreUnlockForMe(pagesToUnlock, otherClient);

		//2nd round: lock some pages, some of which are already lock. Play with unlock too
		pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p3");
		pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p2");
		pagesToUnlock.add("ns2:p4");
		
		
		_client.setLock(pagesToLock, pagesToUnlock);		
		TestHelper.assertPagesAreLockForMe(pagesToLock, otherClient);
		TestHelper.assertPagesAreUnlockForMe(pagesToUnlock, otherClient);
	}
	
	//This doesn't really test the client, but it documents Dokuwiki's behavior,
	//hence it documents a non-intuitive behavior of the client
	@org.junit.Test
	public void iUnlockAutomaticallyWhenIWrite() throws Exception {
		String pageId  = "ns1:start";
		String initialContent = "init";
		String addedContent1 = "added1";
		String addedContent2 = "added2";
		
		//Get a known state
		_client.putPage(pageId, initialContent);
		
		_client.lock(pageId);
		
		//Now I write to let Dokuwiki unlock the page
		_client.appendPage(pageId, addedContent1);
		
		//And I make sure everyone may now write
		DokuJClient otherClient = new DokuJClient(TestParams.url, "writeruser", "writer");
		otherClient.appendPage(pageId, addedContent2);
		String currentContent = _client.getPage(pageId);
		assertEquals(initialContent + addedContent1 + addedContent2, currentContent);
	}
}
