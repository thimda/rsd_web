package nc.uap.lfw.core.comp;

public class GroupLayout extends WebComponent {
	private static final long serialVersionUID = 1L;
	private boolean open = true;
	private String i18nName = null;
	private String text = null;
	private String langDir;
	private String marginTop;
	private String marginBottom;
	private String marginLeft;
	private String marginRight;
	
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}
	
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(String marginTop) {
		this.marginTop = marginTop;
	}
	public String getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(String marginBottom) {
		this.marginBottom = marginBottom;
	}
	public String getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(String marginLeft) {
		this.marginLeft = marginLeft;
	}
	public String getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(String marginRight) {
		this.marginRight = marginRight;
	}
}
