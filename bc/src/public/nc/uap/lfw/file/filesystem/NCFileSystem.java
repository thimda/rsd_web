package nc.uap.lfw.file.filesystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.document.pub.itf.INCFileSystem;
import nc.document.pub.util.DocumentCenter;
import nc.document.pub.vo.NCFileVO;
import nc.jdbc.framework.SQLParameter;
import nc.login.vo.ISystemIDConstants;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.file.vo.LfwFileVO;

/**
 * NC文档中心文件系统实现
 * @author licza
 *
 */
public class NCFileSystem implements ILfwFileSystem {
private static String VIRTUAL = "virtual";
	public void deleteFile(Serializable fileVO) throws Exception {
		DocumentCenter.deleteFile(ISystemIDConstants.NCPORTAL, ((String)fileVO));
	}
 

	public Object download(Serializable fileVO, OutputStream out) throws Exception {
		DocumentCenter.downloadByFilePK((String)fileVO, out);
		return null;
	}

	public boolean existInFs(String fileNo) throws Exception {
		NCFileVO[] vos = NCLocator.getInstance().lookup(INCFileSystem.class).queryNCFileVOs("pk_file = '"+fileNo+"'");
		if(vos != null && vos.length > 0)
			return StringUtils.equals(vos[0].getDef3(), VIRTUAL);
		return false;
	}

	public void upload(Serializable fileVO, InputStream input) throws Exception {
		LfwFileVO vo = ((LfwFileVO) fileVO);
		DocumentCenter.uploadFile(vo.getPk_lfwfile(), null, vo.getFilename(), input, ISystemIDConstants.NCPORTAL);
	}


	public void signVirtualFile(String fileNo) throws Exception {
		SQLParameter param = new SQLParameter();
		param.addParam(fileNo);
		CRUDHelper.getCRUDService().executeUpdate("update " + (new NCFileVO()).getTableName() + "set def3 = '"+VIRTUAL+"' where pk_file = ?" ,param);
	}
}
