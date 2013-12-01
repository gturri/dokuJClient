package dw.cli.itest;

public class T_PermissionLessUser extends TestHelper {
	@org.junit.Test
	public void shouldntCrashIfUserDoesntHaveEnoughPermissions() throws Exception {
		assertGenericError(runWithArgumentAsPermissionLessUser("putPage", "start", "some text"));
	}
}
