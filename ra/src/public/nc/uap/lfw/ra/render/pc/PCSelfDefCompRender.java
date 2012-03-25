package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISelfDefComp;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * 自定义控件渲染器
 */
public class PCSelfDefCompRender extends UINormalComponentRender<UISelfDefComp, SelfDefComp> {

	public PCSelfDefCompRender(UISelfDefComp uiEle,SelfDefComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
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

		StringBuffer buf = new StringBuffer();
		SelfDefComp sdComp = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		
		String id = getVarId();
		buf.append("var ").append(id).append(" = new SelfDefComp(document.getElementById('");
		buf.append(getDivId()).append("'),'").append(sdComp.getId());
		buf.append("','0','0','100%','100%','absolute',");
		buf.append(sdComp.isVisible()).append(",'");
		
		String className = null;
		if(uiComp != null){
			className = uiComp.getClassName();
		}
		buf.append(className == null ? "" : className).append("');\n");

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");

		if (sdComp.isVisible() == false)
			buf.append(id + ".setVisible(false);\n");
		
		return buf.toString();
	}
	

	protected String getSourceType(IEventSupport ele) {

		return LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP;
	}
	
	public String getType(){
		return "selfdefcomp";
	}
}
