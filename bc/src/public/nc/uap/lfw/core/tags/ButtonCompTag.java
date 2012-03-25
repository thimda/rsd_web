package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
/**
 * Button¿Ø¼þäÖÈ¾Tag
 * @author dengjt
 *
 */
public class ButtonCompTag extends NormalComponentTag {

	public ButtonCompTag() {
		setWidth("60");
		setHeight("22");
	}
	
	public String generateBody() {
		return super.generateBody();
	}

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		ButtonComp button = (ButtonComp) getComponent();
		
		String buttonId = getVarShowId();
		String tip = button.getTip() == null? "": translate(button.getTip(), button.getTip(), button.getLangDir());
		String displayHotKey = button.getDisplayHotKey();
		if (displayHotKey != null && !"".equals(displayHotKey)) {
			tip += "(" + displayHotKey + ")";
		}
		buf.append("var ")
		   .append(buttonId)
		   .append(" = new ButtonComp(document.getElementById('")
		   .append(getDivShowId())
		   .append("'),'")
		   .append(button.getId())
		   .append("','0','0','")
		   .append(getWidth()) //button.getWidth()
		   .append("','")
		   .append(getHeight()) //button.getHeight()
		   .append("','")
		   .append(translate(button.getI18nName(), button.getText(), button.getLangDir()))
		   .append("','")
		   .append(tip)
		   .append("','")
//		   .append((button.getRefImg() == null || button.getRefImg().equals(""))?"":getRealImgPath(button.getRefImg()))
		   .append((button.getRefImg() == null || button.getRefImg().equals("")) ? "" : button.getRealRefImg())
		   .append("','")
		   .append("relative")
		   .append("','")
		   .append("left") //button.getAlign()
		   .append("',")
		   .append(!button.isEnabled()) 
		   .append(",'")
		   .append(getClassName())
		   .append("');\n");
		
		String hotKey = button.getHotKey();
		if (hotKey != null && !"".equals(hotKey)) {
			buf.append(buttonId)
			   .append(".setHotKey(\"")
			   .append(hotKey)
			   .append("\");\n");
		}
		
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + buttonId + ");\n");
		
		if(button.isVisible() == false)
			buf.append(buttonId + ".hide();\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_BUTTON;
	}

}
