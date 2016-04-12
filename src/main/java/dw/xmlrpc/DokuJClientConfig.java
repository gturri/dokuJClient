package dw.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;

import de.timroes.axmlrpc.XMLRPCClient;

public class DokuJClientConfig {
	public static final String defaultUserAgent = "DokuJClient - XmlRpc client for Dokuwiki";
	public static final int defaultTimeoutInSeconds = 30;
	public static final int defaultXMLRPCClientFlags = XMLRPCClient.FLAGS_ENABLE_COOKIES | XMLRPCClient.FLAGS_IGNORE_STATUSCODE;

	private final URL _url;
	URL url(){
		return _url;
	}

	private String _user = null;
	String user(){
		return _user;
	}

	private String _password = null;
	String password(){
		return _password;
	}

	private String _userAgent = DokuJClientConfig.defaultUserAgent;
	String userAgent(){
		return _userAgent;
	}

	public DokuJClientConfig(String url) throws MalformedURLException{
		this(new URL(url));
	}

	public DokuJClientConfig(URL url){
		_url = url;
	}

	public void setUserAgent(String userAgent){
		_userAgent = userAgent;
	}

	public void setUser(String user, String password){
		_user = user;
		_password = password;
	}

	private int _timeoutInSeconds = defaultTimeoutInSeconds;
	public void setTimeOutInSeconds(int timeout){
		_timeoutInSeconds = timeout;
	}

	public int timeoutInSeconds(){
		return _timeoutInSeconds;
	}

	private int _xmlRpcClientFlags = defaultXMLRPCClientFlags;
	public void setXMLRPCClientFlags(int flags){
		_xmlRpcClientFlags = flags;
	}

	public int xmlRpcClientFlags(){
		return _xmlRpcClientFlags;
	}

	private boolean _debug = false;
	public void setDebug(boolean debug){
		_debug = debug;
	}

	public boolean getDebug(){
		return _debug;
	}
}
