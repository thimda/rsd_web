package nc.uap.lfw.login.login;

import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.LfwThemeManager;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;

/**
 * NC帐套信息获取类
 * @author gd 2010-4-8
 * @version NC6.0
 */
public class ThemeDatas extends ComboData
{
	private static final long serialVersionUID = -937084332629423579L;

	@Override
	public CombItem[] getAllCombItems() {
		LfwTheme[] themes = LfwThemeManager.getLfwThemes();
		//获取当前所有帐套
		CombItem[] items = new CombItem[themes.length];
		for (int j = 0; j < themes.length; j++) {
			CombItem item = new CombItem();
			item.setText(themes[j].getName());
			item.setI18nName(themes[j].getI18nName());
			item.setValue(themes[j].getId());
			items[j] = item;
		}	
		return items;
	}
}
