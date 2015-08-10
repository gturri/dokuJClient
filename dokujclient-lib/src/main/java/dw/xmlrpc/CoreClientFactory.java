package dw.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;

import de.timroes.axmlrpc.XMLRPCClient;

class CoreClientFactory {
	public static CoreClient Build(String url) throws MalformedURLException{
		return Build(url, DokuJClientConfig.defaultUserAgent);
	}

	public static CoreClient Build(String url, String userAgent) throws MalformedURLException{
		return Build(new URL(url), userAgent);
	}

	public static CoreClient Build(DokuJClientConfig dokuConfig){
		return Build(dokuConfig.url(), dokuConfig.userAgent(), dokuConfig.timeoutInSeconds(), dokuConfig.xmlRpcClientFlags());
	}

	public static CoreClient Build(URL url, String userAgent) {
		return Build(url, userAgent, DokuJClientConfig.defaultTimeoutInSeconds, DokuJClientConfig.defaultXMLRPCClientFlags);
	}

	public static CoreClient Build(URL url, String userAgent, int timeoutInSeconds, int xMLRPCClientFlags) {
		XMLRPCClient xmlRpcClient = new XMLRPCClient(url, userAgent, xMLRPCClientFlags);
		xmlRpcClient.setTimeout(timeoutInSeconds);
		return Build(xmlRpcClient);
	}

	public static CoreClient Build(XMLRPCClient xmlRpcClient){
		return new CoreClient(xmlRpcClient);
	}
}
