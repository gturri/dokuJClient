package dw.xmlrpc;

import java.util.Set;

/**
 * Describes the result of an attempt to (un)lock pages
 */
public class LockResult {
	private final Set<String> _locked;

	/**
	 * @return The pages successfully locked
	 */
	public Set<String> locked(){
		return _locked;
	}

	private final Set<String> _lockfail;

	/**
	 * @return The pages we failed to lock
	 */
	public Set<String> lockfail(){
		return _lockfail;
	}

	private final Set<String> _unlocked;

	/**
	 * @return The pages successfully unlocked
	 */
	public Set<String> unlocked(){
		return _unlocked;
	}

	private final Set<String> _unlockfail;

	/**
	 * @return The pages we failed to unlock
	 */
	public Set<String> unlockfail(){
		return _unlockfail;
	}

	/**
	 * @return Whether at least a page failed to be locked or unlocked
	 */
	public boolean hasFailure(){
		return !_unlockfail.isEmpty() || !_lockfail.isEmpty();
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

	@Override
	public int hashCode(){
		//Any arbitrary constant will do
		return 0;
	}
}
