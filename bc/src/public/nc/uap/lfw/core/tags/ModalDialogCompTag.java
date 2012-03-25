package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.ModalDialogComp;
import nc.uap.lfw.core.comp.WebElement;


/**
 * Modal¶Ô»°¿ò¿Ø¼þTag
 * @author dengjt
 *
 */
public class ModalDialogCompTag extends ContainerComponentTag{
	private String width = "400px";
	private String height = "300px";
	private String left = "0";
	private String top = "0";
	public String generateHead() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"")
		   .append(DIV_PRE)
		   .append(getId())
		   .append("\" style=\"width:100%;height:100%;top:0;left:0\">");
		return buf.toString();
	}

	public String generateHeadScript() {
		//function ModalDialogComp(name, title, left, top, width, height, className)
		ModalDialogComp dialog = (ModalDialogComp) getComponent();
		StringBuffer buf = new StringBuffer();
		buf.append("window.")
		   .append(COMP_PRE)
		   .append(getId())
		   .append(" = new ModalDialogComp(\"")
		   .append(getId())
		   .append("\",\"")
		   .append(dialog.getI18nName())
		   .append("\",")
		   .append(getLeft())
		   .append(",")
		   .append(getTop())
		   .append(",\"")
		   .append(getWidth())
		   .append("\",\"")
		   .append(getHeight())
		   .append("\");\n");
		
		buf.append(COMP_PRE)
		   .append(getId())
		   .append(".getContentPane().appendChild(document.getElementById(\"")
		   .append(DIV_PRE)
		   .append(getId())
		   .append("\"));\n");
		return buf.toString();
	}

	public String generateTail() {
		return "</div>";
	}

	public String generateTailScript() {
		return super.generateTailScript();
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public void setTop(String top){
		this.top = top;
	}
	
	public String getTop(){
		return this.top;
	}
	
	public void setLeft(String left){
		this.left = left;
	}
	
	public String getLeft(){
		return this.left;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
