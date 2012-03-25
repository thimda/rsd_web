package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
/**
 * 布局标签
 * @author dengjt
 * 2007-1-25
 * TODO Grid Layout
 */
public class LayoutTag extends WebElementTag implements IContainerElementTag{
	public static final String BORDER = "border";
	public static final String FLOWV = "flowv";
	public static final String FLOWH = "flowh";
	public static final String CARD = "card";
	public static final String GRID = "grid";
	
	//用来标识BorderLayout中是否有左右
	protected static final String PARTS_LEFT = "PARTS_LEFT";
	protected static final String PARTS_RIGHT = "PARTS_RIGHT";
	protected static final String PARTS_CENTER = "PARTS_CENTER";
	protected static final String PARTS_TOP = "PARTS_TOP";
	protected static final String PARTS_BOTTOM = "PARTS_BOTTOM";
	protected static final String REPLACE_STR = "$REPLACE_STR$";
	
	private String type = BORDER;
	
	//卡片布局ID,仅用于卡片
	private String cardId;
	
	private int cellSpacing = 0;
	private int cellPadding = 0;
	private int border = 0;
	private boolean dragable = false;
	//用来完成CardPanel的计数
	protected int cardCount = 0;
	
	//Grid Layout的行数
	private int colCount = 1;
	//Grid Layout的列数
	private int rowCount = 1;
	
	//初始时显示的页的顺序
	private int cardIndex = 0;
	
	//Grid panelCount 计数，仅用于Grid布局
	protected int panelCount = 0;
	protected void doRender() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter out = jspContext.getOut();
		out.write(generateHead());
		
		getJspBody().invoke(out);
		if(type.equals(BORDER))
		{
			StringBuffer buf = new StringBuffer();
			if(getAttribute(PARTS_TOP) != null)
			{
				buf.append("<tr>\n");
				buf.append(getAttribute(PARTS_TOP));
				buf.append("</tr>\n");
			}
			buf.append("<tr height=\"100%\">\n");
			int colCount = 3;
			if(getAttribute(PARTS_LEFT) != null)
			{
				colCount --;
				buf.append(getAttribute(PARTS_LEFT));
			}
			if(getAttribute(PARTS_RIGHT) != null)
			{
				colCount --;
			}
			buf.append(((String)getAttribute(PARTS_CENTER)).replace(REPLACE_STR, "colSpan=\"" + colCount + "\""));
			if(getAttribute(PARTS_RIGHT) != null)
				buf.append(getAttribute(PARTS_RIGHT));
			
			buf.append("</tr>\n");
			
			if(getAttribute(PARTS_BOTTOM) != null)
			{
				buf.append("<tr>\n");
				buf.append(getAttribute(PARTS_BOTTOM));
				buf.append("</tr>\n");
			}
			out.write(buf.toString());
		}
		out.write(generateTail());
		String script = generateTailScript();
		addToBodyScript(script);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String generateHead()
	{
		String head = null;
		if(type.equals(BORDER) || type.equals(FLOWV) || type.equals(GRID))
		{
			head = "<table width=\"100%\" height=\"100%\" border=\"" + border + "\" cellspacing=\"" + cellSpacing + "\" cellpadding=\"" + cellPadding + "\" bordercolor=\"gray\">\n";
		}
		else if(type.equals(CARD))
		{
			if(cardId == null)
				throw new LfwRuntimeException("card id can not be null for CardLayout");
			head = "<div id=\"" + DIV_PRE + cardId + "\" style=\"width:100%;height:100%\">\n";
		}
		else if(type.equals(FLOWH))
		{
			head = "<table width=\"100%\" height=\"100%\" border=\"" + border + "\" cellspacing=\"" + cellSpacing + "\" cellpadding=\"" + cellPadding + "\">\n<tr>\n";
		}
		return head;
	}
	
	public String generateTail()
	{
		String tail = null;
		if(type.equals(BORDER) || type.equals(FLOWV) || type.equals(GRID))
			tail = "</table>\n";
		else if(type.equals(CARD))
			tail = "</div>\n";
		else if(type.equals(FLOWH) )
			tail = "</tr>\n</table>\n";
		return tail;
	}


	
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public int getCellPadding() {
		return cellPadding;
	}

	public void setCellPadding(int cellPadding) {
		this.cellPadding = cellPadding;
	}

	public int getCellSpacing() {
		return cellSpacing;
	}

	public void setCellSpacing(int cellSpacing) {
		this.cellSpacing = cellSpacing;
	}

	public boolean isDragable() {
		return dragable;
	}

	public void setDragable(boolean dragable) {
		this.dragable = dragable;
	}

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String generateHeadScript() {
		return null;
	}

	public String generateTailScript() {
		StringBuffer buf = new StringBuffer();
		if(getType().equals(CARD))
		{
			buf.append("window.")
			   .append(COMP_PRE)
			   .append(cardId)
			   .append(" = new CardLayout(")
			   .append("\"")
			   .append(cardId)
			   .append("\",")
			   .append("document.getElementById(\"")
			   .append(DIV_PRE)
			   .append(cardId)
			   .append("\"),")
			   .append(cardIndex)
			   .append(");\n");
		}
		return buf.toString();
	}

	public int getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
	
}
