package nc.uap.lfw.file.listener;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.AbstractServerListener;
import nc.uap.lfw.file.event.UploadEvent;

/**
 * �ļ��ϴ����Listener
 * @author licza
 *
 */
public abstract class UploadCompleteServerListener extends AbstractServerListener{

	public UploadCompleteServerListener(LfwPageContext pagemeta,String widgetId) {
		super(pagemeta, widgetId);
	}
 	
	public abstract void onComplete(UploadEvent event);
}
