package nc.uap.lfw.ra.render.pc;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh Outlookbar控件渲染器 OutLookBarComp控件,提供类似百叶窗式的树行控件
 * 
 */
public class PCOutlookbarCompRender extends UILayoutRender<UIShutter, WebElement> {

	private String className;
	private int currentIndex = 0;
	private List<String> itemList = new ArrayList<String>();
	public PCOutlookbarCompRender(UIShutter uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.setPageMeta(pageMeta);
		UIShutter shutter = this.getUiElement();
		this.className = shutter.getClassName();
		if(shutter.getCurrentItem() != null)
			this.currentIndex = Integer.parseInt(shutter.getCurrentItem());
		this.divId = DIV_PRE + this.id;
		if (this.widget == null || this.widget.equals("")) {
			this.varId = COMP_PRE + this.id;
		} else {
			this.varId = COMP_PRE + this.widget + "_" + this.id;
		}

	}


	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(getNewDivId());
		buf.append("\" style=\"top:").append(0);
		buf.append(";left:").append(0);
		buf.append(";width:").append("100%");
		buf.append(";height:").append("100%");
		buf.append(";padding-left:").append(0);
		buf.append(";padding-top:").append(0);
		buf.append(";padding-right:").append(0);
		buf.append(";padding-bottom:").append(0);
		buf.append(";overflow:hidden;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {

		StringBuffer buf = new StringBuffer();

		String showId = getVarId();
		buf.append("var ").append(showId).append(" = new OutLookBarComp(document.getElementById(\"").append(getDivId()).append("\"), \"");
		buf.append(getId()).append("\", 0, 0, \"100%\", \"100%\", \"relative\", ");
		if (className != null && !className.equals(""))
			buf.append("\"").append(className).append("\");\n");
		else
			buf.append("'');\n");
		
		if (getWidget() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getWidget('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addOutlook(" + getVarId() + ");\n");
		} else {
			buf.append("pageUI.addOutlook(" + getVarId() + ");\n");
		}
		
		UIShutter shutter = this.getUiElement();
		if (shutter != null)
			buf.append(addEventSupport(shutter, getWidget(), showId, null));
//		OutlookbarComp outlook = getOutlookBar();
//		if(outlook != null)
//			buf.append(addEventSupport(shutter, getWidget(), showId, null));

		return buf.toString();
	}


	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {

		StringBuffer buf = new StringBuffer();
		buf.append(getVarId()).append(".afterActivedItemChangeForInternal = function(currItem){\n");
		buf.append("var tmpFunc = window['$' + currItem.parentOwner.id + '_' + currItem.name + '_init'];\n");
		buf.append("if(tmpFunc){\ntmpFunc();\n");
		buf.append("window['$' + currItem.parentOwner.id + '_' + currItem.name + '_init'] = null;\n}\n");
		buf.append("};\n");
		buf.append(getVarId()).append(".activeItem(").append(currentIndex).append(");\n");
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_OUTLOOKBAR;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public List<String> getItemList() {
		return itemList;
	}

	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
//	
	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.top = '0';\n");
		buf.append(getNewDivId()).append(".style.left = '0';\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".style.paddingLeft = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingTop = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingRight = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingBottom = '0';\n");
		buf.append(getNewDivId()).append(".style.overflow = 'hidden';\n");
		buf.append(getNewDivId()).append(".id = '"+getNewDivId()+"';\n");
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild("+getDivId()+");\n");
		}
		return buf.toString();
	}
	
	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer buf = new StringBuffer();
		if(this.getWidget() != null){
			buf.append("var curWidget = pageUI.getWidget('").append(this.getWidget()).append("');\n");
			buf.append("var ").append(getVarId()).append(" = curWidget.getOutlook('" + this.getId() + "');\n");
		}
		else{
			buf.append("var ").append(getVarId()).append(" = pageUI.getOutlook('" + getId() + "');\n");
		}
		UpdatePair pair = (UpdatePair) obj;
		if(pair == null)
			return;
		if(pair.getKey() != null && pair.getKey().equals(UIShutter.CURRENT_ITEM)){
			buf.append("if("+getVarId()+"){\n");
			buf.append(getVarId()).append(".activeItem(" + pair.getValue() + ");\n");
			buf.append("};\n");
		}
		buf.append("layoutInitFunc();\n");		
		addBeforeExeScript(buf.toString());
	}
}
