package dw.xmlrpc.itest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuPageLockedException;

public class TestHelper {

	static Date buildDate(int year, int month, int day, int hour, int minute, int second){
		Calendar cal = Calendar.getInstance();
		cal.set(year,  month, day, hour, minute, second);
		return cal.getTime();
	}

	/**
	 * Assert that the actual Date is equal to the expected one,
	 * although it may differ of a few milliseconds
	 */
	static void assertDatesNear(int year, int month, int day, int hour, int minute, int second, Date actual){
		Date date = buildDate(year,  month, day, hour, minute, second);
		assertTrue(Math.abs(date.getTime() - actual.getTime()) < 1000);
	}

	public static void assertPageIsLockForMe(String pageId, DokuJClient client) throws DokuException{
		boolean lockExceptionCaught = false;
		try {
			client.appendPage(pageId, "something");
		} catch ( DokuPageLockedException e){
			lockExceptionCaught = true;
		}

		assertTrue(lockExceptionCaught);
	}

	public static void assertPageIsUnlockForMe(String pageId, DokuJClient client) throws DokuException{
		String initialContent = client.getPage(pageId);
		try {
			client.appendPage(pageId, "something");
		} catch (DokuPageLockedException e){
			fail();
		}

		//Clean page content
		client.putPage(pageId, initialContent);
	}

	public static void assertPagesAreLockForMe(Iterable<String> pageIds, DokuJClient client) throws DokuException {
		for(String pageId : pageIds){
			assertPageIsLockForMe(pageId, client);
		}
	}

	public static void assertPagesAreUnlockForMe(Iterable<String> pageIds, DokuJClient client) throws DokuException {
		for(String pageId : pageIds){
			assertPageIsUnlockForMe(pageId, client);
		}
	}
}
