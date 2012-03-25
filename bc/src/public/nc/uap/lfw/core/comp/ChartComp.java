package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.comp.ctx.ChartContext;

public class ChartComp extends WebComponent{

	private static final long serialVersionUID = 4177302075991369709L;
	
	private ChartConfig config;

	private BaseChartModel chartModel;
	
	public ChartConfig getConfig() {
		return config;
	}
	
	public void setConfig(ChartConfig config) {
		this.config = config;
	}
	
	public BaseChartModel getChartModel() {
		return chartModel;
	}
	
	public void setChartModel(BaseChartModel chartModel) {
		this.chartModel = chartModel;
	}
	public void setContext(ChartContext context){
	}
	public ChartContext getContext() {
		ChartContext ctx = new ChartContext();
		ctx.setId(this.getId());
		return ctx;
	}
}
