package dw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
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
	
	public void putAttachment(String fileId, File file, boolean overwrite) throws IOException, DokuException{
		Map<String, Object> additionalParam = new HashMap<String, Object>();
		additionalParam.put("ow", overwrite);
		
		Object[] params = new Object[]{
				fileId,
				serializeFile(file),
				additionalParam
		};
		
		_client.genericQuery("wiki.putAttachment", params);
	}
	
	public AttachmentInfo getAttachmentInfo(String fileId) throws DokuException{
		Object result = _client.genericQuery("wiki.getAttachmentInfo", fileId);
		@SuppressWarnings("unchecked")
		Map<String, Object> mapRes = (Map<String, Object>) result;
		Integer size = (Integer) mapRes.get("size");
		Date lastModified  = (Date) mapRes.get("lastModified");
		return new AttachmentInfo(size, lastModified);
	}
	
	public void deleteAttachment(String fileId) throws DokuException{
		_client.genericQuery("wiki.deleteAttachment", fileId);
	}
}
