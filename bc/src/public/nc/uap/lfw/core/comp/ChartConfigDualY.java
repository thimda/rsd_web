package nc.uap.lfw.core.comp;

import java.util.HashMap;
import java.util.Set;

public class ChartConfigDualY extends ChartConfigMSCombi3D {
	/**
	 * primary YAxis
	 */
	public static final String DualY_ParentYAxis_P = "P";
	
	/**
	 * secondary YAxis
	 */
	public static final String DualY_ParentYAxis_S = "S";
	
	public ChartConfigDualY(){
		super();
		this.setSeriesType(Series_DualY_Type);
		this.setShowType(ShowType_MSCombiDY_3D);
	}
	private String sYAxisName;
	
	public String getsYAxisName() {
		return sYAxisName;
	}
	public void setsYAxisName(String sYAxisName) {
		this.sYAxisName = sYAxisName;
	}
	
	public String GenCreateScript(String chartId){
		StringBuffer buf = new StringBuffer();
		buf.append(super.GenCreateScript(chartId));
		buf.append(" config_").append(chartId).append(".setSYAxisName('")
			.append(getsYAxisName()).append("');\n");

		if(SeriesparentYAxisList != null){
			Set<String> keys = SeriesparentYAxisList.keySet();
			for(String key : keys){				
				buf.append(" config_").append(chartId).append(".addSeriesParentYAxis('")
					.append(key).append("','").append(SeriesparentYAxisList.get(key))
					.append("'); \n");
			}
		}
		return buf.toString();
	}
	
	private HashMap<String,String> SeriesparentYAxisList;
	
	public void putparentYAxis(String SeriesName,String parentYAxis){
		if(SeriesparentYAxisList == null)
			SeriesparentYAxisList = new HashMap<String,String>();		
		SeriesparentYAxisList.put(SeriesName, parentYAxis);
	}
}
