package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.core.exception.LfwRuntimeException;
/**
 * 信号-槽连接器配置
 * @author dengjt
 *
 */
public class Connector implements Cloneable, Serializable {
	private static final long serialVersionUID = 6792907017833197559L;
	private String id;
	
	//发起信号window
	private String sourceWindow;
	//发起信号对象
	private String source;
	//接受对象 window
	private String targetWindow;
	//接受对象
	private String target;
	
	private String pluginId;
	private String plugoutId;
	
	//输入与输出值描述项映射，如果1对1完全对应，则此mapping 不需要录入
	private Map<String, String> mapping = null;
	
	
	public String getSourceWindow() {
		return sourceWindow;
	}
	public void setSourceWindow(String sourceWindow) {
		this.sourceWindow = sourceWindow;
	}
	public String getTargetWindow() {
		return targetWindow;
	}
	public void setTargetWindow(String targetWindow) {
		this.targetWindow = targetWindow;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPluginId() {
		return pluginId;
	}
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	public String getPlugoutId() {
		return plugoutId;
	}
	public void setPlugoutId(String plugoutId) {
		this.plugoutId = plugoutId;
	}
	public Map<String, String> getMapping() {
		return mapping;
	}
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
	public void putMapping(String key, String value){
		if (this.mapping == null)
			this.mapping = new HashMap<String, String>();
		this.mapping.put(key, value);
	}
	
	public Object clone(){
		try {
			Connector connector = (Connector)super.clone();
			Map<String, String> mapping = null;
			if (this.mapping != null){
				connector.mapping = new HashMap<String, String>();
				Iterator<String>  it = this.mapping.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
						connector.mapping.put(key, this.mapping.get(key));
				}
			}
			
			return connector;
		} catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
	
}
