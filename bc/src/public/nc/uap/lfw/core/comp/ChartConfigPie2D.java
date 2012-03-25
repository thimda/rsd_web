package nc.uap.lfw.core.comp;

import java.util.Set;

/**
 * 2άչ�ֱ�ͼ 
 * @author lisw
 *
 */
public class ChartConfigPie2D extends ChartConfig {
	public ChartConfigPie2D(){
		super();
		this.setShowType(ChartConfig.ShowType_Pie_2D);
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
