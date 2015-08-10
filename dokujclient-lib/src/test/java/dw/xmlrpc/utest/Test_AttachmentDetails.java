package dw.xmlrpc.utest;

import static org.junit.Assert.*;
import dw.xmlrpc.AttachmentDetails;

public class Test_AttachmentDetails {
	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		AttachmentDetails details = new AttachmentDetails(null, null, null, null, null, null);
		assertNotNull(details.toString());
	}
}
