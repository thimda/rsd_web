package nc.uap.lfw.jsp.uimeta;

import java.util.List;


/**
 * panel UI
 * @author zhangxya
 *
 */
public class UIPanel extends UILayout {

	private static final long serialVersionUID = 1L;
	public static final String TITLE = "title";
	public static final String I18NNAME = "i18nName";

	public String getTitle() {
		return (String) getAttribute(TITLE);
	}

	public void setTitle(String title) {
		setAttribute(TITLE, title);
	}

	public String getI18nName() {
		return (String) getAttribute(I18NNAME);
	}
	
	public void setI18nName(String i18nName){
		setAttribute(I18NNAME, i18nName);
	}

	public void setLangDir(String langDir) {
		
	}

	public UIPanelPanel addElementToPanel(UIComponent uigrid) {
		List<UILayoutPanel> list = getPanelList();
		UIPanelPanel panel = null;
		if(list != null && list.size() > 0){
			panel = (UIPanelPanel) list.get(0);
		}
		else{
			panel = new UIPanelPanel();
			panel.setId("panel");
			addPanel(panel);
		}
		panel.setElement(uigrid);
		return panel;
	}
}
