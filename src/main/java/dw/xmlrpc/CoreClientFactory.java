package dw.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;

import de.timroes.axmlrpc.XMLRPCClient;

class CoreClientFactory {
	public static CoreClient build(String url) throws MalformedURLException{
		return build(url, DokuJClientConfig.defaultUserAgent);
	}

	public static CoreClient build(String url, String userAgent) throws MalformedURLException{
		return build(new URL(url), userAgent);
	}

	public static CoreClient build(DokuJClientConfig dokuConfig){
		int xmlRpcClientFlags = dokuConfig.getDebug()
				? dokuConfig.xmlRpcClientFlags() | XMLRPCClient.FLAGS_DEBUG
				: dokuConfig.xmlRpcClientFlags();
		return build(dokuConfig.url(), dokuConfig.userAgent(), dokuConfig.timeoutInSeconds(), xmlRpcClientFlags);
	}

	public static CoreClient build(URL url, String userAgent) {
		return build(url, userAgent, DokuJClientConfig.defaultTimeoutInSeconds, DokuJClientConfig.defaultXMLRPCClientFlags);
	}

	public static CoreClient build(URL url, String userAgent, int timeoutInSeconds, int xMLRPCClientFlags) {
		XMLRPCClient xmlRpcClient = new XMLRPCClient(url, userAgent, xMLRPCClientFlags);
		xmlRpcClient.setTimeout(timeoutInSeconds);
		return build(xmlRpcClient);
	}

	public static CoreClient build(XMLRPCClient xmlRpcClient){
		return new CoreClient(xmlRpcClient);
	}
}
