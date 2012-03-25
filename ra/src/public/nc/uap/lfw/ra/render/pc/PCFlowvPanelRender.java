package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh
 * 纵向布局的panel渲染器
 * @param <T>
 * @param <K>
 */
public class PCFlowvPanelRender extends UILayoutPanelRender<UIFlowvPanel, WebElement> {
	// 高度
	private String height;
	// 锚点名称
	private String anchor;

	private String className;
	public PCFlowvPanelRender(UIFlowvPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta,parentRender);
		if (getParentRender() == null)
			throw new LfwRuntimeException("this tag must be included in FlowvLayout");
		
		/** modify by renxh 2011-10-09 原因：要求每个panel必须有自己的id属性，否则将抛出异常*/
		int childCount = parentRender.getChildCount();
		childCount++;
		parentRender.setChildCount(childCount);
		// 设置默认ID
		if (id == null || id.equals("")) {
			id = String.valueOf(childCount);
		}
//		
		if (id == null || id.equals("")) {
			throw new LfwRuntimeException(this.getClass().getName() + "id不能为空！");
		}
		
//		divId = getParentRender().getDivId() + "_" + id;
		UIFlowvPanel flowvPanel = this.getUiElement();
		this.height = this.getFormatSize(flowvPanel.getHeight());
		if(this.height.equals("0px"))
			this.height = null;
		this.anchor = flowvPanel.getAnchor();
		this.className = uiEle.getClassName();

	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		if (height != null) {
			buf.append("<div id=\"" + getNewDivId() + "\" hasheight=\"1\" style=\"" + getBorderString(false) + getMarginLeftString(false) + getMarginRightString(false) + getMarginTopString(false) + getMarginBottomString(false) + "height:" + height + ";left:0px;position:relative;\" ");
		} 
		else {
			buf.append("<div id=\"" + getNewDivId() + "\" hasheight=\"0\" style=\"" + getBorderString(false) + getMarginLeftString(false) + getMarginRightString(false) + getMarginTopString(false) + getMarginBottomString(false) + "left:0px;position:relative;\" ");
		}
		if (anchor != null)
			buf.append("anchor=\"").append(anchor).append("\"");
		if(this.className != null)
			buf.append("class=\"").append(this.className).append("\"");
		buf.append(">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}
	
	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.left = '0px';\n");
		buf.append(getNewDivId()).append(".style.position = 'relative';\n");
//		buf.append(getNewDivId()).append(".style.overflow = 'auto';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		
		if (height != null) {
			buf.append(getNewDivId()).append(".style.height = '"+height+"';\n");
			buf.append(getNewDivId()).append(".setAttribute('hasheight','1');\n");
		} else {
			buf.append(getNewDivId()).append(".setAttribute('hasheight','0');\n");
//			buf.append(getNewDivId()).append(".style.minHeight = '30px';\n");
		}
		if (anchor != null)
			buf.append(getNewDivId()).append(".anchor='").append(anchor).append("';\n");
		
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
	

	@Override
	public String generalEditableHeadHtml() {
		if (isEditMode()) {
			StringBuffer buf = new StringBuffer("");
			buf.append("<div ");
			buf.append(" id='").append(getDivId()).append("'");
			buf.append(" style='height:100%;min-height:" + MIN_HEIGHT + ";overflow-x:hidden;'");
			buf.append(">");
			return buf.toString();
		}
		return "";
	}
	/**
	 * 2011-8-2 下午07:02:06 renxh des：编辑态时，需加入此div
	 * 
	 * @return
	 */
	public String generalEditableHeadHtmlDynamic() {
		if (isEditMode()) {

			StringBuffer buf = new StringBuffer("");
			buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
			buf.append(getDivId()).append(".style.position = 'relative';\n");
			buf.append(getDivId()).append(".style.height = '100%';\n");
			buf.append(getDivId()).append(".style.overflowX = 'hidden';\n");
			buf.append(getDivId()).append(".id='" + getDivId() + "';\n");
			if(isFlowMode()){
				buf.append(getDivId()).append(".style.minHeight = '" + MIN_HEIGHT + "';\n");
				buf.append(getDivId()).append(".flowmode = true;\n");
			}
			return buf.toString();
		}
		return "";
	}

	public String generalHeadScript() {
		return "";
	}

	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {

		return "";
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_FLOWVPANEL;
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyDestroy(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		if(obj instanceof UpdatePair){
			UpdatePair pair = (UpdatePair) obj;
			if(pair.getKey().equals(UIFlowvPanel.HEIGHT)){
				String height = (String) pair.getValue();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(getNewDivId()).append(" = $ge('"+getNewDivId()+"');\n");
				if (height != null) {
					buf.append(getNewDivId()).append(".style.height = '" + height + "px';\n");
					buf.append(getNewDivId()).append(".setAttribute('hasheight','1');\n");
				} else {
					buf.append(getNewDivId()).append(".setAttribute('hasheight','0');\n");
				}
				if (anchor != null)
					buf.append(getNewDivId()).append(".anchor='").append(anchor).append("';\n");
				
				buf.append("layoutInitFunc();");
				
				addDynamicScript(buf.toString());
			}
		}
		else
			super.notifyUpdate(uiMeta, pageMeta, obj);
	}


}
