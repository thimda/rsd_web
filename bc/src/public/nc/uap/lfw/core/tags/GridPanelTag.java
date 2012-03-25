package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * FLOW-H ���������ǩ
 * @author guoweic
 * 2009-10-19
 */
public class GridPanelTag extends WebElementTag implements IContainerElementTag {

	// �߶�
	private String height;
	
	// ���
	private String width;

	// ����DIV��ID
	private String divId;

	// ��Tag����
	private GridLayoutTag parentTag = null;
	
	//Ŀǰ��Ҫ����GridLayout�У�����ʵ��panel��������ָ������
	//0 ��ֹ��1 ���п�ͷ�� 2����ͨ״̬�� 3�� �н�β
	private int goonState = 2;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (GridLayoutTag) findAncestorWithClass(this, GridLayoutTag.class);
		if (parentTag == null)
			throw new LfwRuntimeException("this tag must be included in BorderLayoutTag");
		parentTag.childCount++;
		
		StringWriter writer = new StringWriter();
		writer.write(generateHead());
		//gridLayout��ֹ��Ⱦ��ʶ
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
		
		// ֱ�ӽ���Panelд����ҳ��
		getJspContext().getOut().write(writer.getBuffer().toString());

	}

	public String generateHead() {
		divId = parentTag.getDivId() + "_";
		StringBuffer strBuf = new StringBuffer();
		int nowCount = parentTag.childCount;
		int colCount = parentTag.getColcount();
		int rowCount = parentTag.getRowcount();
		if ((nowCount / colCount) > rowCount ) {
			//����������
			goonState = 0;
		}
		if (goonState != 0) {
			// �к�
			int rowNum = ((parentTag.childCount - 1) / parentTag.getColcount()) + 1;
			// �к�
			int colNum = parentTag.childCount % parentTag.getColcount();
			if (colNum == 0) 
				colNum = parentTag.getColcount();
			//�п�ʼ
			if(nowCount % colCount == 1){
				goonState = 1;
				strBuf.append("<div id=\"")
					  .append(divId)
					  .append(rowNum)
					  .append("\" ");
				if (height != null) {  // �����˸߶�
					strBuf.append("hasheight=\"1\" style=\"width:100%;height:")
						  .append(height)
						  .append(";overflow:hidden;\">\n");
				} else {
					strBuf.append("hasheight=\"0\" style=\"width:100%;overflow:hidden;\">\n");
				}
			}
			else{
				//�н�β
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
			if (rowNum == 1 && width != null) {  // ��һ��ĳ�������˿��
				parentTag.colWidth.put(colNum, width);
				strBuf.append("haswidth=\"1\" ")
					  .append("style=\"float:left;height:100%;width:")
					  .append(width)
					  .append("\">\n");
			} else if (rowNum != 1 && parentTag.colWidth.get(colNum) != null) {  // �ǵ�һ�в����п��
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
			if(goonState == 3) {  // �н�β
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
