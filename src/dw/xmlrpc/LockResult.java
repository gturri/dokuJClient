package dw.xmlrpc;

import java.util.Set;

/**
 * Describes the result of an attempt to (un)lock pages
 */
public class LockResult {
	private Set<String> _locked;

	/**
	 * Pages successfully locked
	 */
	public Set<String> locked(){
		return _locked;
	}

	private Set<String> _lockfail;

	/**
	 * Pages we failed to lock
	 */
	public Set<String> lockfail(){
		return _lockfail;
	}

	private Set<String> _unlocked;

	/**
	 * Pages successfully unlocked
	 */
	public Set<String> unlocked(){
		return _unlocked;
	}

	private Set<String> _unlockfail;

	/**
	 * Pages we failed to unlock
	 */
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

	@Override
	public String toString(){
		return "[locked:" + _locked.toString() + "]"
				+ "[lockfail:" + _lockfail.toString() + "]"
				+ "[unlocked:" + _unlocked.toString() + "]"
				+ "[unlockfail:" + _unlockfail.toString() + "]";
	}

	@Override
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
