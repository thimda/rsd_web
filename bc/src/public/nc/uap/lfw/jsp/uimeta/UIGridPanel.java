package nc.uap.lfw.jsp.uimeta;




/**
 * @author renxh
 * ����е�cell
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
		UpdatePair pair = new UpdatePair(CELLTYPE, cellType);
		notifyChange(UPDATE, pair);
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
		UpdatePair pair = new UpdatePair(ROWSPAN, rowSpan);
		notifyChange(UPDATE, pair);
	}
	public String getColSpan() {
		return (String)this.getAttribute(COLSPAN);
	}
	public void setColSpan(String colSpan) {
		this.setAttribute(COLSPAN, colSpan);
		UpdatePair pair = new UpdatePair(COLSPAN, colSpan);
		notifyChange(UPDATE, pair);
	}
	public String getColWidth() {
		return (String)this.getAttribute(COLWIDTH);
	}
	public void setColWidth(String colWidth) {
		this.setAttribute(COLWIDTH, colWidth);
		UpdatePair update = new UpdatePair(COLWIDTH, colWidth);
		notifyChange(UPDATE, update);
	}
	public String getColHeight() {
		return (String)this.getAttribute(COLHEIGHT);
	}
	public void setColHeight(String colHeight) {
		this.setAttribute(COLHEIGHT, colHeight);
		UpdatePair pair = new UpdatePair(COLHEIGHT, colHeight);
		notifyChange(UPDATE, pair);
	}
	public String getId() {
		return (String)this.getAttribute(ID);
	}
	public void setId(String id) {
		this.setAttribute(ID, id);
	}
}
