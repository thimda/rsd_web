package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.ChartConfig;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * Í¼±í¿Ø¼þ±êÇ©
 * @author guoweic
 */
public class ChartCompTag extends NormalComponentTag {

	public String generateBody() {
//		StringBuffer buf = new StringBuffer();
//		WebComponent comp = getComponent();
//		buf.append("<div id=\"")
//		   .append(DIV_PRE)
//		   .append(getId())
//		   .append("\" style=\"width:")
//		   .append(comp.getWidth())
//		   .append(";height:") 
//		   .append(comp.getHeight())
//		   .append(";top:")
//		   .append(comp.getTop())
//		   .append("px;left:")
//		   .append(comp.getLeft())
//		   .append("px;position:")
//		   .append(comp.getPosition())
//		   .append(";\">")
//		   .append("</div>");
//		return buf.toString();
		return super.generateBody();
	}

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		ChartComp chart = (ChartComp) getComponent();
		ChartConfig config = chart.getConfig();
		
		String chartId = getVarShowId();
		
//		ChartComp(parent, name, left, top, width, height, chartconfig, position, className)
		
		buf.append("var ")
		   .append(chartId)
		   .append(" = new ChartComp(document.getElementById('")
		   .append(DIV_PRE)
		   .append(getId())
		   .append("'),'")
		   .append(chart.getId())
		   .append("','0','0','100%','100%',")
		   .append("new ChartConfig(");
		
		if (config.getShowType() != null)
			buf.append("'")
			   .append(config.getShowType())
			   .append("'");
		else
			buf.append("null");
		if (config.getSeriesType() != null)
			buf.append("'")
			   .append(config.getSeriesType())
			   .append("'");
		else
			buf.append("null");
		if (config.getCaption() != null)
			buf.append("'")
			   .append(config.getCaption())
			   .append("'");
		else
			buf.append("null");
		if (config.getNumberPrefix() != null)
			buf.append("'")
			   .append(config.getNumberPrefix())
			   .append("'");
		else
			buf.append("null");
		if (config.getGroupColumn() != null)
			buf.append("'")
			   .append(config.getGroupColumn())
			   .append("'");
		else
			buf.append("null");
		if (config.getGroupName() != null)
			buf.append("'")
			   .append(config.getGroupName())
			   .append("'");
		else
			buf.append("null");
		if (config.getSeriesColumns() != null)
			buf.append("'")
			   .append(config.getSeriesColumns())
			   .append("'");
		else
			buf.append("null");
		if (config.getSeriesNames() != null)
			buf.append("'")
			   .append(config.getSeriesNames())
			   .append("'");
		else
			buf.append("null");
		if (config.getXAxisName() != null)
			buf.append("'")
			   .append(config.getXAxisName())
			   .append("'");
		else
			buf.append("null");
		if (config.getYAxisName() != null)
			buf.append("'")
			   .append(config.getYAxisName())
			   .append("'");
		else
			buf.append("null");
		   
		buf.append("),'")
		   .append("relative") //chart.getPosition()
		   .append("','")
		   .append("")
		   .append("');\n");
		
		
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + chartId + ");\n");
		
		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append(chartId + ".widget=" + widget + ";\n");
		
		buf.append(chartId + ".setDataSet(" + widget + ".getDataset('" + chart.getChartModel().getDataset() + "'));\n");
		if (chart.isVisible() == false)
			buf.append(chartId + ".hide();\n");
		
		
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_CHART;
	}


}
