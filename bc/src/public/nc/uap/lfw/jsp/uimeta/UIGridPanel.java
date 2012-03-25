package nc.uap.lfw.jsp.uimeta;



/**
 * @author renxh
 * 表格中的cell
 *
 */
public class UIGridPanel extends UILayoutPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String ROWSPAN = "rowSpan";
	public static final String COLSPAN = "colSpan";
	public static final String COLWIDTH = "colWidth";
	public static final String COLHEIGHT = "colHeight";	
	/**
	 * 0  表示第一个点 1 表示第二个点 2 表示第三个点 三个一组
	 * */ 
	public static final String CELLTYPE = "cellType";
	
	private UIGridRowLayout parent;
	
//	private UIGridPanel preCell; // 前一个兄弟节点
//	private UIGridPanel nextCell; // 后一个兄弟节点
	
	
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
