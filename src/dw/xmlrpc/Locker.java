package dw.xmlrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dw.xmlrpc.exception.DokuException;

//! @cond
class Locker {
	private final CoreClient _client;

	public Locker(CoreClient client){
		_client = client;
	}

	public LockResult lock(String pageId) throws DokuException{
		List<String> pageIds = new ArrayList<String>();
		pageIds.add(pageId);
		return setLocks(pageIds, null);
	}

	public LockResult unlock(String pageId) throws DokuException{
		List<String> pageIds = new ArrayList<String>();
		pageIds.add(pageId);
		return setLocks(null, pageIds);
	}

	@SuppressWarnings("unchecked")
	public LockResult setLocks(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		Map<String, Object> params = BuildParams(pagesToLock, pagesToUnlock);
		Object result = _client.genericQuery("dokuwiki.setLocks", params);
		return BuildLockResult((Map<String, Object>) result);
	}

	private Map<String, Object> BuildParams(List<String> pagesToLock, List<String> pagesToUnlock){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lock", pagesToLock == null ? new Object[]{} : pagesToLock.toArray());
		params.put("unlock", pagesToUnlock == null ? new Object[]{} : pagesToUnlock.toArray());
		return params;
	}

	private LockResult BuildLockResult(Map<String, Object> data){
		Object lockedObj = data.get("locked");
		Object lockfailObj = data.get("lockfail");
		Object unlockedObj = data.get("unlocked");
		Object unlockfailObj = data.get("unlockfailObj");

		return new LockResult(objToStr(lockedObj),
				objToStr(lockfailObj),
				objToStr(unlockedObj),
				objToStr(unlockfailObj));
	}

	private Set<String> objToStr(Object objects){
		return objToStr((Object[]) objects);
	}

	private Set<String> objToStr(Object[] objects){
		Set<String> result = new HashSet<String>();
		if ( objects == null ){
			return result;
		}

		for(Object o : objects){
			result.add((String) o);
		}

		return result;
	}
//! @endcond
}
