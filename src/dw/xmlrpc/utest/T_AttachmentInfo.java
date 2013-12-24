package dw.xmlrpc.utest;

import dw.xmlrpc.AttachmentInfo;

public class T_AttachmentInfo {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		AttachmentInfo info = new AttachmentInfo(null, null, null);
		info.toString();
	}
}
