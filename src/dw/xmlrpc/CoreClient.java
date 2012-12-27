package dw.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.ExceptionConverter;

class CoreClient {
	private XmlRpcClient _client;
	private Logger _logger = null;
	
	public void setLogger(Logger logger){
		_logger = logger;
	}
	
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
			if ( _logger != null ){
				_logger.log(Level.INFO, "Caught exception when executing action " + action + ": " + e.toString());
				_logger.log(Level.FINEST, "Details of the exception: ", e);
			}
			throw ExceptionConverter.Convert(e);
		}
	}
}
