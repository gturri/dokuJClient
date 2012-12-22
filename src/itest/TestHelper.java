package itest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import dw.DokuJClient;
import dw.exception.DokuException;
import dw.exception.DokuPageLockedException;

public class TestHelper {
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
