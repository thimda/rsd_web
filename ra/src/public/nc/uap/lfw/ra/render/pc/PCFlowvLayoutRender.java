package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * ���򲼾���Ⱦ��
 * @param <T>
 * @param <K>
 */
public class PCFlowvLayoutRender extends UILayoutRender<UIFlowvLayout, WebElement> {

	// FLOW-V ����ID�����ַ���
	protected static final String FLOW_V_ID_BASE = "pc_flowv_layout_";

//	// û���ø߶������Ƿ��Զ����
//	private boolean autoFill = true;

	public PCFlowvLayoutRender(UIFlowvLayout uiEle, UIMeta uimeta, PageMeta pageMeta,UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIFlowvLayout flowvLayout = this.getUiElement();
		
//		if(flowvLayout.getAttribute(UIFlowvLayout.AUTO_FILL) != null){
//			if((Integer)flowvLayout.getAttribute(UIFlowvLayout.AUTO_FILL) == 0){
//				this.autoFill = true;
//			}
//			else if((Integer)flowvLayout.getAttribute(UIFlowvLayout.AUTO_FILL) == 1){
//				this.autoFill = false;
//			}
//		}
//		divId = DIV_PRE + (widget == null || widget.equals("") ? "" : (widget + "_")) + id;
	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		String height = "height:100%;";
		if(isFlowMode()){
			height = "";
		}
//		if (autoFill == false) // �����Զ����
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;" + height + "overflow:visible;position:relative;\">\n");
//		else
//			buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;" + height + "overflow:hidden;position:relative;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {
		StringBuffer buf = new StringBuffer();
		if(!isFlowMode()){
			//if (autoFill) { // �����Զ����
			buf.append(toResizeDynamic("$ge(\""+getDivId()+"\")", "flowvResize"));
			//}
		}
//		buf.append(" if (IS_IE7) $ge(\"" + getNewDivId() + "\").style.position=\"absolute\";\n");
		return buf.toString();
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {

		return "";
	}

//	public boolean isAutoFill() {
//		return autoFill;
//	}
//
//	public void setAutoFill(boolean autoFill) {
//		this.autoFill = autoFill;
//	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT;
	}
	
	

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
//		String height = "height:100%;";
//		if(isFlowMode()){
//			height = "";
//		}
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		if(!isFlowMode())
			buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.position = 'relative';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");		
//		if (autoFill == false){ // �����Զ����
		buf.append(getNewDivId()).append(".style.overflowY = 'auto';\n");
		buf.append(getNewDivId()).append(".style.overflowX = 'hidden';\n");
//		} else{
//			buf.append(getNewDivId()).append(".style.overflow = 'hidden';\n");
//		}
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
}
