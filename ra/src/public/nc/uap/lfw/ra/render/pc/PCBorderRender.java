package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh border布局，只包含一个panel
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCBorderRender extends UILayoutRender<UIBorder, WebElement> {
	// 边框基础字符串
	protected static final String BORDER_ID_BASE = "border_";

	private String color = LfwRuntimeEnvironment.getTheme().getThemeElement(LfwTheme.LFW_BORDER_COLOR);
	private String leftColor = null;
	private String rightColor = null;
	private String topColor = null;
	private String bottomColor = null;

	private int width = 0;
	private int leftWidth = -1;
	private int rightWidth = -1;
	private int topWidth = -1;
	private int bottomWidth = -1;

	private boolean showLeft = true;
	private boolean showRight = true;
	private boolean showTop = true;
	private boolean showBottom = true;

	// div的class属性
	private String className = null;
	public PCBorderRender(final UIBorder uiEle, final UIMeta uimeta, final PageMeta pageMeta, final UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		final UIBorder border = this.getUiElement();
		divId = DIV_PRE + (widget == null || widget.equals("") ? "" : (widget + "_")) + id;
		this.leftColor = border.getLeftColor();
		this.rightColor = border.getRightColor();
		this.topColor = border.getTopColor();
		this.bottomColor = border.getBottomColor();
	
		this.width = getInteger(border.getWidth());
		this.leftWidth = getInteger(border.getLeftWidth());
		this.rightWidth = getInteger(border.getRightWidth());
	
		this.topWidth = getInteger(border.getTopWidth());
	
		this.bottomWidth = getInteger(border.getBottomWidth());
		
		this.showLeft = border.getShowLeft() == 0 ? true : false;
		this.showRight = border.getShowRight() == 0 ? true : false;
		this.showTop = border.getShowTop() == 0 ? true : false;
//		this.roundBorder = border.getRoundBorder() == 0 ? true : false;
		this.showBottom = border.getShowBottom() == 0 ? true : false;
		this.className = border.getClassName();

	}
	
	private int getInteger(final String str) {
		if(str == null || str.equals(""))
			return 0;
		return Integer.parseInt(str);
	}

	

	public String generalHeadHtml() {

//		StringBuffer head = new StringBuffer();
//
//		head.append("<div id=\"").append(getNewDivId()).append("\" style=\"width:100%;height:100%;overflow:hidden;\">\n");
//		head.append(this.generalEditableHeadHtml());
//		head.append("<div id=\"").append(divId).append("_top\" style=\"overflow:hidden;width:100%;");
//		if (showTop && (topWidth > 0)) {
//
//			head.append("height:").append(topWidth).append("px;");
//			head.append("background:").append(topColor == null ? color : topColor).append(";\"></div>\n");
//
//		} else if (showTop && width > 0) {
//
//			head.append("height:").append(width).append("px;");
//			head.append("background:").append(topColor == null ? color : topColor).append(";\"></div>\n");
//
//		} else {
//			head.append("height:").append(0).append("px;").append("\"></div>\n");
//		}
//
//		head.append("<div id=\"").append(divId).append("_middle\" style=\"width:100%;clear:both;overflow:hidden;\">\n");
//
//		head.append("<div id=\"").append(divId).append("_left\" style=\"float:left;height:100%;");
//		if (showLeft && (leftWidth > 0)) {
//			head.append("width:").append(leftWidth).append("px;");
//			head.append("background:").append(leftColor == null ? color : leftColor).append(";\"></div>\n");
//
//		} else if (showLeft && width > 0) {
//			head.append("width:").append(width).append("px;");
//			head.append("background:").append(leftColor == null ? color : leftColor).append(";\"></div>\n");
//		} else {
//			head.append("width:").append(0).append("px;").append("\"></div>\n");
//		}
//
//		head.append("<div id=\"").append(divId).append("_center\" style=\"float:left;height:100%;\">\n");
		
//		return head.toString();
		final StringBuffer head = new StringBuffer();
		final String height = "height:100%;";
//		if(isFlowMode())
//			height = "";
//		String parentDivId = this.getDivId();
//		String classStr = "";
//		if(roundBorder){
//			classStr = " class='roundcorner'";
//		}
		head.append("<div id='").append(getNewDivId()).append("' style='" + height + "position:relative;'>\n");
		head.append(this.generalEditableHeadHtml());
		return head.toString();
	}

	public String generalHeadScript() {
//		
//		StringBuffer buf = new StringBuffer();
//		buf.append("addResizeEvent($ge(\"").append(getDivId()).append("\"), borderResize);\n");
//		buf.append("borderResize.call($ge(\"").append(getDivId()).append("\"));\n");
//		return buf.toString();
		return "";
	}

	public String generalTailHtml() {

//		StringBuffer tail = new StringBuffer();
//		tail.append("</div>\n");
//		tail.append("<div id=\"").append(divId).append("_right\" style=\"float:right;height:100%;");
//		if (showRight && (rightWidth > 0)) {
//			tail.append("width:").append(rightWidth).append("px;").append("background:").append(rightColor == null ? color : rightColor).append(";\"></div>\n");
//		} else if (showRight && width > 0) {
//			tail.append("width:").append(width).append("px;").append("background:").append(rightColor == null ? color : rightColor).append(";\"></div>\n");
//		} else {
//			tail.append("width:").append(0).append("px;").append("\"></div>\n");
//		}
//
//		tail.append("</div>\n");
//
//		tail.append("<div id=\"").append(divId).append("_bottom\" style=\"overflow:hidden;width:100%;");
//		if (showBottom && (bottomWidth > 0)) {
//			tail.append("height:").append(bottomWidth).append("px;").append("background:").append(bottomColor == null ? color : bottomColor).append(";\"></div>\n");
//		} else if (showBottom && width > 0) {
//			tail.append("height:").append(width).append("px;").append("background:").append(bottomColor == null ? color : bottomColor).append(";\"></div>\n");
//		} else {
//			tail.append("height:").append(0).append("px;").append("\"></div>\n");
//		}
//		tail.append(this.generalEditableTailHtml());
//		tail.append("</div>\n");
//
//		return tail.toString();
		final StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	@Override
	public void notifyRemoveChild(final UIMeta uiMeta, final PageMeta pageMeta, final Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(final UIMeta uimeta, final PageMeta pagemeta, final Object obj) {
		super.notifyDestroy(uimeta, pagemeta, obj);
	}

	public String generalTailScript() {

		return "";
	}

	public String getColor() {
		return color;
	}

	public void setColor(final String color) {
		this.color = color;
	}

	public String getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(final String leftColor) {
		this.leftColor = leftColor;
	}

	public String getRightColor() {
		return rightColor;
	}

	public void setRightColor(final String rightColor) {
		this.rightColor = rightColor;
	}

	public String getTopColor() {
		return topColor;
	}

	public void setTopColor(final String topColor) {
		this.topColor = topColor;
	}

	public String getBottomColor() {
		return bottomColor;
	}

	public void setBottomColor(final String bottomColor) {
		this.bottomColor = bottomColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getLeftWidth() {
		return leftWidth;
	}

	public void setLeftWidth(final int leftWidth) {
		this.leftWidth = leftWidth;
	}

	public int getRightWidth() {
		return rightWidth;
	}

	public void setRightWidth(final int rightWidth) {
		this.rightWidth = rightWidth;
	}

	public int getTopWidth() {
		return topWidth;
	}

	public void setTopWidth(final int topWidth) {
		this.topWidth = topWidth;
	}

	public int getBottomWidth() {
		return bottomWidth;
	}

	public void setBottomWidth(final int bottomWidth) {
		this.bottomWidth = bottomWidth;
	}

	public boolean isShowLeft() {
		return showLeft;
	}

	public void setShowLeft(final boolean showLeft) {
		this.showLeft = showLeft;
	}

	public boolean isShowRight() {
		return showRight;
	}

	public void setShowRight(final boolean showRight) {
		this.showRight = showRight;
	}

	public boolean isShowTop() {
		return showTop;
	}

	public void setShowTop(final boolean showTop) {
		this.showTop = showTop;
	}

	public boolean isShowBottom() {
		return showBottom;
	}

	public void setShowBottom(final boolean showBottom) {
		this.showBottom = showBottom;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(final String className) {
		this.className = className;
	}

	protected String getSourceType(final IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_BORDER;
	}

	@Override
	public String generalHeadHtmlDynamic() {
//		StringBuffer buf = new StringBuffer();
//
//		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
//		buf.append(getNewDivId()).append(".style.width = '100%';\n");
//		buf.append(getNewDivId()).append(".style.height = '100%';\n");
//		buf.append(getNewDivId()).append(".style.overflow = 'hidden';\n");
//		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");		
//		if(this.isEditMode()){
//			buf.append(this.generalEditableHeadHtmlDynamic());
//			buf.append(getNewDivId()).append(".appendChild("+getDivId()+");\n");
//		}		
//		// top
//		String top = getDivId() + "_top";
//		buf.append("var ").append(top).append(" = $ce('DIV');\n");
//		buf.append(top).append(".style.width = '100%';\n");
//		buf.append(top).append(".style.overflow = 'hidden';\n");
//		buf.append(top).append(".id = '" + top + "';\n");
//		if (showTop && (topWidth > 0)) {
//			buf.append(top).append(".style.height = '").append(topWidth).append("px';\n");
//			buf.append(top).append(".style.background = '").append(topColor == null ? color : topColor).append("';\n");
//		} else if (showTop && width > 0) {
//			buf.append(top).append(".style.height ='").append(width).append("px';\n");
//			buf.append(top).append(".style.background = '").append(topColor == null ? color : topColor).append("';\n");
//		} else {
//			buf.append(top).append(".style.height = '").append(0).append("px';\n");
//		}
//		buf.append(getDivId()).append(".appendChild(" + top + ");\n");
//
//		// middle
//		String middle = getDivId() + "_middle";
//		buf.append("var ").append(middle).append(" = $ce('DIV');\n");
//		buf.append(middle).append(".style.width = '100%';\n");
//		buf.append(middle).append(".style.clear = 'both';\n");
//		buf.append(middle).append(".style.overflow = 'hidden';\n");
//		buf.append(middle).append(".id = '" + middle + "';\n");
//		buf.append(getDivId()).append(".appendChild(" + middle + ");\n");
//
//		// middle - left
//		String left = getDivId() + "_left";
//		buf.append("var ").append(left).append(" = $ce('DIV');\n");
//		buf.append("if(IS_IE){");
//		buf.append(left).append(".style.styleFloat = 'left';\n");
//		buf.append("}else{");
//		buf.append(left).append(".style.cssFloat = 'left';\n");
//		buf.append("};\n");
//		buf.append(left).append(".style.height = '100%';\n");
//		buf.append(left).append(".id = '" + left + "';\n");
//		if (showLeft && (leftWidth > 0)) {
//			buf.append(left).append(".style.width = '").append(leftWidth).append("px';\n");
//			buf.append(left).append(".style.background = '").append(leftColor == null ? color : leftColor).append("';\n");
//
//		} else if (showLeft && width > 0) {
//			buf.append(left).append(".style.width = '").append(width).append("px';\n");
//			buf.append(left).append(".style.background = '").append(leftColor == null ? color : leftColor).append("';\n");
//		} else {
//			buf.append(left).append(".style.width = '").append(0).append("px';\n");
//		}
//		buf.append(middle).append(".appendChild(" + left + ");\n");
//
//		// middle - center
//		String center = getDivId() + "_center";
//		buf.append("var ").append(center).append(" = $ce('DIV');\n");
//		buf.append("if(IS_IE){");
//		buf.append(center).append(".style.styleFloat = 'left';\n");
//		buf.append("}else{");
//		buf.append(center).append(".style.cssFloat = 'left';\n");
//		buf.append("};\n");
//		buf.append(center).append(".style.height = '100%';\n");
//		buf.append(center).append(".id = '" + center + "';\n");
//		buf.append(middle).append(".appendChild(" + center + ");\n");
//		return buf.toString();
		final StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.left = '0px';\n");
		buf.append(getNewDivId()).append(".style.position = 'relative';\n");
//		buf.append(getNewDivId()).append(".style.border = '1px solid red';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
//		
//		if (height != null) {
//			buf.append(getNewDivId()).append(".style.height = '"+height+"';\n");
//			buf.append(getNewDivId()).append(".setAttribute('hasheight','1');\n");
//		} else {
//			buf.append(getNewDivId()).append(".setAttribute('hasheight','0');\n");
////			buf.append(getNewDivId()).append(".style.minHeight = '30px';\n");
//		}
//		if (anchor != null)
//			buf.append(getNewDivId()).append(".anchor='").append(anchor).append("';\n");
		
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
		
	}
	
	public String generalTailHtmlDynamic(){
		return "";
	}


	@Override
	public void notifyAddChild(final UIMeta uiMeta, final PageMeta pageMeta, final Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

}
