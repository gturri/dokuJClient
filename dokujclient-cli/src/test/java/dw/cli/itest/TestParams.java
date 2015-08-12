package dw.cli.itest;

public class TestParams {
	public final String url;
	public final String wikiVersion;
	public final Integer apiVersion;
	public final Integer rpcVersionSupported;
	public final String name;

	public static final String urlPrefix = "http://localhost/";
	public static final String urlSuffix = "/lib/exe/xmlrpc.php";

	public TestParams(String name, String wikiVersion, Integer apiVersion, Integer rpcVersionSupported){
        this.name = name;
		this.url = urlPrefix  +"dokuwikiITestsForXmlRpcClient_" + name + urlSuffix;
		this.wikiVersion = wikiVersion;
		this.apiVersion = apiVersion;
		this.rpcVersionSupported = rpcVersionSupported;
	}

	public static final String user = "xmlrpcuser";
	public static final String password = "xmlrpc";

	public static final String writerLogin = "writeruser";
	public static final String writerPwd = "writer";

	private static final String localRoot = "src/test/resources/testEnvironment";
	public static final String localFileToUpload = localRoot +  "/list-plus.gif";
	public static final String localFile2ToUpload = localRoot + "/at.gif";
}
