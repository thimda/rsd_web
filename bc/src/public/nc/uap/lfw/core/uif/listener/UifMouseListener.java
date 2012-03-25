package nc.uap.lfw.core.uif.listener;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;

/**
 * 单据所有菜单按钮的鼠标事件基类
 * @author gd 2010-3-26
 * @version NC6.0
 * @since NC6.0
 *
 */
public abstract class UifMouseListener<T extends WebElement> extends MouseServerListener<T> {

	public UifMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
}
