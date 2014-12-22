package dw.xmlrpc.itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuIncompatibleVersionException;
import dw.xmlrpc.exception.DokuUnauthorizedException;

@RunWith(value = Parameterized.class)
public class Test_AclChanger extends TestHelper {
	private final DokuJClient _client;
	private final String _pageId = "ns1:start";

	TestParams _params;

	public Test_AclChanger(TestParams params) throws MalformedURLException, DokuException {
		_client = new DokuJClient(params.url, TestParams.user, TestParams.password);
		_params = params;
	}

	@Parameters
	 public static Collection<Object[]> data() {
		 return TestParams.data();
	 }

	@org.junit.Test
	public void canAddAndRemoveAcl() throws MalformedURLException, DokuException{
		if ( dwIsTooOldAndDoesntSupportAclChanges()){
			assertExpectedExceptionIsThrown();
		} else {
			assertCanAddAndRemoveAcl();
		}
	}

	private void assertExpectedExceptionIsThrown() throws DokuException{
		try {
			_client.addAcl(_pageId, TestParams.unauthorizedLogin, 1);
		}
		catch(DokuIncompatibleVersionException e){
			return;
		}
		fail("Didn't receive the expected exception");
	}

	private void assertCanAddAndRemoveAcl() throws MalformedURLException, DokuException{
		assertCanRead(false);
		_client.addAcl(_pageId, TestParams.unauthorizedLogin, 1);
		assertCanRead(true);
		_client.delAcl(_pageId,  TestParams.unauthorizedLogin);
		assertCanRead(false);

	}

	private boolean dwIsTooOldAndDoesntSupportAclChanges() {
		return _params.apiVersion < 8 || _params.name.equals("dokuwiki-2013-05-10");
	}

	private void assertCanRead(boolean shouldBeAbleToRead) throws MalformedURLException, DokuException{
		DokuJClient unauthorizedClient = new DokuJClient(_params.url, TestParams.unauthorizedLogin, TestParams.unauthorizedPwd);
		boolean couldRead = true;

		try {
			unauthorizedClient.getPage(_pageId);
		} catch(DokuUnauthorizedException e){
			couldRead = false;
		}

		assertEquals(shouldBeAbleToRead, couldRead);
	}
}
