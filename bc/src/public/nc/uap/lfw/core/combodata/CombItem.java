package nc.uap.lfw.core.combodata;

import java.io.Serializable;

/**
 * 聚合类型的一条数据表示
 * @author dengjt
 */
public class CombItem implements Cloneable, Serializable{

	private static final long serialVersionUID = 5389129118776137462L;
	private String i18nName;
	private String text;
	private String value;
	private String image;
	private String langDir;
	
	public CombItem(){}
	
	public CombItem(String value, String i18nName)
	{
		this.i18nName = i18nName;
		this.value = value;
	}
	
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
