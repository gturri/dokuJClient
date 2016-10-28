package dw.fuse;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

import com.sun.jna.ptr.ByteByReference;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.PageDW;
import dw.xmlrpc.PageInfo;
import dw.xmlrpc.exception.DokuException;
import dw.xmlrpc.exception.DokuPageDoesNotExistException;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.FlockCommand;
import net.fusejna.FuseException;
import net.fusejna.StructFlock.FlockWrapper;
import net.fusejna.StructFuseFileInfo.FileInfoWrapper;
import net.fusejna.StructStat.StatWrapper;
import net.fusejna.StructStatvfs.StatvfsWrapper;
import net.fusejna.StructTimeBuffer.TimeBufferWrapper;
import net.fusejna.XattrFiller;
import net.fusejna.XattrListFiller;
import net.fusejna.types.TypeMode.ModeWrapper;
import net.fusejna.types.TypeMode.NodeType;
import net.fusejna.util.FuseFilesystemAdapterAssumeImplemented;
import net.fusejna.util.FuseFilesystemAdapterFull;


public class DwFS extends FuseFilesystemAdapterFull {
	private final DokuJClient client;
	
	public static void main(final String... args) throws Exception
	{
		DokuJClient client = new DokuJClient("http://localhost/dokuwikiITestsForXmlRpcClient_dokuwiki-2016-06-26/lib/exe/xmlrpc.php",
				"xmlrpcuser", "xmlrpc");
		if (args.length != 1) {
			System.err.println("Usage: DwFS <mountpoint>");
			System.exit(1);
		}
		new DwFS(client).log(true).mount(args[0]);
	}
	
	public DwFS(DokuJClient client){
		super();
		this.client = client;
	}

	@Override
	public int getattr(final String path, final StatWrapper stat)
	{
		final String pageId = path.replace('/', ':');
		try {
			try {
				client.getPageInfo(pageId);
				final String content = client.getPage(pageId);
				stat.setMode(NodeType.FILE).size(content.length());
				return 0;
			} catch(DokuPageDoesNotExistException e){ }

			try {
				client.getPagelist(pageId);
				stat.setMode(NodeType.DIRECTORY);
				return 0;
			} catch(DokuPageDoesNotExistException e){ }
		} catch(DokuException e){
			return handleDokuException(e);
		}

		return -ErrorCodes.ENOENT();
	}
	
	private int handleDokuException(DokuException e){
		System.out.println(e);
		return -ErrorCodes.EIO();
	}

	@Override
	public int read(final String path, final ByteBuffer buffer, final long size, final long offset, final FileInfoWrapper info)
	{
		final String pageId = path.replace('/', ':');
		String content;
		try {
			content = client.getPage(pageId);
		} catch (DokuException e) {
			return handleDokuException(e);
		}

		// Compute substring that we are being asked to read
		final String s = content.substring((int) offset, (int) Math.max(offset, Math.min(content.length() - offset, offset + size)));
		buffer.put(s.getBytes());
		return s.getBytes().length;
	}

	@Override
	public int readdir(final String path, final DirectoryFiller filler)
	{
		final String pageId = path.replace('/', ':');
		List<PageDW> pages;
		try {
			pages = client.getPagelist(pageId);
		} catch (DokuException e) {
			return handleDokuException(e);
		}

		for(PageDW page : pages){
			filler.add(page.id());
		}
		return 0;
	}
}
