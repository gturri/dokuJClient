package dw.xmlrpc;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import dw.xmlrpc.exception.DokuException;

//! @cond
class CoreClient {
	private XMLRPCClient _client;
	private Logger _logger = null;

	public void setLogger(Logger logger){
		_logger = logger;
	}

    public CoreClient(XMLRPCClient client){
    	_client = client;
    }

    public Map<String, String> cookies(){
    	return _client.getCookies();
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
			return _client.call(action, params);
		} catch (XMLRPCException e){
			if ( _logger != null ){
				_logger.log(Level.INFO, "Caught exception when executing action " + action + ": " + e.toString());
				_logger.log(Level.FINEST, "Details of the exception: ", e);
			}
			throw ExceptionConverter.Convert(e);
		}
	}
//! @endcond
}
