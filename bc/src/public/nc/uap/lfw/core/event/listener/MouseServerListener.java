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
		throw new LfwRuntimeException("onclick 方法没有被重写");
	};
	
	public void onDbClick(MouseEvent<T> e){
		throw new LfwRuntimeException("onDbClick 方法没有被重写");
	};
	
	public void onMouseOver(MouseEvent<T> e){
		throw new LfwRuntimeException("onMouseOver 方法没有被重写");
	};
	
	public void onMouseOut(MouseEvent<T> e){
		throw new LfwRuntimeException("onMouseOut 方法没有被重写");
	};
}
