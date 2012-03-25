package nc.uap.lfw.ra.render.pc;

//import nc.uap.lfw.core.comp.TabItem;
//import nc.uap.lfw.core.comp.TabLayout;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.vo.jcom.lang.StringUtil;

/**
 * @author renxh ҳǩ��Ⱦ��
 */
public class PCTabItemRender extends UILayoutPanelRender<UITabItem, WebElement> {

	public PCTabItemRender(UITabItem uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
//		TabItem tabItem = (TabItem)this.getWebElement();
		UITabItem tabItem = this.getUiElement();
		this.i18nName = tabItem.getI18nName();
		this.text = tabItem.getText();
		this.visible = tabItem.getVisible();
		if (tabItem.getState() != null)
			this.state = tabItem.getState().toString();
		this.showCloseIcon = (tabItem.getShowCloseIcon() == null || tabItem.getShowCloseIcon().equals("")) ? "false" : tabItem.getShowCloseIcon();
		divId = DIV_PRE + parentRender.getId() + getId();

	}

	private String text;
	private String i18nName;
	private String langDir;
	// ҳ��״̬
	private String state;
	private String showCloseIcon;
	private String active;
	private Boolean visible;

	public String createRenderHtml() {
			return super.createRenderHtml();
	}

	public String createRenderScript() {
		return super.createRenderScript();
	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;height:100%;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {
		PCTabLayoutRender tab = (PCTabLayoutRender) this.getParentRender();
		StringBuffer buf = new StringBuffer();
		String tabId = tab.getVarId();
		// name, title, showCloseIcon, isFirstItem, disabled
		buf.append("var ").append(getId()).append(" = ");
		buf.append(tabId).append(".createItem(\"");
		buf.append(getId()).append("\", \"");
		buf.append(translate(i18nName, text, langDir));
		buf.append("\",").append(showCloseIcon); // item.isShowCloseIcon()
		buf.append(",").append(false); // item.isFirstItem()
		buf.append(",").append(false); // item.isDisabled()
		buf.append(",'").append(state == null ? "" : state).append("');\n");

		buf.append("var tmpDiv = document.getElementById(\"").append(getNewDivId()).append("\");\n");
		// .append("tmpDiv.style.display = \"\";\n");

		buf.append(getId()).append(".getObjHtml().appendChild(tmpDiv);\n");
		// �ٽ�tmpDiv�Ķ�λ������Ϊ���
		buf.append("tmpDiv.style.position='relative';\n");

		// �˴��Ƿ�ֹtmpDiv����Ϊ0�����(IE��Ⱦ�������Ϊ,�����ڵ�����tab�ϣ�����Ϊʲô�㲻���������������������Ͱ�ʵ�ʵ��Ӽӵ��游���档
		buf.append("if(tmpDiv.offsetWidth == 0){tmpDiv.parentNode.appendChild(tmpDiv.firstChild);tmpDiv.parentNode.removeChild(tmpDiv);}\n");

		buf.append("window.$").append(tab.getId()).append("_").append(getId()).append("_init = function(){\n");

		// �����еĽű��ݴ�����ʱ������
		StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuffer();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		this.setContextAttribute("$tabitem_" + getId() + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());
		
		return buf.toString();
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {
		PCTabLayoutRender tab = (PCTabLayoutRender) this.getParentRender();
		// TabComp tab = (TabComp) tabTag.getComponent();
		StringBuffer tmpBuf = new StringBuffer();
		StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
		
		dsScript.append(PcFormRenderUtil.getAllFormDsScript(this.id));//���ɱ������ݼ�������
		PcFormRenderUtil.removeFormDsScript(this.id);	
		
		String tmpScript = (String) this.getContextAttribute("$tabitem_" + getId() + "$tmpScript");
		tmpBuf.append(dsScript.toString());
		dsScript.delete(0, dsScript.length());
		dsScript.append(tmpScript.toString());
		// ����ǵ�ǰ��ʾ��Ŀ
		if (tab.getCurrentIndex() != tab.getItemList().indexOf(getId())) {
			// ��dsScript�е�����д��ҳ�棬���ָ�ԭ���Ľű�
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			dsScript.append(tmpScript);
		} else {
			if (tmpScript != null)
				dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$tabitem_" + getId() + "$tmpScript");
		tmpBuf.append("pageUI.updateInitedWidgets();\n");
		tmpBuf.append("};\n");
		return tmpBuf.toString();
	}

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

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TABITEM;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".overflow = 'hidden';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyUpdate(uiMeta, pageMeta, obj);
		if(obj == null){
			StringBuffer buf = new StringBuffer();
			UIRender pRender = (UIRender) this.getParentRender();
			String showId = pRender.getVarId();
			String id = pRender.getId();
			LfwWidget lfwWidget = pRender.getCurrWidget();
			if (lfwWidget != null) {
				String widget = WIDGET_PRE + lfwWidget.getId();
				buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
				buf.append("var "+showId+" = ").append(widget + ".getTab('" + id + "');\n");
			} else {
				buf.append("var "+showId+" = ").append("pageUI.getTab(" + id + ");\n");
			}
			buf.append("var "+getId()+" = " ).append(showId).append(".getItemByName('"+getId()+"');\n");
			buf.append(getId()).append(".changeTitle('"+translate(i18nName, text, langDir)+"');\n");
			buf.append("layoutInitFunc();\n");
			this.addDynamicScript(buf.toString());
		}
		else if(obj instanceof UpdatePair){
			UpdatePair pair = (UpdatePair) obj;
			if(pair.getKey() != null && pair.getKey().equals(UIElement.VISIBLE)){
				String isVisible = (String) pair.getValue();
				StringBuffer buf = new StringBuffer();
				UIRender pRender = (UIRender) this.getParentRender();
				String showId = pRender.getVarId();
				String id = pRender.getId();
				LfwWidget lfwWidget = pRender.getCurrWidget();
				if (lfwWidget != null) {
					String widget = WIDGET_PRE + lfwWidget.getId();
					buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
					buf.append("var "+showId+" = ").append(widget + ".getTab('" + id + "');\n");
				} else {
					buf.append("var "+showId+" = ").append("pageUI.getTab(" + id + ");\n");
				}
				if (isVisible.equals("true"))
					buf.append(showId).append(".showItem('"+getId()+"');\n");
				else
					buf.append(showId).append(".hideItem('"+getId()+"');\n");
				this.addDynamicScript(buf.toString());
			}
			else if(pair.getKey() != null && pair.getKey().equals(UITabItem.TEXT)){
				String text = (String) pair.getValue();
				StringBuffer buf = new StringBuffer();
				UIRender pRender = (UIRender) this.getParentRender();
				String showId = pRender.getVarId();
				String id = pRender.getId();
				LfwWidget lfwWidget = pRender.getCurrWidget();
				if (lfwWidget != null) {
					String widget = WIDGET_PRE + lfwWidget.getId();
					buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
					buf.append("var "+showId+" = ").append(widget + ".getTab('" + id + "');\n");
				} else {
					buf.append("var "+showId+" = ").append("pageUI.getTab(" + id + ");\n");
				}
				buf.append("var "+getId()+" = " ).append(showId).append(".getItemByName('"+getId()+"');\n");
				buf.append(getId()).append(".changeTitle('"+translate(i18nName, text, langDir)+"');\n");
				buf.append("layoutInitFunc();\n");
				this.addDynamicScript(buf.toString());
			}
		}
	}
	
	

}
