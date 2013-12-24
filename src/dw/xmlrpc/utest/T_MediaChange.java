package dw.xmlrpc.utest;

import dw.xmlrpc.MediaChange;

public class T_MediaChange {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		MediaChange change = new MediaChange(null, null, null, null, null, null);
		change.toString();
	}
}
