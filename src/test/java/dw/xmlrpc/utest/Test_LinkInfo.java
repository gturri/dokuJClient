package dw.xmlrpc.utest;

import static org.junit.Assert.*;
import dw.xmlrpc.AttachmentDetails;
import dw.xmlrpc.LinkInfo;

public class Test_LinkInfo {
	@org.junit.Test
	public void equals(){
		String link = "http://dokuwiki.org";
		LinkInfo link1 = new LinkInfo("extern", link, link);
		LinkInfo link1bis = new LinkInfo(LinkInfo.Type.extern, link, link);
		LinkInfo link2 = new LinkInfo("local", "start", "http://mywiki?id=start");

		assertTrue(link1.equals(link1bis));
		assertFalse(link1.equals(link2));
		assertFalse(link1bis.equals(link2));

		assertTrue(link1bis.equals(link1));
		assertFalse(link2.equals(link1));
		assertFalse(link2.equals(link1bis));

		assertFalse(link1.equals(null)); //case null
		assertFalse(link1.equals(new AttachmentDetails(null, null, null, null, null, null, null, null))); //Other kind of object
	}

	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		LinkInfo link = new LinkInfo(LinkInfo.Type.local, null, null);
		link.toString();
	}

	@org.junit.Test
	public void equalsLinkInfoHaveSameHashCode(){
		LinkInfo link1 = new LinkInfo("local", "start", "http://something");
		LinkInfo link2 = new LinkInfo("local", "start", "http://something");
		assertEquals(link1.hashCode(), link2.hashCode());
	}
}
