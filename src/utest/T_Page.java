package utest;

import static org.junit.Assert.*;
import dw.Page;

public class T_Page {
	@org.junit.Test
	public void getNamespaceAndName(){
		Page page = new Page("ns:name");
		assertEquals("name", page.name());
		assertEquals("ns", page.namespace());
	}
}
