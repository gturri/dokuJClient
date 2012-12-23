package utest;

import static org.junit.Assert.*;
import dw.LinkInfo;

public class T_LinkInfo {
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
	}
}
