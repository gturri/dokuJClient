package dw.xmlrpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dw.xmlrpc.exception.DokuException;

//! @cond
class Attacher {
	private CoreClient _client;

	public Attacher(CoreClient client){
		_client = client;
	}

	byte[] serializeFile(File f) throws IOException{
		byte[] b = new byte[(int)f.length()];
		FileInputStream fileInputStream = new FileInputStream(f);
		fileInputStream.read(b);
		return b;
	}

	void deserializeFile(byte[] b, File f) throws IOException{
		FileOutputStream fileOutputStream = new FileOutputStream(f);
		fileOutputStream.write(b);
	}

	public void putAttachment(String fileId, byte[] file, boolean overwrite) throws IOException, DokuException{
		Map<String, Object> additionalParam = new HashMap<String, Object>();
		additionalParam.put("ow", overwrite);

		Object[] params = new Object[]{
				fileId,
				file,
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

			String id = (String) map.get("name");
			Date lastModified = (Date) map.get("lastModified");
			String author = (String) map.get("author");
			Integer version = (Integer) map.get("version");
			Integer perms = (Integer) map.get("perms");
			Object sizeObj = map.get("size");
			Integer size = null;
			if ( sizeObj instanceof Integer ){
				size = (Integer) sizeObj;
			}

			res.add(new MediaChange(id, lastModified, author, version, perms, size));
		}

		return res;
	}

	public List<AttachmentDetails> getAttachments(String namespace, Map<String, Object> additionalParams) throws DokuException{
		if ( additionalParams == null ){
			additionalParams = new HashMap<String, Object>();
		}
		Object[] params = new Object[]{namespace, additionalParams};
		Object result = _client.genericQuery("wiki.getAttachments", params);
		List<AttachmentDetails> res = new ArrayList<AttachmentDetails>();
		for(Object o : (Object[]) result){
			res.add(buildAttachmentDetailsFromResult(o));
		}

		return res;
	}

	@SuppressWarnings("unchecked")
	private AttachmentDetails buildAttachmentDetailsFromResult(Object o){
		return buildAttachmentDetailsFromResult((Map<String, Object>) o);
	}

	private AttachmentDetails buildAttachmentDetailsFromResult(Map<String, Object> m){
		String id = (String) m.get("id");
		Integer size = (Integer) m.get("size");
		Date lastModified = (Date) m.get("lastModified");
		Boolean isImg = (Boolean) m.get("isimg");
		Boolean writable = (Boolean) m.get("writable");
		Integer perms = (Integer) m.get("perms");
		return new AttachmentDetails(id, size, lastModified, isImg, writable, perms);
	}

	public AttachmentInfo getAttachmentInfo(String fileId) throws DokuException{
		Object result = _client.genericQuery("wiki.getAttachmentInfo", fileId);
		return buildAttachmentInfoFromResult(result, fileId);
	}

	@SuppressWarnings("unchecked")
	private AttachmentInfo buildAttachmentInfoFromResult(Object o, String fileId){
		return buildAttachmentInfoFromResult((Map<String, Object>) o, fileId);
	}

	private AttachmentInfo buildAttachmentInfoFromResult(Map<String, Object> m, String fileId){
		Integer size = (Integer) m.get("size");
		Date lastModified = (Date) m.get("lastModified");
		return new AttachmentInfo(fileId, size, lastModified);
	}

	public void deleteAttachment(String fileId) throws DokuException{
		_client.genericQuery("wiki.deleteAttachment", fileId);
	}

	public byte[] getAttachment(String fileId) throws DokuException{
		Object result = _client.genericQuery("wiki.getAttachment", fileId);
		return (byte[]) result;
	}
//! @endcond
}
