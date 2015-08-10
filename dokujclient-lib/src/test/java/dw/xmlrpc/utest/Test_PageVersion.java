package dw.xmlrpc.utest;

import static org.junit.Assert.*;

import java.util.Date;

import dw.xmlrpc.PageVersion;

public class Test_PageVersion {
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void ShouldntAcceptToBuildWithNullId(){
		new PageVersion(null, "author", "127.0.0.1", "E", "summary", new Date(), 123456789);
	}

	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		PageVersion page = new PageVersion("id", null, null, null, null, null, null);
		assertNotNull(page.toString());
	}

	@org.junit.Test
	public void userAndAuthorReturnTheSameValue(){
		PageVersion page = new PageVersion("id", "toto", null, null, null, null, null);
		assertEquals(page.user(), page.author());
	}

	@org.junit.Test
	public void returnTheExpectedDate(){
		Date date = new Date();
		PageVersion page = new PageVersion("id", "author", "127.0.0.1", "E", "summary", new Date(), 123456789);
		assertEquals(date, page.lastModified());
	}
}