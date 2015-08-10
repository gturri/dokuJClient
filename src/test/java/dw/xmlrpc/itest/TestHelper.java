package dw.xmlrpc.itest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuPageLockedException;

public class TestHelper {
	/**
	 * Assert that the actual Date is equal to the expected one.
	 * Since we don't want to bother with timezones, we add a margin
	 */
	static void assertDatesNear(int year, int month, int day, int hour, int minute, int second, Date actual){
		Date date = buildDate(year,  month, day, hour, minute, second);
		int marginInMs = 24 * 3600 * 1000 + 1;
		assertTrue(Math.abs(date.getTime() - actual.getTime()) < marginInMs);
	}

	static Date buildDate(int year, int month, int day, int hour, int minute, int second){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(year,  month, day, hour, minute, second);
		return cal.getTime();
	}

	public static void assertPagesAreLockForMe(Iterable<String> pageIds, DokuJClient client) throws DokuException {
		for(String pageId : pageIds){
			assertPageIsLockForMe(pageId, client);
		}
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

	public static void assertPagesAreUnlockForMe(Iterable<String> pageIds, DokuJClient client) throws DokuException {
		for(String pageId : pageIds){
			assertPageIsUnlockForMe(pageId, client);
		}
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

	public void assertFileEquals(File expected, File actual){
		junitx.framework.FileAssert.assertBinaryEquals(expected, actual);
	}
}
