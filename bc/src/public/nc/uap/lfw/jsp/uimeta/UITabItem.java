package nc.uap.lfw.jsp.uimeta;

import java.io.Serializable;

import nc.vo.jcom.lang.StringUtil;


public class UITabItem extends UILayoutPanel {
	private static final long serialVersionUID = 1835380360211884210L;
	
	public static final String TEXT = "text";
	public static final String I18NNAME = "i18nName";
	public static final String LANGDIRF = "langDir";
	
	public static final String STATE = "state";
	
	public static final String SHOWCLOSEICON = "showCloseIcon";
	
	public static final String ACTIVE = "active";
	
	public UITabItem(){
		setActive("1");
	}
	
	public void setId(String id) {
		setAttribute(ID, id);
	}

	public String getId() {
		return (String) getAttribute(ID);
	}

	public String getText() {
		return (String) getAttribute(TEXT);
	}

	public void setText(String text) {
		setAttribute(TEXT, text);
		UpdatePair pair = new UpdatePair(TEXT, text);
		notifyChange(UPDATE, pair);
	}

	public String getI18nName() {
		return (String) getAttribute(I18NNAME);
	}

	public void setI18nName(String name) {
		setAttribute(I18NNAME, name);
	}

	public String getLangDir() {
		return (String) getAttribute(LANGDIRF);
	}

	public void setLangDir(String langDir) {
		setAttribute(LANGDIRF, langDir);
	}
	
	public Integer getState() {
		return (Integer) getAttribute(STATE);
	}
	
	public void setState(Integer pageState) {
		setAttribute(STATE, pageState);
	}
	
	public String getShowCloseIcon() {
		return (String) getAttribute(SHOWCLOSEICON);
	}
	
	public void setShowCloseIcon(String showColseIcon) {
		setAttribute(SHOWCLOSEICON,showColseIcon);
	}

	public String getActive(){
		return (String) getAttribute(ACTIVE);
	}
	
	/**
	 * 设置页签是否缓初始化
	 * @param active
	 */
	public void setActive(String active){
		setAttribute(ACTIVE, active);
	}
	
	@Override
	public Serializable getAttribute(String key) {
		Serializable obj = super.getAttribute(key);
		if(key.equals(STATE)){
			if(obj == null)
				return new Integer(-1);
		}
		return obj;
	}

	@Override
	public void setVisible(boolean visibility) {
		String value;
		if(visibility){
			value = StringUtil.toString(true);
		}
		else
			value = StringUtil.toString(false);
		UpdatePair pair = new UpdatePair(UIElement.VISIBLE, value);
		notifyChange(UPDATE, pair);
	}
	
	
}
