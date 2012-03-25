package nc.uap.lfw.core.comp;

public class ChartConfigPie3D extends ChartConfig {
	public ChartConfigPie3D(){
		super();
		this.setShowType(ChartConfig.ShowType_Pie_3D);
		this.setSeriesType(ChartConfig.Series_Single_Type);
	}
	public double getRscale() {
		return rscale;
	}
	public void setRscale(double rscale) {
		this.rscale = rscale;
	}
	private double rscale = 0;
	

	public String GenCreateScript(String chartId){
		StringBuffer buf = new StringBuffer();
		buf.append(super.GenCreateScript(chartId));
		buf.append(" config_").append(chartId).append(".setRScale(")
			.append(getRscale()).append(");\n");

		return buf.toString();
	}
}
