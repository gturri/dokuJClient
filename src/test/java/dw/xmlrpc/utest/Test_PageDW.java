package dw.xmlrpc.utest;

import static org.junit.Assert.*;
import dw.xmlrpc.PageDW;

public class Test_PageDW {
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void ShouldntAcceptToBuildWithNullId(){
		new PageDW(null, 255, 123456789, 123456789, "123");
	}

	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		PageDW page = new PageDW("id", null, null, null, null);
		assertNotNull(page.toString());
	}
}