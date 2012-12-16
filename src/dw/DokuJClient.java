package dw;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class DokuJClient {
	private XmlRpcClient _client;
	
	public DokuJClient(String url) throws MalformedURLException{
		this(url, "", "");
	}
	
    public DokuJClient(String url, String user, String password) throws MalformedURLException{
    	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    	config.setServerURL(new URL(url));
    	config.setBasicUserName(user);
    	config.setBasicPassword(password);
    	_client = new XmlRpcClient();
    	_client.setConfig(config);
	}
    
    public Integer getTime() throws XmlRpcException{
    	Object[] params = new Object[]{};
		Integer result = (Integer) _client.execute("dokuwiki.getTime", params);
		return result;
    }
    
    public Integer getXMLRPCAPIVersion() throws XmlRpcException{
    	Object[] params = new Object[]{};
		Integer result = (Integer) _client.execute("dokuwiki.getXMLRPCAPIVersion", params);
		return result;
    }
    
	public String getVersion() throws XmlRpcException{
		Object[] params = new Object[]{};
		String result = (String) _client.execute("dokuwiki.getVersion", params);
		return result;
	}
	
	public Set<Page> getPages(String namespace) throws XmlRpcException{
		return getPages(namespace, new HashMap<String, Object>());
	}
	
	@SuppressWarnings("unchecked")
	public Set<Page> getPages(String namespace, Map<String, Object> options) throws XmlRpcException{
		Set<Page> res = null;

		List<Object> params = new ArrayList<Object>();
		params.add(namespace);
		params.add(options == null ? "" : options);
		Object result = null;
		
		result = _client.execute("dokuwiki.getPagelist", params);
		List<HashMap<String, Object>> resList = new ArrayList<HashMap<String, Object>>();
		for(Object o : (Object[]) result ){
			resList.add((HashMap<String, Object>) o);
		}

		res = new HashSet<Page>();
		for ( HashMap<String, Object> pageData : resList){
			Page page = new Page((String) pageData.get("id"),
					(Integer) pageData.get("rev"),
					(Integer) pageData.get("mtime"),
					(Integer) pageData.get("size"));
			res.add(page);
		}

		return res;
	}
	
	public Object genericQuery(String action) throws XmlRpcException {
		Object[] params = new Object[]{};
		return genericQuery(action, params);
	}
	
	public Object genericQuery(String action, Object[] params) throws XmlRpcException{
		return _client.execute(action, params);
	}
}
