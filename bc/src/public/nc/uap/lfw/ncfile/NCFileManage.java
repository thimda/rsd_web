package nc.uap.lfw.ncfile;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.itf.ILfwFileQryService;
import nc.uap.lfw.file.itf.ILfwFileService;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.ArrayUtils;

import sun.misc.BASE64Decoder;

public class NCFileManage extends FileManager{

	@Override
	public void download(String fileNo, OutputStream out) throws Exception {
		// TODO Auto-generated method stub
		String dsName = LfwRuntimeEnvironment.getDatasource();
		download(dsName, fileNo, out);
	}


	@Override
	public String upload(String fileName, String billType, String billItem,
			long size, InputStream in, boolean override) throws Exception {
		// TODO Auto-generated method stub
		//return super.upload(fileName, billType, billItem, size, in, override);
		String dsName = LfwRuntimeEnvironment.getDatasource();
		String creator = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		NCFileNode fileNode = upload(dsName, billItem, fileName, in, creator, size);
		ILfwFileService fileService = NCLocator.getInstance().lookup(ILfwFileService.class);
		ILfwFileQryService fileQry = NCLocator.getInstance().lookup(ILfwFileQryService.class);
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		String pk_user = null;
		if(ses != null)
			pk_user = ses.getPk_user();
		LfwFileVO vo = new LfwFileVO();
		vo.setFilename(fileName);
		vo.setPk_billitem(billItem);
		vo.setPk_billtype(billType);
		vo.setCreator(pk_user);
		vo.setCreattime(new UFDateTime());
		vo.setLastmodifyer(pk_user);
		vo.setLastmodifytime(new UFDateTime());
		if (override) {
		LfwFileVO[] fvo = fileQry.getFile(billType, billItem);
		if (!ArrayUtils.isEmpty(fvo)) {
			vo = fvo[0];
			vo.setFilename(fileName);
			vo.setFilesize(size);
			fileService.updataVos(new LfwFileVO[]{vo});
			return vo.getPk_lfwfile();
		}
		}
		vo.setFilesize(size);
		vo.setFiletypo(getFileType(fileName));
		fileService.add(vo);
			
		return billItem + "/" + fileNode.getFullPath();
		
		
	}

	public ILfwFileQryService getFileQryService(){
		return NCLocator.getInstance().lookup(INCLfwFileQryService.class);
	}
	
	public static void download(String dsName, String path, OutputStream out)throws Exception {
		byte[] bytes = new BASE64Decoder().decodeBuffer(path);
		path = new String(bytes,"gbk");
		ObjectOutputStream oos = null;
		InputStream in = null;
		String serverIp = LfwRuntimeEnvironment.getServerAddr();
		if (serverIp == null || serverIp.equals("")) {
			serverIp = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerName();
			int port = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerPort();
			serverIp = serverIp + ":" + port;
		}
		String address = "http://" + serverIp + "/";
		StringBuilder sbUrl = new StringBuilder(address)
				.append("service/FileManageServlet");
		
		try {
			URL url = new URL(sbUrl.toString());
			URLConnection conn = url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("content-type",
					"application/x-java-serialized-object,charset=utf-8");
			oos = new ObjectOutputStream(conn.getOutputStream());
			HashMap<String, String> headInfo = new HashMap<String, String>();
			headInfo.put("dsName", dsName);
			headInfo.put("operType", "download");
			headInfo.put("path", path);
			oos.writeObject(headInfo);
			oos.flush();
			in = conn.getInputStream();
			byte[] buf = new byte[2048];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				out.flush();
			}
		
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
}



	public static NCFileNode upload(String dsName, String pPath,
			String fileName, InputStream input, String creator, long fileLength)
			throws Exception {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		NCFileNode newNode = null;
		String serverIp = LfwRuntimeEnvironment.getServerAddr();
		if (serverIp == null || serverIp.equals("")) {
			serverIp = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerName();
			int port = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerPort();
			serverIp = serverIp + ":" + port;
		}
		String address = "http://" + serverIp + "/";
		StringBuilder sbUrl = new StringBuilder(address)
				.append("service/FileManageServlet");
		
		try {
			URL url = new URL(sbUrl.toString());
			URLConnection conn = url.openConnection();
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).setChunkedStreamingMode(2048);
			}
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("content-type",
					"application/x-java-serialized-object,charset=utf-8");
			byte[] buf = new byte[2048];
			int len = -1;
			oos = new ObjectOutputStream(conn.getOutputStream());
			HashMap<String, String> headInfo = new HashMap<String, String>();
			headInfo.put("dsName", dsName);
			headInfo.put("operType", "upload");
			headInfo.put("parentPath", pPath);
			headInfo.put("fileName", fileName);
			headInfo.put("creator", creator);
			headInfo.put("fileLength", fileLength + "");
			oos.writeObject(headInfo);
			while ((len = input.read(buf)) != -1) {
				oos.write(buf, 0, len);
			}
			oos.flush();
			oos.close();
			ois = new ObjectInputStream(conn.getInputStream());
			Object obj = ois.readObject();
			if (obj instanceof Exception) {
				throw (Exception) obj;
			}
			newNode = (NCFileNode) obj;
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (Exception e2) {
			}
		}
		return newNode;
	}
	
	public void delete(String nodePath) throws Exception {
		IFileSystemService fileSysService = NCLocator.getInstance().lookup(IFileSystemService.class);
		fileSysService.deleteNCFileNode(nodePath);
	}


}
