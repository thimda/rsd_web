package nc.uap.lfw.core.comp;

import java.io.Serializable;

public class ChartFont  implements Serializable  {
	
	private String name;
	private String size;
	private String color;
	private String bgColor;
	public ChartFont(){
		name = "";
		size = "";
		color = "000000";
		bgColor = "";
	}
	public String getName() {
		return name;
	}
	public void setName(String fontName) {
		this.name = fontName;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	
	public String GenNewScriptstr(){
		StringBuffer str = new StringBuffer();
		str.append(" new  ChartFont ( '").append(name).append("','")
			.append(size).append("','")
			.append(color).append("','")
			.append(bgColor).append("') ");		
		return str.toString();
	}
}
