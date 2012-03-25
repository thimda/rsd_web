package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * FLOW-V 布局标签
 * @author guoweic
 * 2009-10-19
 */
public class GridLayoutTag extends WebElementTag implements
		IContainerElementTag {
	
	// Grid 布局ID基础字符串
	protected static final String GRID_ID_BASE = "grid_layout_";
	
	// 布局ID
	private String id;
	
	// 行数
	private int rowcount = 0;
	
	// 列数
	private int colcount = 0;

	// 生成DIV的ID
	private String divId;
	
	// 用来完成GridPanel的计数
	protected int childCount = 0;
	
	// 记录子项中每列宽度
	protected Map colWidth = new HashMap();

	protected void doRender() throws JspException, IOException {
		if (rowcount == 0) {
			throw new LfwRuntimeException("attritute rowcount must be asigned explicitedly");
		}
		if (colcount == 0) {
			throw new LfwRuntimeException("attritute colcount must be asigned explicitedly");
		}
		JspContext jspContext = getJspContext();
		JspWriter out = jspContext.getOut();
		out.write(generateHead());
		// 渲染子项之前加入脚本
		String script = generateTailScript();
		addToBodyScript(script);
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(out);
		out.write(generateTail());
		
	}

	public String generateHead() {
		StringBuffer head = new StringBuffer();
		if (id == null) {
			id = (String)LfwRuntimeEnvironment.getWebContext().getAttribute("$gridLayoutId");
			if (id == null) {
				id = "0";
			} else {
				id = String.valueOf(Integer.parseInt(id) + 1);
			}
		}
		LfwRuntimeEnvironment.getWebContext().setAttribute("$gridLayoutId", id);
		divId = DIV_PRE + GRID_ID_BASE + id;
		
		head.append("<div id=\"")
			.append(divId)
			.append("\" style=\"width:100%;height:100%;overflow:hidden;\" rowcount=\"")
			.append(rowcount)
			.append("\" colcount=\"")
			.append(colcount)
			.append("\">\n");
		
		return head.toString();
	}

	public String generateHeadScript() {
		return null;
	}

	public String generateTail() {
		return "</div>\n";
	}
	
	public String generateTailScript() {
		StringBuffer buf = new StringBuffer();
		buf.append("addResizeEvent($ge(\"")
		   .append(divId)
		   .append("\"), gridLayoutResize);\n")
		   .append("gridLayoutResize.call($ge(\"")
		   .append(divId)
		   .append("\"));\n");
		return buf.toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDivId() {
		return divId;
	}
	
	public int getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
	}

	public int getColcount() {
		return colcount;
	}

	public void setColcount(int colcount) {
		this.colcount = colcount;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
