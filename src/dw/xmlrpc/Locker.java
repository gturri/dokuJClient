package dw.xmlrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dw.xmlrpc.exception.DokuException;

class Locker {
	private CoreClient _client;
	
	public Locker(CoreClient client){
		_client = client;
	}
	
	@SuppressWarnings("unchecked")
	public LockResult setLocks(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		LockResult result = null;
		try {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lock", pagesToLock == null ? new ArrayList<String>() :pagesToLock);
		params.put("unlock", pagesToUnlock == null ? new ArrayList<String>() : pagesToUnlock);
		
		Object resultObj = _client.genericQuery("dokuwiki.setLocks", params);
		Map<String, Object> resultMap = (Map<String, Object>) resultObj;
		
		Object lockedObj = resultMap.get("locked");
		Object lockfailObj = resultMap.get("lockfail");
		Object unlockedObj = resultMap.get("unlocked");
		Object unlockfailObj = resultMap.get("unlockfailObj");
		
		result = new LockResult(objToStr(lockedObj),
				objToStr(lockfailObj),
				objToStr(unlockedObj),
				objToStr(unlockfailObj));
		
		} catch (Exception e){
			System.out.println(e.toString());
		}
		return result;
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
}
