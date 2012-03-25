package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh button控件渲染器
 * @param <T>
 * @param <K>
 */
public class PCButtonCompRender extends UINormalComponentRender<UIButton, ButtonComp> {

	private static final long serialVersionUID = -5872343354143483180L;
	public PCButtonCompRender(UIButton uiEle, ButtonComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
		ButtonComp button = this.getWebElement();
		this.id = button.getId();
		if (this.id == null) {
			throw new LfwRuntimeException(this.getClass().getName() +"id不能为空！");
		}

		this.widget = button.getWidget() == null ? (this.getCurrWidget() == null ? "" : this.getCurrWidget().getId()) : button.getWidget().getId();

		this.divId = DIV_PRE + this.widget + "_" + id;

		this.varId = COMP_PRE + (this.widget.equals("") ? "" : this.widget + "_") + id;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UINormalComponentRender#generateBodyScript()
	 */
	public String generateBodyScript() {
		ButtonComp button = (ButtonComp) this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		StringBuffer buf = new StringBuffer();
		
		String buttonId = getVarId();
		String tip = button.getTip() == null ? "" : translate(button.getTip(), button.getTip(), button.getLangDir());
		String displayHotKey = button.getDisplayHotKey();
		if (displayHotKey != null && !"".equals(displayHotKey)) {
			tip += "(" + displayHotKey + ")";
		}
		buf.append("var ");
		buf.append(buttonId);
		buf.append(" = new ButtonComp(document.getElementById('");
		buf.append(getDivId());
		buf.append("'),'");
		buf.append(button.getId());
		
		buf.append("','0','0','");
		if(uiComp != null)
			buf.append(uiComp.getWidth()); //button.getWidth()
		else
			buf.append("60");
		buf.append("','");
		if(uiComp != null)
			buf.append(uiComp.getHeight()); //button.getHeight()
		else
			buf.append("22");
		buf.append("','");
		
		//buf.append("','0','0','100%','100%','");
		buf.append(translate(button.getI18nName(), button.getText(), button.getLangDir()));
		buf.append("','");
		buf.append(tip);
		buf.append("','");
		buf.append((button.getRefImg() == null || button.getRefImg().equals("")) ? "" : button.getRealRefImg());
		buf.append("','relative','");
		buf.append("left"); //button.getAlign()
		buf.append("',");
		buf.append(!button.isEnabled());
		buf.append(",'");
		if(uiComp != null)
			buf.append(uiComp.getClassName() == null ? "" : uiComp.getClassName());
		buf.append("');\n");
		String hotKey = button.getHotKey();
		if (hotKey != null && !"".equals(hotKey)) {
			buf.append(buttonId).append(".setHotKey(\"").append(hotKey).append("\");\n");
		}

		buf.append("pageUI.getWidget('" + getWidget() + "').addComponent(" + buttonId + ");\n");

		if (button.isVisible() == false)
			buf.append(buttonId + ".hide();\n");
		return buf.toString();
	}


	protected String getSourceType(IEventSupport ele) { 
		return LfwPageContext.SOURCE_TYPE_BUTTON;
	}

}
