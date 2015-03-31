package dw.xmlrpc.itest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.LockResult;

@RunWith(value = Parameterized.class)
public class Test_Lock extends TestHelper {
	private DokuJClient _client;
	private DokuJClient _otherClient;

	public Test_Lock(TestParams params) throws Exception{
		_client = new DokuJClient(params.url, TestParams.user, TestParams.password);
		_otherClient = new DokuJClient(params.url, TestParams.writerLogin, TestParams.writerPwd);
		clean();
	}

	@Parameters
	 public static Collection<Object[]> data() {
		 return TestParams.data();
	 }

	@org.junit.After
	public void clean() throws Exception {
		_client.unlock("ns2:p1");
		_client.unlock("ns2:p2");
		_client.unlock("ns2:p3");
		_client.unlock("ns2:p4");

		_otherClient.unlock("ns2:p1");
		_otherClient.unlock("ns2:p2");
	}

	@org.junit.Test
	public void iCanPlayWihLockToAllowYouToWriteOrNot() throws Exception{
		String pageId  = "ns1:start";
		_client.lock(pageId);

		assertPageIsLockForMe(pageId, _otherClient);
		assertTrue(_client.unlock("ns1:start"));
		assertPageIsUnlockForMe(pageId, _otherClient);
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
		assertPagesAreLockForMe(pagesToLock, _otherClient);
		assertPagesAreUnlockForMe(pagesToUnlock, _otherClient);

		//2nd round: lock some pages, some of which are already locked. Play with unlock too
		pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		pagesToLock.add("ns2:p3");
		pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p2");
		pagesToUnlock.add("ns2:p4");


		_client.setLocks(pagesToLock, pagesToUnlock);
		assertPagesAreLockForMe(pagesToLock, _otherClient);
		assertPagesAreUnlockForMe(pagesToUnlock, _otherClient);
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
		_otherClient.appendPage(pageId, addedContent2);
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

		LockResult expected = new LockResult(new HashSet<String>(pagesToLock), emptySet, emptySet, new HashSet<String>(pagesToUnlock));
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
		Set<String> failedUnlock = new HashSet<String>();
		failedUnlock.add("ns2:p4");

		expected = new LockResult(locked, emptySet, unlocked, failedUnlock);
		actual = _client.setLocks(pagesToLock, pagesToUnlock);
		assertEquals(expected, actual);
	}

	@org.junit.Test
	public void getFailedLockedAndUnlockedPages() throws Exception {
		Set<String> emptySet = new HashSet<String>();

		_otherClient.lock("ns2:p1");
		_otherClient.lock("ns2:p2");

		List<String> pagesToLock = new ArrayList<String>();
		pagesToLock.add("ns2:p1");
		List<String> pagesToUnlock = new ArrayList<String>();
		pagesToUnlock.add("ns2:p2");


		LockResult expected = new LockResult(emptySet, new HashSet<String>(pagesToLock), emptySet, new HashSet<String>(pagesToUnlock));
		LockResult actual = _client.setLocks(pagesToLock, pagesToUnlock);

		assertEquals(expected, actual);
	}
}
