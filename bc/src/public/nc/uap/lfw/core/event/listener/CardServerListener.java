package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.CardEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class CardServerListener extends AbstractServerListener {

	public CardServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void beforePageInit(CardEvent e){};

	public void afterPageInit(CardEvent e){};
	
	public void beforePageChange(CardEvent e){};

}
