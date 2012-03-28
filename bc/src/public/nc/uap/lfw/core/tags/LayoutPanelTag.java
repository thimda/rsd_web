package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
/**
 * 布局项标签.
 * @author dengjt
 * 2007-1-25
 */
public class LayoutPanelTag extends WebElementTag implements IContainerElementTag{
	private static final String TOP = "top";
	private static final String BOTTOM = "bottom";
	private static final String LEFT = "left";
	private static final String RIGHT = "right";
	private static final String CENTER = "center";
	//仅用于Border Layout的Panel中
	private String position = null;
	//仅用于CardLayout的Panel中
	private String cardId = null;
	//仅用于横向排列的Panel中，比如border Layout的东西中panel. 横向流布局的Panel
	private String width = null;
	//仅用于纵向排列的Panel中，比如border Layout的上中下Panel，纵向流布局的Panel
	private String height = null;
	//横向居中（左右）left  right
	private String align = "center";
	//纵向居中（上下）top  bottom
	private String valign = "middle";
	//目前主要用于GridLayout中，放置实际panel数量超出指定数量
	//0 普通状态，1 ，行开头， 2，中止， 3，行结尾 
	private int goonState = 0;
	public void doRender() throws JspException, IOException {
		LayoutTag tag = (LayoutTag) findAncestorWithClass(this, LayoutTag.class);
		if(tag == null)
			throw new LfwRuntimeException("this tag must be included in LayoutTag");
		
		if(tag.getType().equals(LayoutTag.CARD))
		{
			tag.cardCount ++;
		}
		StringWriter writer = new StringWriter();
		writer.write(generateHead());
		//gridLayout中止渲染标识
		if(goonState == 2)
			return;
		String script = generateHeadScript();
		addToBodyScript(script);
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(writer);
		writer.write(generateTail());
		
		script = generateTailScript();
		addToBodyScript(script);
		
		//如果不是Border Layout，直接将子Panel写出到页面
		if(!tag.getType().equals(LayoutTag.BORDER))
		{
			getJspContext().getOut().write(writer.getBuffer().toString());
		}
		//Border Layout由于需要根据东西的Panel来决定中间的Panel所占列数，所以需要缓画
		else
		{
			if(position == null)
			{
				throw new LfwRuntimeException("attritute position must be asigned explicitedly");
			}
			if(position.equals(TOP))
			{
				tag.setAttribute(LayoutTag.PARTS_TOP, writer.getBuffer().toString());
			}
			else if(position.equals(BOTTOM))
			{
				tag.setAttribute(LayoutTag.PARTS_BOTTOM, writer.getBuffer().toString());
			}
			else if(position.equals(LEFT))
			{
				tag.setAttribute(LayoutTag.PARTS_LEFT, writer.getBuffer().toString());
			}
			else if(position.equals(RIGHT))
			{
				tag.setAttribute(LayoutTag.PARTS_RIGHT, writer.getBuffer().toString());
			}
			else if(position.equals(CENTER))
			{
				tag.setAttribute(LayoutTag.PARTS_CENTER, writer.getBuffer().toString());
			}
		}
	}
	
	/**
	 * 产生标签头部代码
	 * 
	 */
	public String generateHead()
	{
		LayoutTag parent = (LayoutTag) findAncestorWithClass(this, LayoutTag.class);
		StringBuffer strBuf = new StringBuffer();
		if(parent.getType().equals(LayoutTag.BORDER))
		{
			
			if(position.equals(TOP))
			{
				strBuf.append("<td colspan=\"3\" align=\"" + align + "\" valign=\"" + valign + "\"");
				if(height != null)
				{
					strBuf.append(" height=\"")
					      .append(height)
					      .append("\"");
				}
				strBuf.append(">\n");
			}
			else if(position.equals(LEFT))
			{
				strBuf.append("<td colspan=\"1\" align=\"" + align + "\" valign=\"" + valign + "\"");
				if(width != null)
				{
					strBuf.append(" width=\"")
					      .append(width)
					      .append("\"");
				}
				strBuf.append(">\n");
			}
			else if(position.equals(RIGHT))
			{
				strBuf.append("<td colspan=\"1\" align=\"" + align + "\" valign=\"" + valign + "\"");
				if(width != null)
				{
					strBuf.append(" width=\"")
					      .append(width)
					      .append("\"");
				}
				strBuf.append(">\n");
			}
			else if(position.equals(BOTTOM))
			{
				strBuf.append("<td colspan=\"3\" align=\"" + align + "\" valign=\"" + valign + "\"");
				if(height != null)
				{
					strBuf.append(" height=\"")
					      .append(height)
					      .append("\"");
				}
				strBuf.append(">\n");
			}
			//center的跨度是动态决定的，所以需要设置一个替换标志符
			else if(position.equals(CENTER))
			{
				strBuf.append("<td width=\"100%\" " + LayoutTag.REPLACE_STR  + " align=\"" + align + "\" valign=\"" + valign + "\"");
				if(height != null)
				{
					strBuf.append(" height=\"")
					      .append(height)
					      .append("\"");
				}
				strBuf.append(">\n");
			}
		}
		else if(parent.getType().equals(LayoutTag.CARD))
		{
			strBuf.append("<div id=\"" + cardId + "\" style=\"width:100%;height:100%;\">\n");
		}
		else if(parent.getType().equals(LayoutTag.FLOWH))
		{
			strBuf.append("<td align=\"" + align + "\" valign=\"" + valign + "\"");
			if(width != null)
			{
				strBuf.append(" width=\"")
				      .append(width)
				      .append("\"");
			}
			strBuf.append(">\n");
		}
		else if(parent.getType().equals(LayoutTag.FLOWV))
		{
			strBuf.append("<tr>\n<td align=\"" + align + "\" valign=\"" + valign + "\"");
			if(height != null)
			{
				strBuf.append(" height=\"")
				      .append(height)
				      .append("\"");
			}
			strBuf.append(">\n");
		}
		else if(parent.getType().equals(LayoutTag.GRID))
		{
			int nowCount = parent.panelCount;
			int colCount = parent.getColCount();
			int rowCount = parent.getRowCount();
			if((nowCount / colCount + 1) > rowCount )
				//不继续进行
				goonState = 2;
			if(goonState != 2)
			{
				//行开始
				if(nowCount % colCount == 0){
					goonState = 1;
					strBuf.append("<tr>\n<td align=\"" + align + "\" valign=\"" + valign + "\">\n");
				}
				else{
					//行结尾
					if(nowCount % colCount == (colCount - 1))
						goonState = 3;
					else
						goonState = 0;
					strBuf.append("<td align=\"" + align + "\" valign=\"" + valign + "\">\n");
				}
			}
			parent.panelCount ++;
		}
		return strBuf.toString();
	}
	
	public String generateTail()
	{
		LayoutTag parent = (LayoutTag) findAncestorWithClass(this, LayoutTag.class);
		StringBuffer strBuf = new StringBuffer();
		if(parent.getType().equals(LayoutTag.BORDER))
		{
			strBuf.append("</td>\n");
		}
		else if(parent.getType().equals(LayoutTag.CARD))
		{
			strBuf.append("</div>\n");
		}
		else if(parent.getType().equals(LayoutTag.FLOWH))
		{
			strBuf.append("</td>\n");
		}
		else if(parent.getType().equals(LayoutTag.FLOWV))
		{
			strBuf.append("</td>\n</tr>\n");
		}
		else if(parent.getType().equals(LayoutTag.GRID))
		{
			if(goonState != 2){
				if(goonState == 3)
					strBuf.append("</td>\n</tr>\n");
				else
					strBuf.append("</td>");
			}
		}
		return strBuf.toString();
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}


	public String getCardId() {
		return cardId;
	}


	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


	/**
	 * 为CardLayout产生对应的js起始代码
	 */
	public String generateHeadScript() {
		LayoutTag parent = (LayoutTag) findAncestorWithClass(this, LayoutTag.class);
		if(parent.getType().equals(LayoutTag.CARD))
		{
			//获取记录当前子Item的Index
			Integer itemIndex = (Integer) parent.getAttribute("itemIndex");
			if(itemIndex == null)
			{
				itemIndex = Integer.valueOf(0);
				parent.setAttribute("itemIndex", itemIndex);
			}
			StringBuffer buf = new StringBuffer();
			buf.append("window.$")
			   .append(parent.getCardId())
			   .append("_item")
			   .append(itemIndex)
			   .append(" = function(){\n");
			
			itemIndex ++;
			parent.setAttribute("itemIndex", (itemIndex ++));
			
			//将已有的脚本暂存在临时变量中
			StringBuffer dsScript = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
			getJspContext().setAttribute("$card_" + cardId + "$tmpScript", dsScript.toString());
			dsScript.delete(0, dsScript.length());
			
			return buf.toString();
		}
		return null;
	}


	/**
	 * 为CardLayout产生对应的js结束代码
	 */
	public String generateTailScript() {
		LayoutTag parent = (LayoutTag) findAncestorWithClass(this, LayoutTag.class);
		if(parent.getType().equals(LayoutTag.CARD))
		{
			StringBuffer tmpBuf = new StringBuffer();
			StringBuffer dsScript = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
			String tmpScript = (String) getJspContext().getAttribute("$card_" + cardId + "$tmpScript");
			//如果是当前显示项目
			if(parent.cardCount != 1)
			{
				//将dsScript中的内容写入页面，并恢复原来的脚本
				tmpBuf.append(dsScript.toString());
				dsScript.delete(0, dsScript.length());
				if(tmpScript != null)
					dsScript.append(tmpScript);
			}
			else
			{
				if(tmpScript != null)
					dsScript.insert(0, tmpScript);
			}
			getJspContext().removeAttribute("$card_" + cardId + "$tmpScript");
			tmpBuf.append("\n}\n");
			return tmpBuf.toString();
		}
		return null;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getValign() {
		return valign;
	}

	public void setValign(String valign) {
		this.valign = valign;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
	
	
}
