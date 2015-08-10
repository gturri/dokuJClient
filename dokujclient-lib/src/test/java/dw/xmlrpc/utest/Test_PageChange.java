package dw.xmlrpc.utest;

import static org.junit.Assert.*;
import dw.xmlrpc.PageChange;

import java.util.Date;

public class Test_PageChange {
	@org.junit.Test(expected=IllegalArgumentException.class)
	public void ShouldntAcceptToBuildWithNullId(){
		new PageChange(null, 255, new Date(), 0,"author", 123456789);
	}

	@org.junit.Test
	public void toStringShouldntThrowNullRef(){
		PageChange page = new PageChange("id", null, null, null, null, null);
		assertNotNull(page.toString());
	}
}