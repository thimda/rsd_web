package nc.uap.lfw.core.comp;

import java.util.HashMap;
import java.util.Set;

/**
 * ÈýÎ¬¸´ºÏÍ¼
 * @author lisw
 *
 */
public class ChartConfigMSCombi3D extends ChartConfig {
	public static final String MSCombi_RenderAs_Line = "LINE";
	public static final String MSCombi_RenderAs_Area = "AREA";
	public static final String MSCombi_RenderAs_Column = "COLUMN";
	public ChartConfigMSCombi3D(){
		super();
		this.setShowType(ChartConfig.ShowType_MSCombi_3D);
		this.setSeriesType(ChartConfig.Series_Mutil_Type);
		setRenderType(MSCombi_RenderAs_Line);
	}
	
	private String renderType;
	private HashMap<String,String> SeriesRenderlist;
	public String getRenderType() {
		return renderType;
	}
	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}
	public void putRenderType(String SeriesName,String renderType){
		if(SeriesRenderlist == null)
			SeriesRenderlist = new HashMap<String,String>();		
		SeriesRenderlist.put(SeriesName, renderType);
	}
	
	public String GenCreateScript(String chartId){
		StringBuffer buf = new StringBuffer();
		buf.append(super.GenCreateScript(chartId));
		buf.append(" config_").append(chartId).append(".SetRender('")
			.append(getRenderType()).append("');\n");
		
		if(SeriesRenderlist != null){
			Set<String> keys = SeriesRenderlist.keySet();
			for(String key : keys){				
				buf.append(" config_").append(chartId).append(".addSeriesRender('")
					.append(key).append("','").append(SeriesRenderlist.get(key))
					.append("'); \n");
			}
		}
		return buf.toString();
	}
}
