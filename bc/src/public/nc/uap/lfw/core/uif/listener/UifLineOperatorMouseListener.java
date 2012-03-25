package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * �в�������
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
			throw new LfwRuntimeException("����Ƭ��ID:" + getWidgetId() + ",û���ҵ���Ӧ��������");
		}
		LfwWidget widget = ctx.getWidget();
		return (IBodyInfo) widget.getExtendAttributeValue(LfwWidget.BODYINFO);
	}
	
	protected abstract String getWidgetId();
}
