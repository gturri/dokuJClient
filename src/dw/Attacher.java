package dw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	
	private void deserializeFile(byte[] b, File f) throws IOException{
		FileOutputStream fileOutputStream = new FileOutputStream(f);
		fileOutputStream.write(b);
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
	
	public List<MediaChange> getRecentMediaChanges(Integer timestamp) throws DokuException{
		Object result = _client.genericQuery("wiki.getRecentMediaChanges", timestamp);
		List<MediaChange> res = new ArrayList<MediaChange>();
		
		for(Object o : (Object[]) result){
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			String id = (String) map.get("id");
			Date lastModified = (Date) map.get("lastModified");
			String author = (String) map.get("author");
			Integer version = (Integer) map.get("version");
			Integer perms = (Integer) map.get("perms");
			Object sizeObj = map.get("size");
			Integer size = null;
			if ( sizeObj instanceof Integer ){
				size = (Integer) size;
			}
			
			res.add(new MediaChange(id, lastModified, author, version, perms, size));
		}
		
		return res;
	}
	
	public List<AttachmentInfo> getAttachments(String namespace, Map<String, Object> additionalParams) throws DokuException{
		Object[] params = new Object[]{namespace, additionalParams};
		Object result = _client.genericQuery("wiki.getAttachments", params);
		List<AttachmentInfo> res = new ArrayList<AttachmentInfo>();
		for(Object o : (Object[]) result){
			res.add(buildAttachmentInfoFromResult(o));
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private AttachmentInfo buildAttachmentInfoFromResult(Object o){
		return buildAttachmentInfoFromResult((Map<String, Object>) o);
	}
	
	private AttachmentInfo buildAttachmentInfoFromResult(Map<String, Object> m){
		String id = (String) m.get("id");
		Integer size = (Integer) m.get("size");
		Date lastModified = (Date) m.get("lastModified");
		Boolean isImg = (Boolean) m.get("isimg");
		Boolean writable = (Boolean) m.get("writable");
		Integer perms = (Integer) m.get("perms");
		return new AttachmentInfo(id, size, lastModified, isImg, writable, perms);
	}
	
	public AttachmentInfo getAttachmentInfo(String fileId) throws DokuException{
		Object result = _client.genericQuery("wiki.getAttachmentInfo", fileId);
		return buildAttachmentInfoFromResult(result);
	}
	
	public void deleteAttachment(String fileId) throws DokuException{
		_client.genericQuery("wiki.deleteAttachment", fileId);
	}
	
	public File getAttachment(String fileId, String localPath) throws DokuException, IOException{
		Object result = _client.genericQuery("wiki.getAttachment", fileId);
		byte[] b = (byte[]) result;
		
		File f = new File(localPath);
		deserializeFile(b, f);
		return f;
	}
}
