package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.SpliterEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class SpliterServerListener extends AbstractServerListener {

	public SpliterServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void resizeDiv1(SpliterEvent e){};
	public void resizeDiv2(SpliterEvent e){};
}
