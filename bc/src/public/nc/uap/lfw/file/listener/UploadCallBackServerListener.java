package nc.uap.lfw.file.listener;

import java.lang.reflect.Constructor;

import org.apache.commons.lang.StringUtils;

import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.ScriptServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.event.UploadEvent;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.lang.UFDateTime;

/**
 * 上传完毕服务器端回调类
 * @author licza
 *
 */
public class UploadCallBackServerListener extends ScriptServerListener {

	public UploadCallBackServerListener(LfwPageContext pageCtx, String widgetId) {
		super(pageCtx, widgetId);
	}

	@Override
	public void handlerEvent(ScriptEvent event) {
		LfwPageContext ctx = getGlobalContext();
		String upload_listener_class = ctx.getParameter("upload_listener_class");
		String fileName = ctx.getParameter("fileName");
		String size = ctx.getParameter("size");
		String pk_file = ctx.getParameter("pk_file");
		String fileType = ctx.getParameter("fileType");
		String createtime = ctx.getParameter("createtime");
		String pk_user = ctx.getParameter("pk_user");
		String user_name = ctx.getParameter("user_name");
		String parentWidget = ctx.getParameter("parentWidget");
		String billitem = ctx.getParameter("billitem");
		
		LfwFileVO file = new LfwFileVO();
		file.setPk_lfwfile(pk_file);
		file.setFilename(fileName);
		file.setFilesize(Long.parseLong(StringUtils.defaultIfEmpty(size, "0")));
		file.setFiletypo(fileType);
		file.setCreattime(new UFDateTime(createtime));
		file.setPk_billitem(billitem);
		file.setCreator(pk_user);
		try {
			Class<?> cls = LfwClassUtil.forName(upload_listener_class);
			Constructor<?>  constructor = cls.getConstructor(LfwPageContext.class,String.class);
			UploadCompleteServerListener csl = (UploadCompleteServerListener)constructor.newInstance(new Object[]{getGlobalContext().getParentGlobalContext(),parentWidget});
			UploadEvent ue = new UploadEvent(file, user_name);
			csl.onComplete(ue);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

}
