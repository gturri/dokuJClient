package dw.xmlrpc.itest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestParams {
	public final String localPath;
	public final String url;
	public final String wikiVersion;
	public final Integer apiVersion;
	public final Integer rpcVersionSupported;

	public static final String urlPrefix = "http://localhost/";
	public static final String urlSuffix = "/lib/exe/xmlrpc.php";

	public TestParams(String name, String wikiVersion, Integer apiVersion, Integer rpcVersionSupported){
		this.localPath = "dokuwikiITestsForXmlRpcClient_" + name;
		this.url = urlPrefix  + localPath + urlSuffix;
		this.wikiVersion = wikiVersion;
		this.apiVersion = apiVersion;
		this.rpcVersionSupported = rpcVersionSupported;
	}


	public static final String user = "xmlrpcuser";
	public static final String password = "xmlrpc";

	public static final String writerLogin = "writeruser";
	public static final String writerPwd = "writer";

	public static final String unauthorizedLogin = "norightsuser";
	public static final String unauthorizedPwd = "norights";
	public static final String wikiTitle = "test xmlrpc";

	public static final String localFileToUpload = "dw/xmlrpc/itest/testEnvironment/list-plus.gif";

	public static final String sleepingWiki = urlPrefix  + "dokuwikiITestsForXmlRpcClient_sleepingWiki" + urlSuffix;

	 public static Collection<Object[]> data() {
		 List<Object[]> result = new ArrayList<Object[]>();
		 result.add(new Object[]{new TestParams("dokuwiki-2012-01-25b", "Release 2012-01-25b \"Angua\"", 6, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2012-10-13", "Release 2012-10-13 \"Adora Belle\"", 7, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2013-05-10", "Release 2013-05-10 \"Weatherwax\"", 8, 2)});
		 return result;
	}
}
