package dw.xmlrpc;

import java.util.Set;


public class LockResult {
	private Set<String> _locked;
	public Set<String> locked(){
		return _locked;
	}
	
	private Set<String> _lockfail;
	public Set<String> lockfail(){
		return _lockfail;
	}
	
	private Set<String> _unlocked;
	public Set<String> unlocked(){
		return _unlocked;
	}
	
	private Set<String> _unlockfail;
	public Set<String> unlockfail(){
		return _unlockfail;
	}
	
	public LockResult(Set<String> locked,
			Set<String> lockfail,
			Set<String> unlocked,
			Set<String> unlockfail){
		_locked = locked;
		_lockfail = lockfail;
		_unlocked = unlocked;
		_unlockfail = unlockfail;
	}
	
	public String toString(){
		return "[locked:" + _locked.toString() + "]"
				+ "[lockfail:" + _lockfail.toString() + "]"
				+ "[unlocked:" + _unlocked.toString() + "]"
				+ "[unlockfail:" + _unlockfail.toString() + "]";
	}
	
	public boolean equals(Object o){
		if ( this == o ){
			return true;
		}
		
		if ( o == null ){
			return false;
		}
		
		if ( !(o instanceof LockResult )){
			return false;
		}
		LockResult other = (LockResult) o;
		
		return _locked.equals(other._locked)
				&& _lockfail.equals(other._lockfail)
				&& _unlocked.equals(other._unlocked)
				&& _unlockfail.equals(other._unlockfail);
	}
}
