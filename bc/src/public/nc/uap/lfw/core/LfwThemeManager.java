package nc.uap.lfw.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Ƥ�����������ɸ�������ID��ȡ����ĸ�������.�����Ϊ�������⼰ģ�����⡣ģ���������������ģ����ʹ��
 * @author dengjt
 *
 */
public class LfwThemeManager {
	private static final String MODULE_PRE = "$";
	//��������
	private static Map<String, LfwTheme> themeMap;
	//ģ������
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
	 * ��ȡweb�����������
	 * @return
	 */
	public static LfwTheme[] getLfwThemes(){
		return themeMap.values().toArray(new LfwTheme[0]);
	}
	
	/**
	 * ��ȡĳ��ģ������
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
	 * �����������,��������ģ���Զ�������
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
	 * ���ģ������ID
	 * @param moduleId
	 * @param themeId
	 * @return
	 */
	private static String aggregateId(String moduleId, String themeId){
		return MODULE_PRE + moduleId + "." + themeId;
	}
}
