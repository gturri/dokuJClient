package dw.xmlrpc.itest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestParams {
	public final String localPath;
	public final String url;
	public final String urlToWikiNotConfiguredToAcceptRpcQueries;
	public final String wikiVersion;
	public final Integer apiVersion;
	public final Integer rpcVersionSupported;
	public final String name;

	public static final String urlPrefix = "http://localhost/";
	public static final String urlSuffix = "/lib/exe/xmlrpc.php";

	public TestParams(String name, String wikiVersion, Integer apiVersion, Integer rpcVersionSupported){
		this.name = name;
		this.localPath = "dokuwikiITestsForXmlRpcClient_" + name;
		this.url = urlPrefix  + localPath + urlSuffix;
		this.urlToWikiNotConfiguredToAcceptRpcQueries = urlPrefix + localPath + "_noRpc" + urlSuffix;
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

    private static final String localRoot = "src/test/resources/testEnvironment";
	public static final String localFileToUpload = localRoot +  "/list-plus.gif";
	public static final String localFile2ToUpload = localRoot + "/at.gif";

	 public static Collection<Object[]> data() {
		 List<Object[]> result = new ArrayList<Object[]>();
		 result.add(new Object[]{new TestParams("dokuwiki-2012-01-25b", "Release 2012-01-25b \"Angua\"", 6, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2012-10-13", "Release 2012-10-13 \"Adora Belle\"", 7, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2013-05-10", "Release 2013-05-10 \"Weatherwax\"", 8, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2013-12-08", "Release 2013-12-08 \"Binky\"", 8, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2014-05-05", "Release 2014-05-05 \"Ponder Stibbons\"", 9, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2014-09-29a", "Release 2014-09-29a \"Hrun\"", 9, 2)});
		 result.add(new Object[]{new TestParams("dokuwiki-2015-08-10", "Release 2015-08-10 \"Detritus\"", 9, 2)});
		 return result;
	}
}
