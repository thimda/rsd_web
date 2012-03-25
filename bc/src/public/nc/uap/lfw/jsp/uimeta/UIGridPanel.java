package nc.uap.lfw.jsp.uimeta;



/**
 * @author renxh
 * �����е�cell
 *
 */
public class UIGridPanel extends UILayoutPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String ROWSPAN = "rowSpan";
	public static final String COLSPAN = "colSpan";
	public static final String COLWIDTH = "colWidth";
	public static final String COLHEIGHT = "colHeight";	
	/**
	 * 0  ��ʾ��һ���� 1 ��ʾ�ڶ����� 2 ��ʾ�������� ����һ��
	 * */ 
	public static final String CELLTYPE = "cellType";
	
	private UIGridRowLayout parent;
	
//	private UIGridPanel preCell; // ǰһ���ֵܽڵ�
//	private UIGridPanel nextCell; // ��һ���ֵܽڵ�
	
	
	public void setCellType(String cellType){
		this.setAttribute(CELLTYPE, cellType);
	}
	
	public String getCellType(){
		return (String)this.getAttribute(CELLTYPE);
	}
	
	public UIGridRowLayout getParent() {
		return parent;
	}
	public void setParent(UIGridRowLayout parent) {
		this.parent = parent;
	}
	public String getRowSpan() {
		return (String)this.getAttribute(ROWSPAN);
	}
	public void setRowSpan(String rowSpan) {
		this.setAttribute(ROWSPAN, rowSpan);
	}
	public String getColSpan() {
		return (String)this.getAttribute(COLSPAN);
	}
	public void setColSpan(String colSpan) {
		this.setAttribute(COLSPAN, colSpan);
	}
	public String getColWidth() {
		return (String)this.getAttribute(COLWIDTH);
	}
	public void setColWidth(String colWidth) {
		this.setAttribute(COLWIDTH, colWidth);
	}
	public String getColHeight() {
		return (String)this.getAttribute(COLHEIGHT);
	}
	public void setColHeight(String colHeight) {
		this.setAttribute(COLHEIGHT, colHeight);
	}
	public String getId() {
		return (String)this.getAttribute(ID);
	}
	public void setId(String id) {
		this.setAttribute(ID, id);
	}
}