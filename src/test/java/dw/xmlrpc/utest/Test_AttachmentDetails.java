package dw.xmlrpc.utest;

import dw.xmlrpc.AttachmentDetails;

public class Test_AttachmentDetails {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		AttachmentDetails details = new AttachmentDetails(null, null, null, null, null, null);
		details.toString();
	}
}
