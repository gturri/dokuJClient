package dw.cli.commands.itest;

import dw.cli.itest.TestHelper;

public class Test_LocksSetter extends TestHelper {

	@org.junit.Before
	@org.junit.After
	public void unlockAll() throws Exception {
		runWithArguments("unlock", "ns2:p1", "ns2:p2", "ns2:p3");
		runWithArgumentAsWriterUser("unlock", "ns2:p1", "ns2:p2", "ns2:p3");
	}

	@org.junit.Test
	public void canLockAPage() throws Exception {
		//Check initial state
		assertIsntLocked("ns2:p1");
		assertIsntLocked("ns2:p3");

		//Act
		assertSuccess("", runWithArguments("lock", "ns2:p1"));

		//Check final state
		assertIsLocked("ns2:p1");
		assertIsntLocked("ns2:p3");
	}

	private void assertIsntLocked(String pageId) throws Exception  {
		assertGenericSuccess(runWithArgumentAsWriterUser("putPage", pageId, "some text"));
	}

	private void assertIsLocked(String pageId) throws Exception  {
		assertGenericError(runWithArgumentAsWriterUser("putPage", pageId, "some text"));
	}

	@org.junit.Test
	public void canLockSeveralPages() throws Exception {
		//Check initial state
		assertIsntLocked("ns2:p1");
		assertIsntLocked("ns2:p2");
		assertIsntLocked("ns2:p3");

		//Act
		assertSuccess("", runWithArguments("lock", "ns2:p1", "ns2:p2"));

		//Check final state
		assertIsLocked("ns2:p1");
		assertIsLocked("ns2:p2");
		assertIsntLocked("ns2:p3");
	}

	@org.junit.Test
	public void canUnlockAPage() throws Exception {
		//Prepare and check initial state
		runWithArguments("lock", "ns2:p1");
		assertIsLocked("ns2:p1");

		//Act
		assertSuccess("", runWithArguments("unlock", "ns2:p1"));

		//Check final state
		assertIsntLocked("ns2:p1");
	}

	@org.junit.Test
	public void canUnlockSeveralPage() throws Exception {
		//Prepare and check initial state
		runWithArguments("lock", "ns2:p1", "ns2:p2");
		assertIsLocked("ns2:p1");
		assertIsLocked("ns2:p2");

		//Act
		assertSuccess("", runWithArguments("unlock", "ns2:p1", "ns2:p2"));

		//Check final state
		assertIsntLocked("ns2:p1");
		assertIsntLocked("ns2:p2");
	}

	@org.junit.Test
	public void reportsAnErrorIfAtLeastOneLockFailed() throws Exception {
		//Prepare initial state
		runWithArgumentAsWriterUser("lock", "ns2:p1");

		//Act
		assertGenericError(runWithArguments("lock", "ns2:p1", "ns2:p2"));
	}

	@org.junit.Test
	public void reportsAnErrorIfAtLeastOneUnlockFailed() throws Exception {
		//Prepare initial state
		runWithArgumentAsWriterUser("lock", "ns2:p1");

		//Act
		assertGenericError(runWithArguments("unlock", "ns2:p1", "ns2:p2"));
	}
}
