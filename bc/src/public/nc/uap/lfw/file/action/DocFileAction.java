package nc.uap.lfw.file.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.action.FileSystemAction;
import nc.uap.lfw.file.itf.ILfwFileQryService;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;

import org.apache.commons.io.IOUtils;

/**
 * Word�ĵ��ļ�����
 * @author licza
 *
 */
@Servlet(path = "/doc/file")
public class DocFileAction extends FileSystemAction{
	/**
	 * �����ļ�
	 * 
	 * @param id �ļ�����
	 * @throws IOException
	 */
	@Action
	public void down(@Param(name = "id") String id) throws IOException {
		OutputStream out = null;
		int BUFSIZE = 1024 * 1024;
		try {
			ILfwFileQryService fileQry = NCLocator.getInstance().lookup(ILfwFileQryService.class);
			LfwFileVO fvo = fileQry.getFile(id);
			out = response.getOutputStream();
			boolean b = getFileManager().existInFs(fvo.getPk_lfwfile());
			addFileName(fvo,true);
			//�ж��ļ�ϵͳ���Ƿ���ڴ��ļ� �����������ʹ�ÿհ׵�wordģ��
			if(b){
				getFileManager().download(id, out);
				out.flush();
			}else{
				InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("portal/tmp_word.doc");
				try {
					byte[] buf = new byte[BUFSIZE];
					int len = -1;
					while ((len = ins.read(buf)) != -1) {
						out.write(buf, 0, len);
						out.flush();
					}
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(),e);
				}finally{
					IOUtils.closeQuietly(ins);
				}
			}
			
		} catch (Exception e) {
			throw new LfwRuntimeException("�ļ�����ʧ��", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}
