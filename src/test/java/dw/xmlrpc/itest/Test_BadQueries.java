package dw.xmlrpc.itest;

import java.net.MalformedURLException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.Rule;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.DokuJClientConfig;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuTimeoutException;
import dw.xmlrpc.exception.DokuUnknownException;

public class Test_BadQueries {

	private final int port = 8080;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(port);

	@org.junit.Test(expected=DokuTimeoutException.class)
	public void iCanStopIfItTakesTooLongToRespond() throws MalformedURLException, DokuException{
		setTimeout();
		DokuJClient client = buildClientWithTimeOut(1);
		makeADummyCall(client);
	}

	@org.junit.Test
	public void iCanWaitEvenIfResponseTakesLong() throws MalformedURLException, DokuException{
		setTimeout();
		DokuJClient client = buildClientWithTimeOut(60);
		makeADummyCall(client);
	}

	@org.junit.Test
	public void timeoutOfZeroMeansWaitAsLongAsYouNeed() throws MalformedURLException, DokuException{
		setTimeout();
		DokuJClient client = buildClientWithTimeOut(0);
		makeADummyCall(client);
	}

	private void setTimeout(){
		addRequestProcessingDelay(5*1000);
		stubFor(post(urlEqualTo("/lib/exe/xmlrpc.php"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("<methodResponse><params><param><value><array><data></data></array></value></param></params></methodResponse>")
						));
	}

	@org.junit.Test(expected=DokuUnknownException.class)
	public void corruptedReply() throws Exception {
		stubFor(post(urlEqualTo("/lib/exe/xmlrpc.php"))
				.willReturn(aResponse()
						.withStatus(200)
						.withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
		makeADummyCall(buildClient());
	}

	private DokuJClient buildClient() throws MalformedURLException, DokuException {
		return buildClientWithTimeOut(0);
	}

	private DokuJClient buildClientWithTimeOut(int timeoutInSeconds) throws MalformedURLException, DokuException{
		DokuJClientConfig config = new DokuJClientConfig("http://localhost:" + port + "/lib/exe/xmlrpc.php");
		config.setTimeOutInSeconds(timeoutInSeconds);
		return new DokuJClient(config);
	}

	private void makeADummyCall(DokuJClient client) throws DokuException{
		client.genericQuery("wiki.getAllPages");
	}
}
