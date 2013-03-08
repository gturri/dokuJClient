package dw.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

class CoreClientFactory {
	public static CoreClient Build(String url, String user, String password) throws MalformedURLException{
       	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    	config.setServerURL(new URL(url));
    	config.setBasicUserName(user);
    	config.setBasicPassword(password);
    	XmlRpcClient xmlRpcClient = new XmlRpcClient();
    	xmlRpcClient.setConfig(config);
    	config.setUserAgent(DokuJClientConfig.defaultUserAgent);
    	return new CoreClient(xmlRpcClient);
	}

	public static CoreClient Build(DokuJClientConfig dokuConfig){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    	config.setServerURL(dokuConfig.url());
    	if ( dokuConfig.user() != null ){
    		config.setBasicUserName(dokuConfig.user());
    		config.setBasicPassword(dokuConfig.password());
    	}

    	if ( dokuConfig.userAgent() != null ){
    		config.setUserAgent(dokuConfig.userAgent());
    	} else {
    		config.setUserAgent(DokuJClientConfig.defaultUserAgent);
    	}

    	XmlRpcClient xmlRpcClient = new XmlRpcClient();
    	xmlRpcClient.setConfig(config);

    	return new CoreClient(xmlRpcClient);
	}

	public static CoreClient Build(XmlRpcClient xmlRpcClient){
		return new CoreClient(xmlRpcClient);
	}
}
