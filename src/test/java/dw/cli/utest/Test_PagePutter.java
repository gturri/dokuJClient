package dw.cli.utest;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import dw.cli.StdinReader;
import dw.cli.commands.PagePutter;
import dw.xmlrpc.DokuJClient;

public class Test_PagePutter {
	private StdinReader _mockStdinReader;
	private DokuJClient _mockDokuJClient;
	private PagePutter _sut;

	private static final String PAGE_ID = "somePageId";
	private static final String TEXT_ON_CMD_LINE = "some text on cmd line";
	private static final String TEXT_ON_STDIN = "some text on stdin";

	@org.junit.Before
	public void before() throws IOException{
		_mockStdinReader = mock(StdinReader.class);
		_mockDokuJClient = mock(DokuJClient.class);
		when(_mockStdinReader.readStdin()).thenReturn(TEXT_ON_STDIN);

		_sut = new PagePutter(false, _mockStdinReader);
	}

	@org.junit.Test
	public void shouldReadFromStdinIfThereIsTextOnTheCommandLine() throws Exception{
		String[] args = new String[]{
				PAGE_ID,
				TEXT_ON_CMD_LINE
		};

		_sut.run(_mockDokuJClient, args);

		verify(_mockDokuJClient).putPage(eq(PAGE_ID), eq(TEXT_ON_CMD_LINE), anyString(), anyBoolean());
	}

	@org.junit.Test
	public void shouldReadFromStdinIfThereIsNoTextOnTheCommandLine() throws Exception {
		String[] args = new String[]{
				PAGE_ID
		};

		_sut.run(_mockDokuJClient, args);
		verify(_mockDokuJClient).putPage(eq(PAGE_ID), eq(TEXT_ON_STDIN), anyString(), anyBoolean());
	}
}
