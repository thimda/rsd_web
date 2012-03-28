package nc.uap.lfw.jsp.uimeta;


/**
 * panel UI
 * @author zhangxya
 *
 */
public class UICanvas extends UILayout {
	private static final long serialVersionUID = 1L;
	public static final String NULLCANVAS = "nonecanvas";
	public static final String LEFTCANVAS = "leftcanvas";
	public static final String RIGHTCANVAS = "rightcanvas";
	public static final String FULLCANVAS = "fullcanvas";
	public static final String CENTERCANVAS = "centercanvas";
	public static final String TITLE = "title";
	public static final String I18NNAME = "i18nName";

	public UICanvas(){
		setClassName(FULLCANVAS);
	}
	
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
