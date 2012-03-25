package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 分割面板布局渲染器
 */
public class PCSpliterLayoutRender extends UILayoutRender<UISplitter, WebElement> {
	private String divideSize = "0.5";
	private String orientation = "v";
	private String boundMode = "%";
	private boolean oneTouch = false;
	private int spliterWidth = 4;
	private boolean inverse = false;
	// 是否隐藏拖动条
	private boolean hideBar = true;

	// 隐藏方向（true-向左/向上；false-向右/向下）
	private boolean hideDirection = true;
	public PCSpliterLayoutRender(UISplitter uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UISplitter spliter = this.getUiElement();
		this.id = spliter.getId();
		this.widget = spliter.getWidgetId() == null ? "pagemeta" : spliter.getWidgetId();
		this.orientation = "h";
		if (UISplitter.ORIENTATION_H.equals(spliter.getOrientation())) {
			this.orientation = "h";
		} 
		else {
			this.orientation = "v";
		}
		
		this.boundMode = "%";
		if (spliter.getBoundMode() != null){
			if (UISplitter.BOUNDMODE_PERC.equals(spliter.getBoundMode()))
				this.boundMode = "%";
			else if (spliter.getBoundMode() == 1) {
				this.boundMode = "px";
			}
		}
		
		if (spliter.getDivideSize() != null){
			if (this.boundMode.equals("%"))
					this.divideSize = "0." + spliter.getDivideSize();
			else
				this.divideSize = spliter.getDivideSize();
		}
		if (spliter.getInverse() != null)
			this.inverse = UIConstant.TRUE.equals(spliter.getInverse());
//		try {
//			this.spliterWidth = Integer.parseInt(spliter.getSpliterWidth());
//		} catch (Exception e) {
//			LfwLogger.error(e);
//		}
//		if (spliter.getHideBar() == 1) {
//			this.hideBar = true;
//		} else {
//			this.hideBar = false;
//		}

		if (UIConstant.TRUE.equals(spliter.getOneTouch())) {
			this.oneTouch = true;
		} else {
			this.oneTouch = false;
		}

		this.divId = DIV_PRE + this.getId(); // + getUniqueId(DIV_INDEX); 
		this.varId = COMP_PRE + this.widget + "_" + getId();
	}

//	private static final String P_SUFFIX = "_p";

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;height:100%;position:relative;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {
		StringBuffer buf = new StringBuffer();
		String divId = getDivId();
		String spliterId = divId + "_spliter";
		buf.append("window.").append(getVarId()).append(" = new SpliterComp(document.getElementById(\"");
		buf.append(divId).append("\"), \"");
		buf.append(spliterId).append("\", 0, 0, \"100%\", \"100%\", \"relative\", \"");
		buf.append(divideSize).append("\", \"");
		buf.append(orientation).append("\", ");
		buf.append(oneTouch).append(", ");
		buf.append("{spliterWidth:").append(spliterWidth);
		buf.append(", boundMode:'").append(boundMode);
		buf.append("', isInverse:").append(inverse);
		// 反方向隐藏
		if (!isHideDirection())
			buf.append(", hideDirection:false");
		// 不隐藏bar
		if (!isHideBar())
			buf.append(", hideBar:false");
		boolean isRunMode = false;
		if(LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("eclipse") == null){
			isRunMode = !LfwRuntimeEnvironment.isEditMode();
		}
		buf.append(", isRunMode:" + isRunMode);
		buf.append("},null);\n");
		return buf.toString();
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

	public String getDivideSize() {
		return divideSize;
	}

	public void setDivideSize(String divideSize) {
		this.divideSize = divideSize;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getBoundMode() {
		return boundMode;
	}

	public void setBoundMode(String boundMode) {
		this.boundMode = boundMode;
	}

	public boolean isOneTouch() {
		return oneTouch;
	}

	public void setOneTouch(boolean oneTouch) {
		this.oneTouch = oneTouch;
	}

	public int getSpliterWidth() {
		return spliterWidth;
	}

	public void setSpliterWidth(int spliterWidth) {
		this.spliterWidth = spliterWidth;
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	public boolean isHideBar() {
		return hideBar;
	}

	public void setHideBar(boolean hideBar) {
		this.hideBar = hideBar;
	}

	public boolean isHideDirection() {
		return hideDirection;
	}

	public void setHideDirection(boolean hideDirection) {
		this.hideDirection = hideDirection;
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_SPLITERLAYOUT;
	}
	
	

	@Override
	public String generalHeadHtmlDynamic() {
		
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $ce('DIV');\n");
		buf.append(newDivId).append(".style.width = '100%';\n");
		buf.append(newDivId).append(".style.height = '100%';\n");
//		buf.append(newDivId).append(".overflow = 'hidden';\n");
		buf.append(newDivId).append(".id = '" + newDivId + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(newDivId).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		
		StringBuffer buf = new StringBuffer();
		
		if(this.widget != null){
			buf.append("var curWidget = pageUI.getWidget('").append(this.getWidget()).append("');\n");
			buf.append("var ").append(getVarId()).append(" = curWidget.getSplit('").append(this.getDivId()).append("');\n");
		}
		else{
			buf.append("var ").append(getVarId()).append(" = pageUI.getSplit('").append(this.getDivId()).append("');\n");
		}
//		buf.append("debugger;\n");
		buf.append("if("+getVarId()+"){\n");
		
		buf.append("SpliterComp.spliterCompResize(").append(getVarId()).append(");\n");
		buf.append("};\n");
		addBeforeExeScript(buf.toString());
		
	}

}
