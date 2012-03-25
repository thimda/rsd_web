package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

public class PCGridPanelRender extends UILayoutPanelRender<UIGridPanel, WebElement> {

	public PCGridPanelRender(UIGridPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.colWidth = uiEle.getColWidth();
		this.colSpan = uiEle.getColSpan();
		this.rowSpan = uiEle.getRowSpan();
		this.colHeight = uiEle.getColHeight();
		this.cellType = uiEle.getCellType();

		if (this.colHeight == null && parentRender != null) {
			String rowHeight = ((PCGridRowLayoutRender) parentRender).getRowHeight();
			if (rowHeight != null) {
				this.colHeight = rowHeight;
			}
		}
		if (this.id == null || this.id.equals("")) {
			throw new LfwRuntimeException(this.getClass().getName() + ":id不能为空！");
		}
		if (parentRender != null && parentRender.getDivId() != null) {
			this.divId = parentRender.getDivId() + "_" + this.id;
		} else {
			throw new LfwRuntimeException("父渲染器不不能为空！");
		}
	}

	private String colHeight;
	private String colWidth;
	private String rowSpan;
	private String colSpan;
	private String cellType = null;
	


	public String getColWidth() {
		return colWidth;
	}

	public void setColWidth(String colWidth) {
		this.colWidth = colWidth;
	}

	public String getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(String rowSpan) {
		this.rowSpan = rowSpan;
	}

	public String getColSpan() {
		return colSpan;
	}

	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	protected String getSourceType(IEventSupport ele) {

		return LfwPageContext.SOURCE_TYPE_GRIDPANEL;
	}

	public String getColHeight() {
		return colHeight;
	}

	public void setColHeight(String colHeight) {
		this.colHeight = colHeight;
	}

	@Override
	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<td id='").append(this.getDivId()).append("' ");
		if (this.colWidth != null && !this.colWidth.equals("")) {
			buf.append(" width='").append(colWidth).append("'");
			buf.append(" haswidth='1'");
		} else {
			buf.append(" haswidth='0'");
		}
		if (this.colHeight != null && !this.colHeight.equals("")) {
			buf.append(" height='").append(colHeight).append("'");
			buf.append(" hasheight='1'");
		}else{
			buf.append(" hasheight='0'");
		}
		if (rowSpan != null) {
			buf.append(" rowspan='").append(rowSpan).append("'");
		}
		if (colSpan != null) {
			buf.append(" colspan='").append(colSpan).append("'");
		}
		if (cellType != null) {
			buf.append(" celltype='").append(cellType).append("'");
		}
		buf.append(">");
		return buf.toString();
	}

	@Override
	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("</td>");
		return buf.toString();
	}

	@Override
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuffer buf = new StringBuffer();
		IUIRender gridRender = getParentRender().getParentRender().getParentRender();
		UIGridLayout grid = gridRender.getUiElement();
		UIGridLayout.CellPair cellPair = grid.getCellPair(this.getUiElement());
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(gridRender.getId()).append("'");
		buf.append(",subuiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",subeleid:'").append(subEleId).append("'");
		buf.append(",rowindex:").append(cellPair.getRow());
		buf.append(",colindex:").append(cellPair.getCol());
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("new EditableListener(document.getElementById('" + getDivId() + "'),params,'layout');\n");
		buf.append(addDragableListener(getDivId()));
		return buf.toString();
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ce('TD');\n");
		buf.append(getDivId()).append(".id = '" + getDivId() + "';\n");
		if (this.colWidth != null && !this.colWidth.equals("")) {
			buf.append(getDivId()).append(".setAttribute('width','").append(colWidth).append("');\n");
			buf.append(getDivId()).append(".setAttribute('haswidth','1');\n");
		} else {
			buf.append(getDivId()).append(".setAttribute('haswidth','0');\n");
		}
		if (this.colHeight != null && !this.colHeight.equals("")) {
			buf.append(getDivId()).append(".setAttribute('height','").append(colHeight).append("');\n");
			buf.append(getDivId()).append(".setAttribute('hasheight','1');\n");
		}else{
			buf.append(getDivId()).append(".setAttribute('hasheight','0');\n");
		}
		if (rowSpan != null) {
			buf.append(getDivId()).append(".setAttribute('rowspan','").append(rowSpan).append("');\n");
		}
		if (colSpan != null) {
			buf.append(getDivId()).append(".setAttribute('colspan','").append(colSpan).append("');\n");
		}
		if (cellType != null) {
			buf.append(getDivId()).append(".setAttribute('celltype','").append(cellType).append("');\n");
		}
//		buf.append(getDivId()).append(".innerHTML = \"&nbsp;\";\n");
		return buf.toString();
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ge('" + getDivId() + "');\n");
		buf.append("var parent = ").append(getDivId()).append(".parentNode;\n");
		buf.append("if(parent) parent.removeChild(" + getDivId() + ");\n");
		addDynamicScript(buf.toString());
	}

	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		UIGridPanel panel = this.getUiElement();
//		this.setEditMode(true);
		if (getDivId() != null) {
			StringBuffer buf = new StringBuffer();
			buf.append("var ").append(getDivId()).append(" = $ge('" + getDivId() + "');\n");
			buf.append("if(" + getDivId() + "){");
			if (this.colWidth != null && !this.colWidth.equals("")) {
				buf.append(getDivId()).append(".setAttribute('width','").append(colWidth).append("');\n");
				buf.append(getDivId()).append(".setAttribute('haswidth','1');\n");
			} else {
				buf.append(getDivId()).append(".setAttribute('haswidth','0');\n");
			}
			if (this.colHeight != null && !this.colHeight.equals("")) {
				buf.append(getDivId()).append(".setAttribute('height','").append(colHeight).append("');\n");
				buf.append(getDivId()).append(".setAttribute('hasheight','1');\n");
			}else{
				buf.append(getDivId()).append(".setAttribute('hasheight','0');\n");
			}
			if (rowSpan != null) {
				buf.append(getDivId()).append(".setAttribute('rowspan','").append(rowSpan).append("');\n");
				if(obj != null && obj.equals("already")){
					//已经做了调整
				}
				else{
					//对所影响cell作调整
					int count = Integer.parseInt(rowSpan);
					if(count < 1) return;
					
					adjustCellByRowSpan(panel, count);
					
				}
			}
			if (colSpan != null) {
				buf.append(getDivId()).append(".setAttribute('colspan','").append(colSpan).append("');\n");
				if(obj != null && obj.equals("already")){
					//已经做了调整
				}
				else{
					//对所影响cell作调整
					int count = Integer.parseInt(colSpan);
					if(count < 1)return;
					adjustCellByColSpan(panel, count);
				}
			}
			if (cellType != null) {
				buf.append(getDivId()).append(".setAttribute('celltype','").append(cellType).append("');\n");
			}
			buf.append("}");
			addDynamicScript(buf.toString());
		}
	}

	private void adjustCellByColSpan(UIGridPanel panel, int count) {
//		UIGridPanel cell = panel.getNextCell();
//		while(count > 1 && cell != null){
//			panel.removeElement(cell);
//			cell = cell.getNextCell();
//			count--;
//		}
	}

	private void adjustCellByRowSpan(UIGridPanel panel, int count) {
//		String id = panel.getId();
//		UIGridRowLayout row = panel.getParent();
//		int x = 0;
//		
//		int iCount = 0;
//		for(int i = 0; i < row.getPanelList().size(); i++){
//			UIGridPanel p = (UIGridPanel) row.getPanelList().get(i);
//			String ic = p.getColSpan();
//			if(ic != null){
//				iCount  += Integer.parseInt(ic) - 1;
//			}
//			if(p.getId().equals(id)){
//				x = i + iCount;
//				break;
//			}
//		}
//		//获取i列所有panel
//		int y = 0;
//		List<UIGridRowLayout> gridRows = row.getParent().getGridRow();
//		List<UIGridPanel> rowPanels = new ArrayList<UIGridPanel>();
//		for(int j = 0; j < gridRows.size(); j ++){
//			UIGridRowLayout r = gridRows.get(j);
//			UIGridPanel p = null;
//			int spanCount = 0;
//			if(r.getPanelList() != null && r.getPanelList().size() > 0){
//				for(int z = 0; z < r.getPanelList().size(); z++){
//					UIGridPanel grdp = (UIGridPanel) r.getPanelList().get(z);
//					String cc = grdp.getColSpan();
//					if(cc != null)
//						spanCount += Integer.parseInt(cc) - 1;
//					
//				}
//				int temp = x - spanCount < 0 ? 0 : x-spanCount; 
//				p = (UIGridPanel) r.getPanelList().get(temp);
//			}
//			
//			if(p != null && p.getId().equals(id))
//				y = j;
//			rowPanels.add(p);
//		}
//		
//		for(int t = 1; t < count; t++){
//			
//			try {
//				UIGridPanel rowPanel = rowPanels.get(y + t);
//				if(rowPanel != null){
//					rowPanel.getParent().removeElement(rowPanel);
//					
//				}
//			} catch (Exception e) {
//				throw new LfwRuntimeException(e.getMessage(), "获取元素超出边界");
//			}
//		}
	}


	@Override
	public String generalEditableHeadScriptDynamic() {
		return "";
	}

	public String generalHeadScript() {

		return "";
	}

	public String generalTailScript() {

		return "";
	}

	public String generalEditableHeadHtml() {

		return "";
	}

	public String generalEditableHeadScript() {

		return "";
	}

	public String generalEditableTailHtml() {

		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}

}
