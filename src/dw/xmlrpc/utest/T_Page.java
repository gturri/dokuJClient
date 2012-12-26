package dw.xmlrpc.utest;

import static org.junit.Assert.*;
import dw.xmlrpc.Page;

public class T_Page {
	@org.junit.Test
	public void getNamespaceAndName(){
		Page page = new Page("ns:name");
		assertEquals("name", page.name());
		assertEquals("ns", page.namespace());
	}
}
