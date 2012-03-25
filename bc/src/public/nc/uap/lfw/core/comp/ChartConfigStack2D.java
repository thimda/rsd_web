package nc.uap.lfw.core.comp;
/**
 * 单序列堆积二维图
 * @author lisw
 *
 */
public class ChartConfigStack2D extends ChartConfig {
	public ChartConfigStack2D(){
		super();
		this.setShowType(ChartConfig.ShowType_Stacked_2D);
		this.setSeriesType(ChartConfig.Series_Mutil_Type);
	}
}
