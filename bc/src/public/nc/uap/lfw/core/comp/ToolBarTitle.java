package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * toolbar 标题
 * 
 * @author guoweic
 *
 */
public class ToolBarTitle extends WebComponent {
	
	private static final long serialVersionUID = 1L;
	
	private String refImg1 = "";
	private String refImg2 = "";
	
	// 图片路径是否改变
	private boolean refImg1Changed = true;
	// 图片真实路径，在context中使用
	private String realRefImg1;
	// 图片路径是否改变
	private boolean refImg2Changed = true;
	// 图片真实路径，在context中使用
	private String realRefImg2;
	
	private String text = "";
	private String i18nName = "";
	private String langDir = "";
	private String color = "black";
	private boolean isBold = false;
	private String menuId;

	public static final int DEFAULT_WIDTH = 120;
	public static final int DEFAULT_HEIGHT = 20;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
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
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	public String getRefImg1() {
		return refImg1;
	}
	public void setRefImg1(String refImg1) {
		this.refImg1 = refImg1;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getRefImg2() {
		return refImg2;
	}
	public void setRefImg2(String refImg2) {
		this.refImg2 = refImg2;
	}
	public boolean isRefImg1Changed() {
		return refImg1Changed;
	}
	public void setRefImg1Changed(boolean refImg1Changed) {
		this.refImg1Changed = refImg1Changed;
	}
	public String getRealRefImg1() {
		if (isRefImg1Changed()) {
			realRefImg1 = getRealImgPath(this.refImg1, null);
			setRefImg1Changed(false);
		}
		return realRefImg1;
	}
	public void setRealRefImg1(String realRefImg1) {
		this.realRefImg1 = realRefImg1;
	}
	public boolean isRefImg2Changed() {
		return refImg2Changed;
	}
	public void setRefImg2Changed(boolean refImg2Changed) {
		this.refImg2Changed = refImg2Changed;
	}
	public String getRealRefImg2() {
		if (isRefImg2Changed()) {
			realRefImg2 = getRealImgPath(this.refImg2, null);
			setRefImg2Changed(false);
		}
		return realRefImg2;
	}
	public void setRealRefImg2(String realRefImg2) {
		this.realRefImg2 = realRefImg2;
	}

}
