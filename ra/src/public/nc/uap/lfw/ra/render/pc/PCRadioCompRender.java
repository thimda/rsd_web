package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.RadioComp;
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
 * @author renxh Radio控件渲染器
 */
public class PCRadioCompRender extends UINormalComponentRender<UITextField, RadioComp> {
	
	public PCRadioCompRender(UITextField uiEle,RadioComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}
	
	@Override
	public String generateBodyHtml() {
		return super.generateBodyHtml();
	}
	
	@Override
	public String generateBodyHtmlDynamic() {
		return super.generateBodyHtmlDynamic();
	}
	
	@Override
	public String generateBodyScript() {
		WebComponent component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof RadioComp))
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null,
					new String[] { this.getId() })/* 标签配置出错，{0}不是RadioComp类型！ */);

		RadioComp radio = (RadioComp) component;
		StringBuffer buf = new StringBuffer();
		// RadioComp(parent, name, left, top, group, value, text, checked,
		// position, className)
		buf.append("window.").append(this.getVarId());
		buf.append(" = new RadioComp(").append("document.getElementById('" + getDivId());
		buf.append("'), '" + this.getId());
		buf.append("',0,0,'").append(radio.getGroup());
		buf.append("','").append(radio.getValue());
		buf.append("','").append(radio.getText());
		buf.append("',").append(radio.isChecked());
		buf.append(",'relative','").append(uiComp.getClassName()).append("'");
		String attrArr = generateAttrArr(radio, uiComp);
		buf.append(",").append(attrArr).append(");\n");
		return buf.toString();
	}
	
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}
	
	@Override
	protected String getRenderType(WebElement ele) {
		return EditorTypeConst.RADIOCOMP;
	}
	
	public String getType(){
		return "radiotext";
	}
	
	private String generateAttrArr(RadioComp radio, UIComponent uiComp) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if(radio != null){
			
		}
		if(uiComp != null){
			if(uiComp.getAttribute(UITextField.IMG_SRC) != null){
				buf.append("'imgsrc':'").append(uiComp.getAttribute(UITextField.IMG_SRC)).append("'");
			}
		}
		buf.append("}");
		return buf.toString();
	}

}
