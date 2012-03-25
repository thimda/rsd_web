package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * 行操作基类
 *
 * @param <T>
 */
public abstract class UifLineOperatorMouseListener<T extends WebElement> extends UifMouseListener<T> {

	public UifLineOperatorMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public IBodyInfo getBodyInfo(){
		WidgetContext ctx = getGlobalContext().getWidgetContext(getWidgetId());
		if(ctx == null){
			throw new LfwRuntimeException("根据片段ID:" + getWidgetId() + ",没有找到对应的上下文");
		}
		LfwWidget widget = ctx.getWidget();
		return (IBodyInfo) widget.getExtendAttributeValue(LfwWidget.BODYINFO);
	}
	
	protected abstract String getWidgetId();
}
