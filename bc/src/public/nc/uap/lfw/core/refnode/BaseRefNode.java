package nc.uap.lfw.core.refnode;

import nc.uap.lfw.core.comp.WidgetElement;

public class BaseRefNode  extends WidgetElement implements IRefNode{
	private static final long serialVersionUID = 4305953060997010652L;
	private String windowType;
	/**
	 * Ĭ��Dialog��ʾ
	 */
	private boolean dialog = true;
	/**
	 * Ĭ��ˢ��
	 */
	private boolean refresh = true;
	/**
	 * ����Ŀ¼
	 */
	private String langDir;
	private String i18nName;
	private String text;
	private String path;
	
	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	public boolean isDialog() {
		return dialog;
	}
	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}
	public String getWindowType() {
		return windowType;
	}
	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}
	
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
}
