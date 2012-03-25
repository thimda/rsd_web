package nc.uap.lfw.core.listener;

import java.util.Date;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;

/**
 * ��־�����ѯ���������ʼ����ִ�з���
 * 
 * @author guoweic
 *
 */
public class WatchLoggerStartListener extends MouseServerListener<ButtonComp> {

	public WatchLoggerStartListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}

	@Override
	public void onclick(MouseEvent<ButtonComp> e) {
		Date date = new Date();
		long startTime = date.getTime();
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("startTime", startTime);
	}

}
