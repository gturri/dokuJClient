package dw.xmlrpc.utest;

import static org.junit.Assert.assertNotNull;
import dw.xmlrpc.MediaChange;

public class Test_MediaChange {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		MediaChange change = new MediaChange(null, null, null, null, null, null);
		assertNotNull(change.toString());
	}
}
