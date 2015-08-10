package dw.xmlrpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//! @cond
class ObjectConverter {

	static List<PageDW> toPageDW(Object[] objs){
		List<PageDW> result = new ArrayList<PageDW>();
		for(Object o : objs){
			result.add(toPageDW(o));
		}
		return result;
	}

	static PageDW toPageDW(Object o){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) o;
		String id = (String) map.get("id");
		Integer size = (Integer) map.get("size");
		Integer version = (Integer) map.get("rev");
		Integer mtime = (Integer) map.get("mtime");
		String hash = (String) map.get("hash");
		return new PageDW(id, size, version, mtime, hash);
	}

	static List<SearchResult> toSearchResult(Object[] objs){
		List<SearchResult> result = new ArrayList<SearchResult>();
		for ( Object o : objs ){
			result.add(toSearchResult(o));
		}
		return result;
	}

	static SearchResult toSearchResult(Object o){
		@SuppressWarnings("unchecked")
		Map<String, Object> mapResult = (Map<String, Object>) o;

		String id = (String) mapResult.get("id");
		String title  = (String) mapResult.get("title");
		if ( title == null){
			//for DW up to 2012-01-25
			title = id;
		}
		Integer rev = (Integer) mapResult.get("rev");
		Integer mtime = (Integer) mapResult.get("mtime");
		Integer size = (Integer) mapResult.get("size");
		Integer score = (Integer) mapResult.get("score");
		String snippet = (String) mapResult.get("snippet");

		return new SearchResult(id, title, rev, mtime, score, snippet, size);
	}

	static PageInfo toPageInfo(Object o){
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) o;
		String name = (String) resMap.get("name");
		Date modified = (Date) resMap.get("lastModified");
		String author = (String) resMap.get("author");
		Integer version = (Integer) resMap.get("version");
		return new PageInfo(name, modified, author, version);
	}

	static List<Page> toPage(Object[] objs){
		List<Page> result = new ArrayList<Page>();
		for(Object o : objs){
			result.add(toPage(o));
		}
		return result;
	}

	static Page toPage(Object o){
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) o;
		String id = (String) resMap.get("id");
		Integer perms = toPerms(resMap.get("perms"));
		Date lastModified = (Date) resMap.get("lastModified");
		Integer size = (Integer) resMap.get("size");
		return new Page(id, perms, lastModified, size);
	}

	static List<String> toString(Object[] objs){
		List<String> result = new ArrayList<String>();
		for(Object o : objs){
			result.add((String) o);
		}
		return result;
	}

	static List<LinkInfo> toLinkInfo(Object[] objs){
		List<LinkInfo> result = new ArrayList<LinkInfo>();
		for ( Object o : objs ){
			result.add(toLinkInfo(o));
		}
		return result;
	}

	static LinkInfo toLinkInfo(Object o){
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) o;
		String type = (String) resMap.get("type");
		String page = (String) resMap.get("page");
		String href = (String) resMap.get("href");
		return new LinkInfo(type, page, href);
	}

	static List<PageChange> toPageChange(Object[] objs){
		List<PageChange> result = new ArrayList<PageChange>();
		for(Object o : objs){
			result.add(toPageChange(o));
		}
		return result;
	}

	static PageChange toPageChange(Object o){
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) o;
		String id = (String) resMap.get("name");
		Integer perms = toPerms(resMap.get("perms"));
		Date lastModified = (Date) resMap.get("lastModified");
		Integer size = null;
		try {
			size = (Integer) resMap.get("size");
		} catch (ClassCastException e)
		{
			//Sometimes Dokuwiki fails to get a size (it seems to be the case if the
			//corresponding page doesn't exist anymore)
			size = null;
		}
		String author = (String) resMap.get("author");
		Integer version = (Integer) resMap.get("version");
		return new PageChange(id, perms, lastModified, size, author, version);

	}

	static List<PageVersion> toPageVersion(Object[] objs, String pageId){
		List<PageVersion> result = new ArrayList<PageVersion>();
		for ( Object o : objs ){
			result.add(toPageVersion(o, pageId));
		}
		return result;
	}

	static PageVersion toPageVersion(Object o, String pageId){
		@SuppressWarnings("unchecked")
		Map<String, Object> resMap = (Map<String, Object>) o;
		String author = (String) resMap.get("author");
		if ( author == null ){
			author = (String) resMap.get("user");
		}
		String ip = (String) resMap.get("ip");
		String type = (String) resMap.get("type");
		String summary = (String) resMap.get("sum");
		Date modified = (Date) resMap.get("modified");
		Integer version = (Integer) resMap.get("version");
		return new PageVersion(pageId, author, ip, type, summary, modified, version);
	}

	static Integer toPerms(Object o){
		//Because DW may sometime return a string instead
		//(fixed after Adora Belle (2012-10-03))
		if (o instanceof Integer){
			return (Integer) o;
		}
		return Integer.valueOf((String) o);
	}
}
//! @endcond

