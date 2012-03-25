package nc.uap.lfw.jsp.uimeta;


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
}
