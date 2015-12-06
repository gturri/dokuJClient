package dw.xmlrpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;

import dw.xmlrpc.exception.DokuException;

//! @cond
class Attacher {
	private final CoreClient _client;

	public Attacher(CoreClient client){
		_client = client;
	}

	byte[] serializeFile(File f) throws IOException{
		byte[] b = new byte[(int)f.length()];
		FileInputStream fileInputStream = new FileInputStream(f);
		fileInputStream.read(b);
		fileInputStream.close();
		return b;
	}

	void deserializeFile(byte[] b, File f) throws IOException{
		FileOutputStream fileOutputStream = new FileOutputStream(f);
		fileOutputStream.write(b);
		fileOutputStream.close();
	}

	public void putAttachment(String fileId, byte[] file, boolean overwrite) throws DokuException{
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
		String file = (String) m.get("file");
		Integer mtime = (Integer) m.get("mtime");
		Integer perms = (Integer) m.get("perm");
		if ( perms == null ){
			//Because it has been renamed in API v8
			perms = (Integer) m.get("perms");
		}
		return new AttachmentDetails(id, size, lastModified, isImg, writable, perms, file, mtime);
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
		Date lastModified = null;
		try {
			lastModified = (Date) m.get("lastModified");
		} catch (ClassCastException e){
			//for DW up to 2012-01-25b: when the file doesn't exist,
			//"lastModified" is int 0
			lastModified = defaultDate();
		}
		return new AttachmentInfo(fileId, size, lastModified);
	}

	private Date defaultDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(1970, 0, 0);
		return calendar.getTime();
	}

	public void deleteAttachment(String fileId) throws DokuException{
		_client.genericQuery("wiki.deleteAttachment", fileId);
	}

	public byte[] getAttachment(String fileId) throws DokuException{
		Object result = _client.genericQuery("wiki.getAttachment", fileId);
		try {
			return (byte[]) result;
		} catch (ClassCastException e){
			//for DW up to 2012-01-25b
			String base64Encoded = (String) result;
			return DatatypeConverter.parseBase64Binary(base64Encoded);
		}
	}
//! @endcond
}
