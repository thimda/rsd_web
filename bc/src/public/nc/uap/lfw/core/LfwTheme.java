package nc.uap.lfw.core;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 用来储存当前外观定义的类.通过此类可完成动态换肤功能
 * @author dengjt
 *
 */
public class LfwTheme implements Serializable {
	private static final long serialVersionUID = -1684792772032253827L;
	public static final String LFW_BORDER_COLOR = "LFW_BORDER_COLOR";
	public static final String LFW_BACKGROUND_COLOR = "LFW_BACKGROUND_COLOR";
	public static final String LFW_COMP_BACKGROUND_COLOR = "LFW_COMP_BACKGROUND_COLOR";
	// 每个spliter区域的border颜色
	public static final String LFW_SPLITER_BORDER_COLOR = "LFW_SPLITER_BORDER_COLOR";
	
	private Map<String, String> elementMap = new HashMap<String, String>();
	
	private String id;
	private String name;
	private String i18nName;
	//皮肤所在上下文路径
	private String ctxPath;
	//主题文件绝对路径。
	private String absPath;
	public void setThemeElement(String key, String value){
		elementMap.put(key, value);
	}
	
	public String getThemeElement(String key){
		return elementMap.get(key);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getAbsPath() {
		return absPath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	public String getCtxPath() {
		return ctxPath;
	}

	public void setCtxPath(String ctxPath) {
		this.ctxPath = ctxPath;
	}
}
