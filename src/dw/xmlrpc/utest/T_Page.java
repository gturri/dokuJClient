package dw.xmlrpc.utest;

import dw.xmlrpc.Page;
import java.util.Date;

public class T_Page {
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void ShouldntAcceptToBuildWithNullId(){
		new Page(null, 255, new Date(), 0);
	}

	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		Page page = new Page("id", null, null, null);
		page.toString();
	}
}
