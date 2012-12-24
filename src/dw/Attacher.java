package dw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dw.exception.DokuException;

class Attacher {
	private CoreClient _client;
	
	public Attacher(CoreClient client){
		_client = client;
	}
	
	private byte[] serializeFile(File f) throws IOException{
		byte[] b = new byte[(int)f.length()];
		FileInputStream fileInputStream = new FileInputStream(f);
		fileInputStream.read(b);
		return b;
	}
	
	public void putAttachment(String attachmentId, File attachment, boolean overwrite) throws IOException, DokuException{
		Map<String, Object> additionalParam = new HashMap<String, Object>();
		additionalParam.put("ow", overwrite);
		
		Object[] params = new Object[]{
				attachmentId,
				serializeFile(attachment),
				additionalParam
		};
		
		_client.genericQuery("wiki.putAttachment", params);
	}
}
