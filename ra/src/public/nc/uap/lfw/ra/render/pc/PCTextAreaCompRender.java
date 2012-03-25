package nc.uap.lfw.ra.render.pc;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.vo.ml.NCLangRes4VoTransl;
/**
 * @author renxh TextAreaComp 渲染器 对应 对应 html 的标签 <textarea/>
 */
public class PCTextAreaCompRender extends UINormalComponentRender<UITextField, TextAreaComp> {
	public PCTextAreaCompRender(UITextField uiEle, TextAreaComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}
	public String generateBodyHtml() {
		return super.generateBodyHtml();
	}
	@Override public String generateBodyHtmlDynamic() {
		return super.generateBodyHtmlDynamic();
	}
	public String generateBodyScript() {
		WebComponent component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof TextAreaComp))
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "TextAreaCompTag-000000", null, new String[] { this.getId() }));
		TextAreaComp textArea = (TextAreaComp) component;
		StringBuffer buf = new StringBuffer();
		// TextAreaComp(parent, name, left, top, rows, cols, position, readOnly,
		// value, width, height)
		String id = getVarId();
		buf.append("window.").append(id);
		if (textArea.isShowMark())
			buf.append(" = new TextMarkComp(");
		else
			buf.append(" = new TextAreaComp(");
		String className = uiComp.getClassName();
		String width = "100%";
		try{
			width = String.valueOf(Integer.parseInt(uiComp.getWidth()) - 4);
		}catch(NumberFormatException e){}
		String height = "100%";
		try{
			height = String.valueOf(Integer.parseInt(uiComp.getHeight()) - 4);
		}catch(NumberFormatException e){}
		buf.append("document.getElementById('" + getDivId()).append("'), '" + this.getId()).append("',0,0,").append(textArea.getRows()).append(",").append(textArea.getCols()).append(",'relative',")
				.append(textArea.isReadOnly()).append(",'").append("").append("','" + width + "','" + height + "',").append(textArea.getTip() == null ? "null" : "'" + textArea.getTip() + "'").append(
						",'").append(className == null ? "" : className).append("');\n");
		String value = textArea.getValue();
		value = value.replaceAll("\r\n", "\n");
		value = value.replaceAll("\\n", "\r\n");
		value = value.replaceAll("\r\n", "\\\\\\r\\\\\\n");
		buf.append(id + ".setValue('" + value + "');\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		if (textArea.isEnabled() == false)
			buf.append(id).append(".setActive(false);\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		return buf.toString();
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TEXTAREA;
	}
	@Override protected String getRenderType(WebElement ele) {
		return EditorTypeConst.TEXTAREA;
	}
	public String getType(){
		return "textarea";
	}
}
