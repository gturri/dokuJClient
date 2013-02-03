package dw.xmlrpc;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import dw.xmlrpc.exception.DokuException;

//! @cond
class CoreClient {
	private XmlRpcClient _client;
	private Logger _logger = null;

	public void setLogger(Logger logger){
		_logger = logger;
	}

    public CoreClient(XmlRpcClient client){
    	_client = client;
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
//! @endcond
}
