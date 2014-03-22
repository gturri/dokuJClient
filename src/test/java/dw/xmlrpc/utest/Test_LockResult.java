package dw.xmlrpc.utest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import dw.xmlrpc.LockResult;

public class Test_LockResult {

	@org.junit.Test
	public void LockResultEquality(){
		Set<String> locked1 = new HashSet<String>();
		locked1.add("myPage");
		Set<String> locked2 = new TreeSet<String>();
		locked2.add("myPage");
		Set<String> locked3 = new HashSet<String>();
		locked3.add("yourPage");

		LockResult res1 = BuildLockResult(locked1);
		LockResult res2 = BuildLockResult(locked2);
		LockResult res3 = BuildLockResult(locked3);

		assertTrue(res1.equals(res2));
		assertFalse(res1.equals(res3));
		assertFalse(res2.equals(res3));

		assertFalse(res3.equals(res2));
		assertFalse(res3.equals(res1));
		assertTrue(res2.equals(res1));
	}

	@org.junit.Test
	public void equalsLockResultHaveSameHashCode(){
		Set<String> locked1 = new HashSet<String>();
		locked1.add("myPage");
		Set<String> locked2 = new TreeSet<String>();
		locked2.add("myPage");

		LockResult result1 = BuildLockResult(locked1);
		LockResult result2 = BuildLockResult(locked2);

		System.out.println(result1.hashCode());
		System.out.println(result2.hashCode());
		assertEquals(result1.hashCode(), result2.hashCode());
	}

	private LockResult BuildLockResult(Set<String> locked){
		Set<String> emptySet = new HashSet<String>();
		return new LockResult(locked, emptySet, emptySet, emptySet);
	}
}
