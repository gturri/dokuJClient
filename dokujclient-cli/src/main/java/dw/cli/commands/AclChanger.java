package dw.cli.commands;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;

import dw.cli.Command;
import dw.cli.Output;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;

public class AclChanger extends Command {
	public enum aclAction {
		add,
		delete
	}

	private final aclAction _action;

	public AclChanger(aclAction action){
		_action = action;
	}

	@Override
	protected void registerParameters(JSAP jsap) throws JSAPException {
			jsap.registerParameter(new FlaggedOption("scope")
			.setStringParser(JSAP.STRING_PARSER)
			.setRequired(true)
			.setLongFlag("scope"));

			jsap.registerParameter(new FlaggedOption("username")
			.setStringParser(JSAP.STRING_PARSER)
			.setRequired(true)
			.setLongFlag("username"));

			if ( _action == aclAction.add ){
				jsap.registerParameter(new FlaggedOption("permission")
				.setStringParser(JSAP.INTEGER_PARSER)
				.setRequired(true)
				.setLongFlag("permission"));
			}
	}

	@Override
	protected Output run(DokuJClient dokuClient) throws DokuException {
		String scope = _config.getString("scope");
		String username = _config.getString("username");
		boolean success;

		if ( _action == aclAction.add ){
			int permission = _config.getInt("permission");
			success = dokuClient.addAcl(scope, username, permission);
		} else {
			success = dokuClient.delAcl(scope, username);
		}

		if ( success ){
			return new Output();
		} else {
			return new Output("Acl change returned false.", 1);
		}
	}
}
