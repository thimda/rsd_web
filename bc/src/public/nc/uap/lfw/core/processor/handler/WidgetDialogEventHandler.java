package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.WidgetEvent;
import nc.uap.lfw.core.event.conf.DialogListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.PageListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * widgetDialog事件处理Handler
 * @author zhangxya
 *
 */
public class WidgetDialogEventHandler extends AbstractEventHandler {

	@Override
	protected AbstractServerEvent getServerEvent(String eventName,
			WebElement source) {
		JsListenerConf listener = getListenerConf();
		LfwWidget widget = getPageCtx().getPageMeta().getWidget(getWidgetId());
		if(listener instanceof DialogListener){
			DialogEvent dialogEvent = new DialogEvent(widget);
			return dialogEvent;
		}
		else
			return super.getServerEvent(eventName, widget);
	}

	public WidgetDialogEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected WebElement getSource() {
		LfwWidget widget =  getPageCtx().getPageMeta().getWidget(getWidgetId());
		return widget;
	}

}
