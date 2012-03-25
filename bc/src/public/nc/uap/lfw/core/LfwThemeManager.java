package nc.uap.lfw.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 皮肤管理器。可根据主题ID获取主题的各项配置.主题分为公共主题及模块主题。模块主题仅可在所在模块中使用
 * @author dengjt
 *
 */
public class LfwThemeManager {
	private static final String MODULE_PRE = "$";
	//公共主题
	private static Map<String, LfwTheme> themeMap;
	//模块主题
	private static Map<String, LfwTheme> moduleThemeMap;
	static{
		themeMap = new HashMap<String, LfwTheme>();
		moduleThemeMap = new HashMap<String, LfwTheme>();
	}
	
	public static void registerModelTheme(String moduleId, LfwTheme theme) {
		String themeId = aggregateId(moduleId, theme.getId());
		moduleThemeMap.put(themeId, theme);
	}
	
	public static void registerTheme(LfwTheme theme){
		themeMap.put(theme.getId(), theme);
	}
	
	public static LfwTheme getLfwTheme(String moduleId, String themeId) {
		LfwTheme theme = themeMap.get(themeId);
		if(theme == null){
			if(moduleThemeMap != null){
				String id = aggregateId(moduleId, themeId);
				theme = moduleThemeMap.get(id);
			}
		}
		return theme;
	}
	
	/**
	 * 获取web框架内置主题
	 * @return
	 */
	public static LfwTheme[] getLfwThemes(){
		return themeMap.values().toArray(new LfwTheme[0]);
	}
	
	/**
	 * 获取某个模块主题
	 * @param moduleId
	 * @return
	 */
	public static LfwTheme[] getModelThemes(String moduleId) {
		String id = MODULE_PRE + moduleId;
		if(moduleThemeMap != null){
			List<LfwTheme> tList = new ArrayList<LfwTheme>();
			Iterator<String> it = moduleThemeMap.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				if(key.startsWith(id)){
					tList.add(moduleThemeMap.get(key));
				}
			}
			return tList.toArray(new LfwTheme[0]);
		}
		return null;
	}
	
	/**
	 * 获得所有主题,包含各个模块自定义主题
	 * @return
	 */
	public static LfwTheme[] getAllThemes() {
		LfwTheme[] lfwThemes = getLfwThemes();
		if(moduleThemeMap == null || moduleThemeMap.size() == 0)
			return lfwThemes;
		LfwTheme[] mThemes = moduleThemeMap.values().toArray(new LfwTheme[0]);
		LfwTheme[] themes = new LfwTheme[lfwThemes.length + mThemes.length];
		System.arraycopy(lfwThemes, 0, themes, 0, lfwThemes.length);
		System.arraycopy(mThemes, 0, themes, lfwThemes.length, mThemes.length);
		return themes;
	}
	
	/**
	 * 组合模块主题ID
	 * @param moduleId
	 * @param themeId
	 * @return
	 */
	private static String aggregateId(String moduleId, String themeId){
		return MODULE_PRE + moduleId + "." + themeId;
	}
}
