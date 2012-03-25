package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh
 *OutLookBarComp控件元素
 */
public class PCOutlookItemRender extends UILayoutPanelRender<UIShutterItem, WebElement> {

	public PCOutlookItemRender(UIShutterItem uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIShutterItem item = this.getUiElement();
		this.divId = DIV_PRE + this.id;
		this.text = item.getText();
		this.i18nName = item.getI18nName();
	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;height:100%;overflow:auto;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {

		UILayoutRender<?, ?> parent = (UILayoutRender<?, ?>)this.getParentRender();
		String outlookId = parent.getId();
		String outlookShowId = parent.getVarId();
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getId()).append(" = ");
		buf.append(outlookShowId).append(".addItem(\"").append(getId());
		buf.append("\",\"").append(text == null ? i18nName : text).append("\",");
		if (image != null)
			buf.append("\"");
		buf.append(image);
		if (image != null)
			buf.append("\"");
		buf.append(",\"").append(text == null ? i18nName : text).append("\");\n");

		buf.append(getId()).append(".add(document.getElementById(\"").append(getNewDivId()).append("\"));\n");

		buf.append("window.$").append(outlookId).append("_").append(getId()).append("_init = function(){\n");

		// 将已有的脚本暂存在临时变量中
		StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuffer();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		this.setContextAttribute("$item_" + getId() + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());

		return buf.toString();
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {

		PCOutlookbarCompRender parent = (PCOutlookbarCompRender) this.getParentRender();
		StringBuffer tmpBuf = new StringBuffer();
		StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuffer();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		
		String tmpScript = (String) this.getContextAttribute("$item_" + getId() + "$tmpScript");
		// 如果是当前显示项目
		if (parent.getCurrentIndex() != parent.getItemList().indexOf(getId())) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			dsScript.append(tmpScript);
		} else {
			dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$item_" + getId() + "$tmpScript");
		tmpBuf.append("\n}\n");
		return tmpBuf.toString();
	}

	private String text;
	private String i18nName;
	private String image;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".overflow = 'hidden';\n");
		buf.append(getNewDivId()).append(".id = '"+getNewDivId()+"';\n");
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild("+getDivId()+");\n");
		}
		return buf.toString();
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		UILayoutRender<?, ?> parent = (UILayoutRender<?, ?>)this.getParentRender();
		String outlookId = parent.getId();
		String outlookShowId = parent.getVarId();
		StringBuffer buf = new StringBuffer();
		super.notifyDestroy(uiMeta, pageMeta, obj);
	}

}
