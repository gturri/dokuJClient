package dw.cli.commands;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.exception.DokuException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.lang.RuntimeException;

public class CliVersion extends SimpleCommand {
	@Override
	protected String query(DokuJClient dokuClient) throws DokuException {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = CliVersion.class.getResourceAsStream("/application.properties");

			// load a properties file
			prop.load(input);

			return prop.getProperty("application.version");
		} catch (IOException e) {
		    throw new RuntimeException("Can't open property file to read version", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				  //Shouldn't matter that much
				}
			}
		}
	}
}
