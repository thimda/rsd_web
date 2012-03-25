package nc.uap.lfw.jsp.uimeta;

import java.util.ArrayList;
import java.util.Iterator;

public class UIGridRowLayout extends UILayout {
	private static final long serialVersionUID = 4682570170026288924L;

	public static final String ROWHEIGHT = "rowHeight";
	public static final String COLCOUNT = "colcount";

	private UIGridLayout parent;

	public String getRowHeight() {
		return (String) this.getAttribute(ROWHEIGHT);
	}

	public void setRowHeight(String rowHeight) {
		this.setAttribute(ROWHEIGHT, rowHeight);
	}

	public Integer getColcount() {
		return (Integer) this.getAttribute(COLCOUNT);
	}

	public void setColcount(int colcount) {
		this.setAttribute(COLCOUNT, colcount);
	}

	public UIGridLayout getParent() {
		return parent;
	}

	public void setParent(UIGridLayout parent) {
		this.parent = parent;
		if (parent != null)
			this.setColcount(parent.getColcount());
	}
	
	@Override
	public void addPanel(UILayoutPanel panel) {
		UIGridPanel cell = (UIGridPanel) panel;
		panelList.add(cell);
		cell.setParent(this);
	}
	
//	@Override
//	public void removeElement(UIElement ele) {
//		if (ele != null) {
//			if (panelList != null) {
//				for (UILayoutPanel panel : panelList) {
//					if (panel.getAttribute(ID).equals(ele.getAttribute(ID))) {
//						this.notifyChange(DELETE, panel);
//						if (LifeCyclePhase.ajax.equals(getPhase())) {
//							this.removeOtherElement((UIElement) ele.doClone());
//						}
//						updateCell((UIGridPanel) panel);
//						panelList.remove(panel);
//						break;
//					}
//				}
//			}
//		}
//	}

	/**
	 * 更新兄弟节点的cell 合并单元格操作，如果存在前面的节点，则向前面的节点合并，否则向后面的节点合并
	 * 
	 * @param cell
	 */
	private void updateCell(UIGridPanel cell) {
//		UIGridPanel preCell = cell.getPreCell();
//		UIGridPanel nextCell = cell.getNextCell();
//
//		if (nextCell != null) {
//			nextCell.setPreCell(preCell);
//		}
//		if (preCell != null) {
//			preCell.setNextCell(nextCell);
//		}
//
//		if (preCell != null) {
//			Integer colspan = 0;
//			if (cell.getColSpan() == null || cell.getColSpan().equals("")) {
//				colspan = colspan + 1;
//			} else {
//				colspan = colspan + Integer.parseInt(cell.getColSpan());
//			}
//
//			if (preCell.getColSpan() == null || preCell.getColSpan().equals("")) {
//				colspan = colspan + 1;
//			} else {
//				colspan = colspan + Integer.parseInt(preCell.getColSpan());
//			}
//			preCell.setColSpan(colspan+"");
//			return;
//		}
//
//		if (nextCell != null) {
//			Integer colspan = 0;
//			if (cell.getColSpan() == null || cell.getColSpan().equals("")) {
//				colspan = colspan + 1;
//			} else {
//				colspan = colspan + Integer.parseInt(cell.getColSpan());
//			}
//
//			if (nextCell.getColSpan() == null || nextCell.getColSpan().equals("")) {
//				colspan = colspan + 1;
//			} else {
//				colspan = colspan + Integer.parseInt(nextCell.getColSpan());
//			}
//			nextCell.setColSpan(colspan+"");
//			return;
//		}
	}

	@Override
	public UIGridRowLayout doClone() {
		UIGridRowLayout layout = (UIGridRowLayout) super.doClone();
		if (this.panelList != null) {
			layout.panelList = new ArrayList<UILayoutPanel>();
			Iterator<UILayoutPanel> panels = this.panelList.iterator();
			while (panels.hasNext()) {
				UILayoutPanel panel = panels.next();
				layout.addPanel((UILayoutPanel) panel.doClone());
			}
		}
		return layout;
	}

}
