package dw.xmlrpc.utest;

import dw.xmlrpc.PageDW;

public class T_PageDW {
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void ShouldntAcceptToBuildWithNullId(){
		new PageDW(null, 255, 123456789, 123456789);
	}

	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		PageDW page = new PageDW("id", null, null, null);
		page.toString();
	}
}