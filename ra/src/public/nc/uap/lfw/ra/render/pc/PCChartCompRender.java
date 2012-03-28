package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.ChartConfig;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIChartComp;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 图标渲染器
 * @param <T>
 * @param <K>
 */
public class PCChartCompRender extends UINormalComponentRender<UIChartComp, ChartComp> {

	public PCChartCompRender(UIChartComp uiEle, ChartComp webEle, UIMeta uimeta, PageMeta pageMeta,
			UIRender<? extends UILayoutPanel, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	public String generateBodyHtml() {
//		StringBuffer buf = new StringBuffer();
//		WebComponent comp = this.getWebElement();
//		buf.append("<div id=\"").append(getDivId()).append("\" style=\"width:").append(comp.getWidth()).append(";height:").append(comp.getHeight())
//				.append(";top:").append(comp.getTop()).append("px;left:").append(comp.getLeft()).append("px;position:").append(comp.getPosition())
//				.append(";\">");
//		// buf.append(this.generalEditableHeadHtml());
//		// buf.append(this.generalEditableTailHtml());
//		buf.append("</div>\n");
//		return buf.toString();
		return super.generateBodyHtml();
	}

	@Override
	public String generateBodyHtmlDynamic() {
//
//		StringBuffer buf = new StringBuffer();
//		WebComponent comp = this.getWebElement();
//		buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
//		buf.append(getDivId()).append(".style.width = '").append(comp.getWidth()).append("px';\n");
//		buf.append(getDivId()).append(".style.height = '").append(comp.getHeight()).append("px';\n");
//		buf.append(getDivId()).append(".style.top = '").append(comp.getTop()).append("px';\n");
//		buf.append(getDivId()).append(".style.left = '").append(comp.getLeft()).append("px';\n");
//		buf.append(getDivId()).append(".style.position = '").append(comp.getPosition()).append("';\n");
//
//		return buf.toString();
		return super.generateBodyHtmlDynamic();
	}

	public String generateBodyScript() {

		StringBuffer buf = new StringBuffer();
		ChartComp chart = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		
		ChartConfig config = chart.getConfig();

		String chartId = getVarId();

		// ChartComp(parent, name, left, top, width, height, chartconfig,
		// position, className)

		buf.append(config.GenCreateScript(chartId));
		
		buf.append("var ").append(chartId).append(" = new ChartComp(document.getElementById('").append(getDivId()).append("'),'").append(
				chart.getId()).append("','0','0','100%','100%',config_").append(chartId);
		
		buf.append(",'relative','").append(uiComp.getClassName()).append("');\n");

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + chartId + ");\n");

		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var " + widget + " = pageUI.getWidget('" + this.getCurrWidget().getId() + "') \n");
		buf.append(chartId + ".widget=" + widget + ";\n");

		buf.append(chartId + ".setDataset(" + widget + ".getDataset('" + chart.getChartModel().getDataset() + "'));\n");
		if (chart.isVisible() == false)
			buf.append(chartId + ".hide();\n");
		/*
		String captionfunc = config.getCaptionFunction();
		if(captionfunc != null && !captionfunc.equals("")){
			buf.append(chartId + ".OpenTitle =" + captionfunc + ";\n");
		}*/
		return buf.toString();
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * nc.uap.lfw.ra.render.UINormalComponentRender#generateBodyScriptDynamic()
//	 * 动态脚本生成
//	 */
//	@Override
//	public String generateBodyScriptDynamic() {
//
//		StringBuffer buf = new StringBuffer();
//		ChartComp chart = this.getWebElement();
//		UIComponent uiComp = this.getUiElement();
//		ChartConfig config = chart.getConfig();
//		String widget = WIDGET_PRE + this.getCurrWidget().getId();
//		
//		String chartId = getVarId();
//		buf.append("var " + widget + " =pageUI.getWidget('" +  this.getCurrWidget().getId()+ "'); \n");
//				
//		buf.append(config.GenCreateScript(chartId));
//	
//		buf.append("var ").append(chartId).append(" = new ChartComp(document.getElementById('").append(getDivId()).append("'),'").append(
//			chart.getId()).append("','0','0','100%','100%',config_").append(chartId);
//	
//		buf.append(",'relative','").append(uiComp.getClassName()).append("');\n");
//	
//		
//		buf.append("var comp =  pageUI.getWidget('").append(this.getCurrWidget().getId()).
//			append("').getComponent(").append("'").append(chart.getId()).append("');\n"); 
//		buf.append(" if(comp) \n");
//		buf.append(widget).append(".getDataset('").append(chart.getChartModel().getDataset()).append("').unbindComponent(comp);\n");
//
//		
//		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + chartId + ");\n");
//
//		
//		buf.append(chartId + ".widget=" + widget + ";\n");
//
//		buf.append(chartId + ".setDataset(" + widget + ".getDataset('" + chart.getChartModel().getDataset() + "'));\n");
//		if (chart.isVisible() == false)
//			buf.append(chartId + ".hide();\n");
//		return buf.toString();
//	}

	protected String getSourceType(IEventSupport ele) {

		return LfwPageContext.SOURCE_TYPE_CHART;
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta,Object obj) {
		super.notifyDestroy(uiMeta, pageMeta,obj);
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyUpdate(uiMeta, pageMeta, obj);
	}
	
}


