package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;

/**
 * 容器型元素Tag
 * 
 * @author dengjt
 * 
 */
public class ContainerElementTag extends WebElementTag implements IContainerElementTag {
	private String id;
	private String varShowId = null;
//	private String showId = null;
	// 组件所属widget id
	private String widget = null;
	private LfwWidget currWidget = null;

	protected void doRender() throws JspException, IOException {
		LfwLogger.info("begin to render element:" + getId());

		JspWriter out = getJspContext().getOut();
		String headStr = generateHead();
		if (headStr != null)
			out.write(headStr);
		String script = generateHeadScript();
		addToBodyScript(script);

		JspFragment body = getJspBody();
		if (body != null)
			body.invoke(out);

		String tailStr = generateTail();
		if (tailStr != null)
			out.write(tailStr);
		script = generateTailScript();
		addToBodyScript(script);

		LfwLogger.info("successfully render element:" + getId());
	}
	
	protected String getVarShowId(){
		if(varShowId == null){
			String widgetId = getCurrWidget() == null ? "pagemeta" : currWidget.getId();
			varShowId = COMP_PRE + widgetId + "_" + getId();
		}
		return varShowId;
	}

//	protected String getShowId() {
//		if (showId == null) {
//			String widgetId = getCurrWidget() == null ? "pagemeta" : currWidget.getId();
//			showId = DIV_PRE + widgetId + "_" + getId();
//		}
//		return showId;
//	}

	protected LfwWidget getCurrWidget() {
		if (currWidget != null)
			return currWidget;
		if (getWidget() != null) {
			currWidget = getPageModel().getPageMeta().getWidget(getWidget());
		} else {
			LfwWidget[] widgets = getPageModel().getPageMeta().getWidgets();
			int doubleId = 0;
			LfwWidget ownerWidget = null;
			if (widgets != null && widgets.length > 0) {
				for (int i = 0; i < widgets.length; i++) {
					ViewComponents views = widgets[i].getViewComponents();
					ViewMenus contextMenus = widgets[i].getViewMenus();

					if (views.getComponent(this.id) != null) {
						doubleId++;
					}

					if (contextMenus.getContextMenu(this.id) != null)
						doubleId++;

					if (doubleId >= 2)
						throw new LfwRuntimeException("组件id和已有组件id重复,id=" + this.getId());
					else if (doubleId == 1 && ownerWidget == null)
						ownerWidget = widgets[i];
				}

//				if (doubleId == 0)
//					throw new LfwRuntimeException("组件id为" + this.getId()
//							+ "的组件不属于任何widget,配置错误!");
			}
			currWidget = ownerWidget;
		}
		return currWidget;
	}

	public String generateHead() {
		return null;
	}

	public String generateHeadScript() {
		return "";
	}

	public String generateTail() {
		return null;
	}

	public String generateTailScript() {
		return "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

}
