package nc.uap.lfw.core.util;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;

/**
 * LFW EL���ʽ�Զ�����
 * @author dengjt
 *
 */
public class LfwElTool {
	/**
	 * ��ȡinfoName��Ӧ��ֵ
	 * @param infoName
	 * @return
	 */
	public static String getInfo(String infoName) {
		if(infoName == null)
			return null;
		else if(infoName.equals("background"))
			return getThemeElement(LfwTheme.LFW_BACKGROUND_COLOR);
		else if(infoName.equals("compBackground"))
			return getThemeElement(LfwTheme.LFW_COMP_BACKGROUND_COLOR);
		else if(infoName.equals("borderColor"))
			return getThemeElement(LfwTheme.LFW_BORDER_COLOR);
		else if(infoName.equals("spliterZoneBorderColor"))
			return getThemeElement(LfwTheme.LFW_SPLITER_BORDER_COLOR);
//		else if(infoName.equals("accountName"))
//			return LfwRuntimeEnvironment.getAccountName();
		else if(infoName.equals("themeKey"))
			return LfwRuntimeEnvironment.getTheme().getThemeElement(infoName);
		
//		else if(infoName.equals("modelClazz")) {
//			String modelClazz = LfwRuntimeEnvironment.getWebContext().getParameter("model");
//			if(modelClazz == null)
//				modelClazz = BillTemplatePageModel.class.getName();
//			return modelClazz;
//		}
		return null;
	}
	
	public static String getThemeElement(String key){
		return LfwRuntimeEnvironment.getTheme().getThemeElement(key);
	}
	
	public static String getBackground(){
		return getThemeElement(LfwTheme.LFW_BACKGROUND_COLOR);
	}
	
	public static String getCompBackground(){
		return getThemeElement(LfwTheme.LFW_COMP_BACKGROUND_COLOR);
	}
	
	public static String getBorderColor() {
		return getThemeElement(LfwTheme.LFW_BORDER_COLOR);
	}
	
	public static String getSpliterZoneBorderColor() {
		return getThemeElement(LfwTheme.LFW_SPLITER_BORDER_COLOR);
	}
}
