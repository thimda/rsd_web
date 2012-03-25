package nc.uap.lfw.ra.render.pc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class PcFormRenderUtil {

	private static ThreadLocal<PcFormRenderUtil> threadLocal = new ThreadLocal<PcFormRenderUtil>() {
		protected PcFormRenderUtil initialValue() {
			return new PcFormRenderUtil();
		}
	};
	private Map<String,Map<String,String>> formDs = new HashMap<String, Map<String,String>>();
	
	

	public Map<String, Map<String, String>> getFormDs() {
		return formDs;
	}

	public void setFormDs(Map<String, Map<String, String>> formDs) {
		this.formDs = formDs;
	}

	/**
	 * 为form设置dataset的脚本
	 * 
	 * @param varFormId
	 * @param widget
	 * @param dsId
	 */
	public static void setFromDsScript(String formId, String varFormId, String widget, String dsId) {
		String dsKey = varFormId+"-"+dsId;
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();
		if (!formDs.containsKey(widget)){
			formDs.put(widget, new HashMap<String,String>());
		}
		Map<String,String> map2 = formDs.get(widget);	
		if(map2.containsKey(dsKey)){
			return;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(varFormId).append(" = pageUI.getWidget('").append(widget).append("').getComponent('").append(formId).append("');\n");
		buf.append("if (").append(varFormId).append(" != null && ").append(varFormId).append(".dataset == null){\n");
		
		buf.append(varFormId).append(".setDataset(");
		buf.append("pageUI.getWidget(\"" + widget + "\").getDataset" + "(\"").append(dsId);
		buf.append("\"));\n");
		
		buf.append("}\n");
		map2.put(dsKey, buf.toString());

	}

	public static String wrapWithSetTimeout(String str, int time) {
		StringBuffer buf = new StringBuffer();
		buf.append("\nsetTimeout('");
		buf.append(str);
		buf.append("'," + time + ");\n");
		return buf.toString();
	}

	/**
	 * 获得form的dataSet的脚本
	 * 
	 * @param formId
	 * @param widget
	 * @param dsId
	 * @return
	 */
	public static String getFormDsScript(String formId, String widget, String dsId) {
		String key1 = widget;
		String key2 = formId+"-"+dsId;
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();
		return formDs.get(key1).get(key2);
	}

	public static void removeFormDsScript(String formId, String widget, String dsId) {
		String key1 = widget;
		String key2 = formId+"-"+dsId;
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();		
		formDs.get(key1).remove(key2);
	}
	public static void removeFormDsScript(String widget) {
		String key1 = widget;
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();		
		formDs.remove(key1);
	}

	public static void cleanAllFormDsScript() {
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();
		formDs.clear();
	}

	public static String getAllFormDsScript() {
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();
		Iterator<Map<String,String>> values = formDs.values().iterator();
		StringBuffer buf = new StringBuffer();
		while (values.hasNext()) {
			Iterator<String> v2 =values.next().values().iterator();
			buf.append(v2.next());
		}
		return buf.toString();
	}
	
	public static String getAllFormDsScript(String widget) {
		Map<String,Map<String,String>> formDs = threadLocal.get().getFormDs();
		Map<String,String> map = formDs.get(widget);
		if(map != null){
			Iterator<String> values = map.values().iterator();
			StringBuffer buf = new StringBuffer();
			while (values.hasNext()) {
				buf.append(values.next());
			}
			return buf.toString();
		}		
		return "";
	}
}
