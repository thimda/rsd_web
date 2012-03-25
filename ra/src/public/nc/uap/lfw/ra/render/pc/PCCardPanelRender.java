package nc.uap.lfw.ra.render.pc;

import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UICardPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh 卡片布局的panel渲染器
 * @param <T>
 * @param <K>
 */
public class PCCardPanelRender<T extends UICardPanel, K extends WebElement> extends UILayoutPanelRender<T, K> {

	public PCCardPanelRender(T uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		PCCardLayoutRender parent = (PCCardLayoutRender) this.getParentRender();
		int cardCount = parent.getCardCount();
		cardCount++;
		parent.setCardCount(cardCount);
		UICardPanel cardPanel = this.getUiElement();
		this.id = cardPanel.getId();
		if (id == null || id.equals("")) {
			id = String.valueOf(cardCount);
		}
		divId = parentRender.getDivId() + "_" + id;
	}

	public String generalHeadHtml() {
		StringBuffer strBuf = new StringBuffer();
//		strBuf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;height:100%;position:absolute;\">\n");
		strBuf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;height:100%;position:relative;\">\n");
		strBuf.append(this.generalEditableHeadHtml());
		return strBuf.toString();
	}

	public String generalHeadScript() {

		PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getParentRender();

		// 获取记录当前子Item的Index
		Integer itemIndex = (Integer) getCurrentItemIndex();
		StringBuffer buf = new StringBuffer();
		buf.append("window.$").append(parentRender.getId()).append("_item").append(itemIndex).append(" = function(){\n");

		// 将已有的脚本暂存在临时变量中
		StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);

		if (dsScript == null) {
			dsScript = new StringBuffer();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}

		this.setContextAttribute("$card_" + id + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());

		return buf.toString();
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {
		StringBuffer tmpBuf = new StringBuffer();
		StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
		
		dsScript.append(PcFormRenderUtil.getAllFormDsScript(this.widget));//自由表单的数据集的设置
//		PcFormRenderUtil.removeFormDsScript(this.widget);	
		
		
		String tmpScript = (String) this.getContextAttribute("$card_" + id + "$tmpScript");
		PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getParentRender();
		
		UICardLayout card = parentRender.getUiElement();
		// 如果 不是当前显示项目
		if (!this.id.equals(card.getCurrentItem())) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			if (tmpScript != null)
				dsScript.append(tmpScript);
		} else {
			if (tmpScript != null)
				dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$card_" + id + "$tmpScript");
		tmpBuf.append("\n}\n");
		return tmpBuf.toString();
	}

	/**
	 * 2011-8-2 下午08:09:28 renxh des：获得当前的条目的index
	 * 
	 * @return
	 */
	private int getCurrentItemIndex() {
		UICardPanel cardPanel = this.getUiElement();
		PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getParentRender();
		UICardLayout cardLayout = (UICardLayout) parentRender.getUiElement();
		List<UILayoutPanel> panelList = cardLayout.getPanelList();
		return panelList.indexOf(cardPanel);
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_CARDPANEL;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
//		buf.append(getNewDivId()).append(".style.position = 'absolute';\n");
		buf.append(getNewDivId()).append(".style.position = 'relative';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}

		return buf.toString();
	}

}
