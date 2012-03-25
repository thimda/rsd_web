package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public abstract class MouseServerListener<T extends WebElement> extends AbstractServerListener {
	public MouseServerListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}
	
	public void onclick(MouseEvent<T> e){
		throw new LfwRuntimeException("onclick ����û�б���д");
	};
	
	public void onDbClick(MouseEvent<T> e){
		throw new LfwRuntimeException("onDbClick ����û�б���д");
	};
	
	public void onMouseOver(MouseEvent<T> e){
		throw new LfwRuntimeException("onMouseOver ����û�б���д");
	};
	
	public void onMouseOut(MouseEvent<T> e){
		throw new LfwRuntimeException("onMouseOut ����û�б���д");
	};
}
