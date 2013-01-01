package dw.xmlrpc.itest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.LockResult;

public class T_Lock {
	private DokuJClient _client;
	private DokuJClient _otherClient;
	@org.junit.Before
	public void setup() throws Exception {
		_client = new DokuJClient(TestParams.url, TestParams.user, TestParams.password);
		_otherClient = new DokuJClient(TestParams.url, TestParams.writerLogin, TestParams.writerPwd);
		clean();
	}
	
	@org.junit.After
	public void clean() throws Exception {
		List<String> pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p1");
		pagesToUnlock.add("ns2:p2");
		pagesToUnlock.add("ns2:p3");
		pagesToUnlock.add("ns2:p4");
		_client.setLocks(null, pagesToUnlock);
	}
	
	@org.junit.Test
	public void iCanPlayWihLockToAllowYouToWriteOrNot() throws Exception{
		String pageId  = "ns1:start";		
		_client.lock(pageId);
		
		//Make sure you can't write
		TestHelper.assertPageIsLockForMe(pageId, _otherClient);
				
		assertTrue(_client.unlock("ns1:start"));
		TestHelper.assertPageIsUnlockForMe(pageId, _otherClient);
	}
	
	@org.junit.Test
	public void iCanLockOrUnlockSeveralPagesAtOnce() throws Exception{
		//1st round: lock some pages and unlock some already unlock pages
		List<String> pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p2");
		List<String> pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p3");
		pagesToUnlock.add("ns2:p4");
		
		
		_client.setLocks(pagesToLock, pagesToUnlock);		
		TestHelper.assertPagesAreLockForMe(pagesToLock, _otherClient);
		TestHelper.assertPagesAreUnlockForMe(pagesToUnlock, _otherClient);

		//2nd round: lock some pages, some of which are already lock. Play with unlock too
		pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p3");
		pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p2");
		pagesToUnlock.add("ns2:p4");
		
		
		_client.setLocks(pagesToLock, pagesToUnlock);		
		TestHelper.assertPagesAreLockForMe(pagesToLock, _otherClient);
		TestHelper.assertPagesAreUnlockForMe(pagesToUnlock, _otherClient);
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
		
		assertTrue(_client.lock(pageId));
		
		//Now I write to let Dokuwiki unlock the page
		_client.appendPage(pageId, addedContent1);
		
		//And I make sure everyone may now write
		DokuJClient otherClient = new DokuJClient(TestParams.url, "writeruser", "writer");
		otherClient.appendPage(pageId, addedContent2);
		String currentContent = _client.getPage(pageId);
		assertEquals(initialContent + addedContent1 + addedContent2, currentContent);
	}
	
	@org.junit.Test
	public void lockResult() throws Exception {
		Set<String> emptySet = new HashSet<String>();
		
		//1st round: lock some pages and unlock some already unlocked pages
		List<String> pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p2");
		List<String> pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p3");
		pagesToUnlock.add("ns2:p4");
		
		LockResult expected = new LockResult(new HashSet<String>(pagesToLock), emptySet, emptySet, emptySet);
		LockResult actual = _client.setLocks(pagesToLock, pagesToUnlock);
		assertEquals(expected, actual);
		
		//2nd round: lock some pages, some of which are already locked. Play with unlock too
		pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p3");
		pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p2");
		pagesToUnlock.add("ns2:p4");
		
		Set<String> locked = new HashSet<String>();
		locked.add("ns2:p1");
		locked.add("ns2:p3");
		Set<String> unlocked = new HashSet<String>();
		unlocked.add("ns2:p2");

		expected = new LockResult(locked, emptySet, unlocked, emptySet);
		actual = _client.setLocks(pagesToLock, pagesToUnlock);
		assertEquals(expected, actual);
		
	}
}
