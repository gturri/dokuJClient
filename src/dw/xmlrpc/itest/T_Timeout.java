package dw.xmlrpc.itest;

import java.net.MalformedURLException;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.DokuJClientConfig;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuTimeoutException;

public class T_Timeout {

	@org.junit.Test(expected=DokuTimeoutException.class)
	public void iCanStopIfItTakesTooLongToRespond() throws MalformedURLException, DokuException{
		DokuJClient client = buildClientWithTimeOut(1);
		makeADummyCall(client);
	}

	@org.junit.Test
	public void iCanWaitEvenItResponseTakesLong() throws MalformedURLException, DokuException{
		DokuJClient client = buildClientWithTimeOut(60);
		makeADummyCall(client);
	}

	@org.junit.Test
	public void timeoutOfZeroMeansWaitAsLongAsYouNeed() throws MalformedURLException, DokuException{
		DokuJClient client = buildClientWithTimeOut(60);
		makeADummyCall(client);
	}

	private DokuJClient buildClientWithTimeOut(int timeoutInSeconds) throws MalformedURLException, DokuException{
		DokuJClientConfig config = new DokuJClientConfig(TestParams.sleepingWiki);
		config.setUser(TestParams.user, TestParams.password);
		config.setTimeOutInSeconds(timeoutInSeconds);
		return new DokuJClient(config);
	}

	private void makeADummyCall(DokuJClient client) throws DokuException{
		client.genericQuery("wiki.getAllPages");
	}
}
