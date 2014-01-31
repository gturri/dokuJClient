package dw.xmlrpc.utest;

import dw.xmlrpc.AttachmentInfo;

public class Test_AttachmentInfo {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		AttachmentInfo info = new AttachmentInfo(null, null, null);
		info.toString();
	}
}
