package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.WebElement;
/**
 * Label¿Ø¼þäÖÈ¾Tag
 * @author dengjt
 *
 */
public class LabelCompTag extends NormalComponentTag {

	public String generateBody() {
		return super.generateBody();
	}

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		LabelComp label = (LabelComp) getComponent();
		//LabelComp(parent, name, left, top, text, position, className)
		String labelId = getVarShowId();
		String text = translate(label.getI18nName(), label.getText(), label.getLangDir());
		if(text == null)
			text = "";
		buf.append("var ")
		   .append(labelId)
		   .append(" = new LabelComp(document.getElementById('" + getDivShowId() + "'),'" + label.getId() + "','0','0','" + text + "','relative','');\n");
		if (label.getColor() != null)
			buf.append(labelId + ".setColor('" + label.getColor() + "');\n");
	
		if (label.getInnerHTML() != null)
			buf.append(labelId + ".setInnerHTML('" + label.getInnerHTML() + "');\n");
		if (!label.isVisible())
			buf.append(labelId + ".hide();\n");
		else
			buf.append(labelId + ".show();\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + labelId + ");\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
}
