package nc.uap.lfw.jsp.uimeta;

import java.util.Iterator;
import java.util.List;

public class UIGridLayout extends UILayout {
	private static final long serialVersionUID = 1L;
	public static final String BORDER = "border";
	public static final String ROWCOUNT = "rowcount";
	public static final String COLCOUNT = "colcount";
	public static final String CLASSNAME = "className";
	
	public int getRowcount() {
		return (Integer)this.getAttribute(ROWCOUNT);
	}

	public void setRowcount(int rowcount) {
		this.setAttribute(ROWCOUNT, rowcount);
	}

	public int getColcount() {
		return (Integer)this.getAttribute(COLCOUNT);
	}

	public void setColcount(int colcount) {
		this.setAttribute(COLCOUNT, colcount);
	}

	public String getBorder() {
		return (String)this.getAttribute(BORDER);
	}

	public void setBorder(String border) {
		this.setAttribute(BORDER, border);
	}


	public String getWidgetId() {
		return (String)this.getAttribute(WIDGET_ID);
	}

	public void setWidgetId(String widgetId) {
		this.setAttribute(WIDGET_ID, widgetId);
	}
	
	public void setClassName(String className){
		this.setAttribute(CLASSNAME, className);
	}
	
	public String getClassName(){
		return (String)this.getAttribute(CLASSNAME);
	}
	

	public void addGridRow(UIGridRowLayout row) {
		UIGridRowPanel rowPanel = new UIGridRowPanel(row);
		rowPanel.setId(row.getId()+"Panel");
		addPanel(rowPanel);
	}
	
	public void removeGridRow(UIGridRowLayout row) {
		Iterator<UILayoutPanel> it = this.getPanelList().iterator();
		UILayoutPanel currPanel = null;
		while(it.hasNext()){
			UIGridRowPanel panel = (UIGridRowPanel) it.next();
			UIGridRowLayout layout = panel.getRow();
			if(row.getId().equals(layout.getId())){
				currPanel = panel;
				break;
			}
		}
		if(currPanel != null)
			removePanel(currPanel);
	}

	@Override
	public UIGridLayout doClone() {
		return (UIGridLayout) super.doClone();
	}

	public CellPair getCellPair(UIGridPanel panel) {
		UIGridRowLayout row = (UIGridRowLayout) panel.getLayout();
		List<UILayoutPanel> list = this.getPanelList();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			UIGridRowPanel rowPanel = (UIGridRowPanel) list.get(i);
			int index = findPanel(rowPanel, panel);
			if(index != -1)
				return new CellPair(i, index);
		}
		return null;
	}

	private int findPanel(UIGridRowPanel rowPanel, UIGridPanel panel) {
		UIGridRowLayout rowLayout = (UIGridRowLayout) rowPanel.getElement();
		List<UILayoutPanel> panelList = rowLayout.getPanelList();
		if(panelList == null)
			return -1;
		int size = panelList.size();
		for (int i = 0; i < size; i++) {
			UILayoutPanel lp = panelList.get(i);
			if(panel.equals(lp))
				return i;
		}
		return -1;
	}

	public class CellPair{
		private int col;
		private int row;
		
		public CellPair(int row, int col){
			this.col = col;
			this.row = row;
		}
		public int getCol() {
			return col;
		}
		public void setCol(int col) {
			this.col = col;
		}
		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}
	}

	public UIElement getGridCell(int rowIndex, int colIndex) {
		UIGridRowPanel panel = (UIGridRowPanel) this.getPanelList().get(rowIndex);
		return panel.getRow().getPanelList().get(colIndex);
	}
}

