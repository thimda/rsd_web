package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.FocusEvent;
import nc.uap.lfw.core.event.KeyEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.RefTextEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.ReferenceTextListener;
import nc.uap.lfw.core.event.conf.TextListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.IServerListener;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class TextEventHandler extends AbstractEventHandler<TextComp> {

	public TextEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}
	
	protected TextComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		TextComp text = (TextComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(sourceId);
		return text;
	}

	protected AbstractServerEvent<TextComp> getServerEvent(String eventName, TextComp text) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof MouseListener) {
			AbstractServerEvent<TextComp> event = new MouseEvent<TextComp>(text);
			return event;
		} 
		else if (listener instanceof KeyListener) {
			KeyEvent event = new KeyEvent(text);
			return event;
		} 
		else if (listener instanceof TextListener) {
			AbstractServerEvent<TextComp> event = new TextEvent(text);
			return event;
		} 
		else if (listener instanceof FocusListener) {
			AbstractServerEvent<TextComp> event = new FocusEvent<TextComp>(text);
			return event;
		} 
		else if(listener instanceof ReferenceTextListener){
			AbstractServerEvent<TextComp> event = new RefTextEvent(text);
			return event;
		}
		else
			return super.getServerEvent(eventName, text);
	}

}
