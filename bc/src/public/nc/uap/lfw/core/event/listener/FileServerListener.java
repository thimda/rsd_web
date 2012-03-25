package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.FileUploadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public class FileServerListener extends AbstractServerListener {

	public FileServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void onUpload(FileUploadEvent e) {};

}
