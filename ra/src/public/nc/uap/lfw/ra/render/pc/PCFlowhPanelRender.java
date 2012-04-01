package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh 横向布局的panel渲染器
 * @param <T>
 * @param <K>
 */
public class PCFlowhPanelRender extends UILayoutPanelRender<UIFlowhPanel, WebElement> {

	private String width;
	/**
	 * @param uiEle
	 * @param uimeta 
	 * @param pageMeta
	 * @param parentRender
	 */
	public PCFlowhPanelRender(UIFlowhPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIFlowhPanel flowhPanel = this.getUiElement();

		if (getParentRender() == null)
			throw new LfwRuntimeException("this tag must be included in FlowhLayout");

		if (id == null || id.equals("")) {
			throw new LfwRuntimeException(this.getClass().getName() + "id不能为空！");
		}

		width = this.getFormatSize(flowhPanel.getWidth());
		if (width.equals("0px"))
			width = null;
//		divId = this.getParentRender().getDivId() + "_" + id;
	}


	public String generalHeadHtml() {
		StringBuffer strBuf = new StringBuffer();
		String paddingTop = getPaddingTopString(false);
		String paddingBottom = getPaddingBottomString(false);
		String height = "height:100%;";
		if(!paddingTop.equals("") || !paddingBottom.equals(""))
			height = "";
		if (width != null) {
			strBuf.append("<div id=\"" + getNewDivId() + "\" haswidth=\"1\" style=\"" + getBorderString(false) + paddingTop + paddingBottom + "position:relative;width:" + width + ";" + height + "min-height:" + MIN_HEIGHT + ";float:left;\"");
		} else {
			strBuf.append("<div id=\"" + getNewDivId() + "\" haswidth=\"0\" style=\"" + getBorderString(false) + paddingTop + paddingBottom + "position:relative;" + height + "min-height:" + MIN_HEIGHT + ";float:left;\"");
		}
		if(this.getUiElement().getClassName() != null)
			strBuf.append(" class=\"").append(this.getUiElement().getClassName()).append("\"");
		strBuf.append(">\n");
		strBuf.append(this.generalEditableHeadHtml());
		return strBuf.toString();
	}

	public String generalHeadScript() {

		return "";
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {

		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_FLOWHPANEL;
	}

	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta,Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta,obj);
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $ce('DIV');\n");
		buf.append(newDivId).append(".style.position = 'relative';\n");
		buf.append(newDivId).append(".style.height = '100%';\n");
//		buf.append(newDivId).append(".style[CSSFLOAT] = 'left';\n");
		buf.append("if(IS_IE){");
		buf.append(newDivId).append(".style.styleFloat = 'left';\n");
		buf.append("}else{");
		buf.append(newDivId).append(".style.cssFloat = 'left';\n");
		buf.append("};\n");		
		buf.append(newDivId).append(".style.minHeight = '").append(MIN_HEIGHT).append("';\n");
		buf.append(newDivId).append(".id = '" + newDivId + "';\n");
		
		getBorderScript(buf, getNewDivId());
		getPaddingLeftScript(buf, getNewDivId());
		getPaddingRightScript(buf, getNewDivId());
		getPaddingTopScript(buf, getNewDivId());
		getPaddingBottomScript(buf, getNewDivId());
		
		if (width != null) {
			buf.append(newDivId).append(".style.width = '" + width + "';\n");
			buf.append(newDivId).append(".setAttribute('haswidth','1');\n");
		} else {
			buf.append(newDivId).append(".setAttribute('haswidth','0');\n");
		}
		
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			
			buf.append(newDivId).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		if(obj instanceof UpdatePair){
			UpdatePair pair = (UpdatePair) obj;
			if(pair.getKey().equals(UIFlowhPanel.WIDTH)){
				String width = (String) pair.getValue();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(getNewDivId()).append(" = $ge('"+getNewDivId()+"');\n");
				if (width != null) {
					buf.append(getNewDivId()).append(".style.width = '" + width + "px';\n");
					buf.append(getNewDivId()).append(".setAttribute('haswidth','1');\n");
				} else {
					buf.append(getNewDivId()).append(".setAttribute('haswidth','0');\n");
				}
				buf.append("layoutInitFunc();");		
				addDynamicScript(buf.toString());
			}
		}
		else{
			super.notifyUpdate(uiMeta, pageMeta, obj);
		}
		
	}
	
	

}
