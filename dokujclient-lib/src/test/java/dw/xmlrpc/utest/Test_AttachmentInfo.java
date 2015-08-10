package dw.xmlrpc.utest;

import static org.junit.Assert.*;
import dw.xmlrpc.AttachmentInfo;

public class Test_AttachmentInfo {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		AttachmentInfo info = new AttachmentInfo(null, null, null);
		assertNotNull(info.toString());
	}
}
