package dw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dw.exception.DokuException;

class Locker {
	private CoreClient _client;
	
	public Locker(CoreClient client){
		_client = client;
	}
	
	public void setLock(List<String> pagesToLock, List<String> pagesToUnlock) throws DokuException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lock", pagesToLock == null ? new ArrayList<String>() :pagesToLock);
		params.put("unlock", pagesToUnlock == null ? new ArrayList<String>() : pagesToUnlock);
		_client.genericQuery("dokuwiki.setLocks", params);
	}
	
	public void lock(String pageId) throws DokuException{
		List<String> pageIds = new ArrayList<String>();
		pageIds.add(pageId);
		setLock(pageIds, null);
	}
	
	public void unlock(String pageId) throws DokuException{
		List<String> pageIds = new ArrayList<String>();
		pageIds.add(pageId);
		setLock(null, pageIds);
	}
}
