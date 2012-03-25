package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.comp.BaseChartModel;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.ChartConfig;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChartSerializer extends BaseSerializer<ChartComp> implements IViewZone{

	@Override
	public void deSerialize(ChartComp chart, Document doc, Element parentNode) {
		Element chartNode = doc.createElement("ChartComp");
		parentNode.appendChild(chartNode);
		chartNode.setAttribute("id", chart.getId());
//		if (isNotNullString(chart.getWidth()))
//			chartNode.setAttribute("width", chart.getWidth());
//		if (isNotNullString(chart.getHeight()))
//			chartNode.setAttribute("height", chart.getHeight());
//		if (isNotNullString(chart.getTop()))
//			chartNode.setAttribute("top", chart.getTop());
//		if (isNotNullString(chart.getLeft()))
//			chartNode.setAttribute("left", chart.getLeft());
//		if (isNotNullString(chart.getPosition()))
//			chartNode.setAttribute("position", chart.getPosition());
//		if (isNotNullString(chart.getClassName()))
//			chartNode.setAttribute("className", chart.getClassName());
		BaseChartModel chartModel = chart.getChartModel();
		if (chartModel != null) {
			Element modelNode = null;
			modelNode = doc.createElement("ChartModel");
			chartNode.appendChild(modelNode);
			modelNode.setAttribute("dataset", (chartModel.getDataset()));
		}
		ChartConfig config = chart.getConfig();
		if (config != null) {
			Element configNode = null;
			configNode = doc.createElement("ChartConfig");
			chartNode.appendChild(configNode);
			configNode.setAttribute("showType", (config.getShowType()));
			configNode.setAttribute("seriesType", (config.getSeriesType()));
			configNode.setAttribute("caption", (config.getCaption()));
			configNode.setAttribute("numberPrefix", (config.getNumberPrefix()));
			configNode.setAttribute("groupColumn", (config.getGroupColumn()));
			configNode.setAttribute("groupName", (config.getGroupName()));
			configNode.setAttribute("seriesColumns", (config.getSeriesColumns()));
			configNode.setAttribute("seriesNames", (config.getSeriesNames()));
			configNode.setAttribute("xAxisName", (config.getXAxisName()));
			configNode.setAttribute("yAxisName", (config.getYAxisName()));
		}
	}


	@Override
	public void serialize(Digester digester) {
		String chartClassName = ChartComp.class.getName();
		digester.addObjectCreate("Widget/Components/ChartComp", chartClassName);
		digester.addSetProperties("Widget/Components/ChartComp");
		digester.addSetNext("Widget/Components/ChartComp", "addComponent",
				chartClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ChartComp/Listeners", ChartComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ChartComp", ChartComp.class);
		
		String chartConfigClassName = ChartConfig.class.getName();
		String configBasePath = "Widget/Components/ChartComp/ChartConfig";
		digester.addObjectCreate(configBasePath, chartConfigClassName);
		digester.addSetProperties(configBasePath);
		digester.addSetNext(configBasePath, "setConfig", chartConfigClassName);
		
		String chartModelClassName = BaseChartModel.class.getName();
		String modelbasePath = "Widget/Components/ChartComp/ChartModel";
		digester.addObjectCreate(modelbasePath, chartModelClassName);
		digester.addSetProperties(modelbasePath);
		digester.addSetNext(modelbasePath, "setConfig", chartModelClassName);
	}

}
