package dw;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import dw.exception.DokuException;
import dw.exception.ExceptionConverter;

class CoreClient {
	private XmlRpcClient _client;
	
    public CoreClient(String url, String user, String password) throws MalformedURLException{
    	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    	config.setServerURL(new URL(url));
    	config.setBasicUserName(user);
    	config.setBasicPassword(password);
    	_client = new XmlRpcClient();
    	_client.setConfig(config);
	}
	
	public Object genericQuery(String action) throws DokuException {
		Object[] params = new Object[]{};
		return genericQuery(action, params);
	}
    
	public Object genericQuery(String action, Object param) throws DokuException{
		return genericQuery(action, new Object[]{param});
	}
	
	public Object genericQuery(String action, Object[] params) throws DokuException{
		try {
			return _client.execute(action, params);
		} catch (XmlRpcException e){
			System.out.println(e.toString());
			throw ExceptionConverter.Convert(e);
		}
	}
}
