package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * FLOW-H 布局子项标签
 * @author guoweic
 * 2009-10-19
 */
public class GridPanelTag extends WebElementTag implements IContainerElementTag {

	// 高度
	private String height;
	
	// 宽度
	private String width;

	// 生成DIV的ID
	private String divId;

	// 父Tag对象
	private GridLayoutTag parentTag = null;
	
	//目前主要用于GridLayout中，放置实际panel数量超出指定数量
	//0 中止，1 ，行开头， 2，普通状态， 3， 行结尾
	private int goonState = 2;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (GridLayoutTag) findAncestorWithClass(this, GridLayoutTag.class);
		if (parentTag == null)
			throw new LfwRuntimeException("this tag must be included in BorderLayoutTag");
		parentTag.childCount++;
		
		StringWriter writer = new StringWriter();
		writer.write(generateHead());
		//gridLayout中止渲染标识
		if(goonState == 0)
			return;
		String script = generateHeadScript();
		addToBodyScript(script);
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(writer);
		writer.write(generateTail());
		script = generateTailScript();
		addToBodyScript(script);
		
		// 直接将子Panel写出到页面
		getJspContext().getOut().write(writer.getBuffer().toString());

	}

	public String generateHead() {
		divId = parentTag.getDivId() + "_";
		StringBuffer strBuf = new StringBuffer();
		int nowCount = parentTag.childCount;
		int colCount = parentTag.getColcount();
		int rowCount = parentTag.getRowcount();
		if ((nowCount / colCount) > rowCount ) {
			//不继续进行
			goonState = 0;
		}
		if (goonState != 0) {
			// 行号
			int rowNum = ((parentTag.childCount - 1) / parentTag.getColcount()) + 1;
			// 列号
			int colNum = parentTag.childCount % parentTag.getColcount();
			if (colNum == 0) 
				colNum = parentTag.getColcount();
			//行开始
			if(nowCount % colCount == 1){
				goonState = 1;
				strBuf.append("<div id=\"")
					  .append(divId)
					  .append(rowNum)
					  .append("\" ");
				if (height != null) {  // 设置了高度
					strBuf.append("hasheight=\"1\" style=\"width:100%;height:")
						  .append(height)
						  .append(";overflow:hidden;\">\n");
				} else {
					strBuf.append("hasheight=\"0\" style=\"width:100%;overflow:hidden;\">\n");
				}
			}
			else{
				//行结尾
				if(nowCount % colCount == 0)
					goonState = 3;
				else
					goonState = 2;
			}
			strBuf.append("<div id=\"")
				  .append(divId)
				  .append(rowNum)
				  .append("_")
				  .append(colNum)
				  .append("\" ");
			if (rowNum == 1 && width != null) {  // 第一行某列设置了宽度
				parentTag.colWidth.put(colNum, width);
				strBuf.append("haswidth=\"1\" ")
					  .append("style=\"float:left;height:100%;width:")
					  .append(width)
					  .append("\">\n");
			} else if (rowNum != 1 && parentTag.colWidth.get(colNum) != null) {  // 非第一行并且有宽度
				strBuf.append("haswidth=\"1\" ")
				  .append("style=\"float:left;height:100%;width:")
				  .append(parentTag.colWidth.get(colNum))
				  .append("\">\n");
			} else {
				strBuf.append("haswidth=\"0\" ")
				  .append("style=\"float:left;height:100%;\">\n");
			}
		}
		return strBuf.toString();
	}

	public String generateHeadScript() {
		return "";
	}

	public String generateTail() {
		LayoutTag parent = (LayoutTag) findAncestorWithClass(this, LayoutTag.class);
		StringBuffer strBuf = new StringBuffer();
		if(goonState != 0){
			if(goonState == 3) {  // 行结尾
				strBuf.append("</div>\n</div>\n");
			}
			else
				strBuf.append("</div>\n");
		}
		return strBuf.toString();
	}

	public String generateTailScript() {
		return "";
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = getFormatSize(height);
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = getFormatSize(width);
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
