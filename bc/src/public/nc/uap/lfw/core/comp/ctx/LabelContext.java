package nc.uap.lfw.core.comp.ctx;

public class LabelContext extends BaseContext 
{
	private static final long serialVersionUID = 5421922178586031563L;
	private boolean enabled = true;
//	private String i18nName;
	private String text;
	private String innerHTML;
	private boolean visible = true;
	private String color;
	private String style;
	private String weight;
	private String size;
	private String family;
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
//	public String getI18nName() {
//		return i18nName;
//	}
//	public void setI18nName(String name) {
//		i18nName = name;
//	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getInnerHTML() {
		return innerHTML;
	}
	public void setInnerHTML(String innerHTML) {
		this.innerHTML = innerHTML;
	}
}
