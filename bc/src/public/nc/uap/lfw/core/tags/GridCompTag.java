package nc.uap.lfw.core.tags;

import java.util.List;

import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * Grid标签
 * @author gd
 * 2007-02-28
 */
public class GridCompTag extends NormalComponentTag 
{
	public String generateBodyScript() 
	{
		GridComp grid = (GridComp) getComponent();
		Dataset ds = this.getDataset();
		
		List<IGridColumn> headers = grid.getColumnList();
		StringBuffer buf = new StringBuffer(); 
		if(!headers.isEmpty())
		{	
			buf.append("var ")
			   .append(getVarShowId())
			   .append(" = new GridComp(document.getElementById(\"")
			   .append(getDivShowId())
			   .append("\"),\"")
			   .append(grid.getId())
			   .append("\",")
			   .append("\"")
			   .append("0")
			   .append("\",\"")
			   .append("0")
			   .append("\",\"100%\",\"100%\",\"")
			   .append("relative")
			   .append("\",")
			   .append(grid.isEditable())
			   .append(",")
			   .append(grid.isMultiSelect())
			   .append(",")
			   .append(grid.isShowNumCol())
			   .append(",")
			   .append(grid.isShowSumRow())
			   .append(",");
			if(ds.getPageSize() == -1)
				buf.append("null,");
			else{
				buf.append("{pageSize:")
				   .append(ds.getPageSize())
				   .append("},");
			}
			if(grid.getGroupColumns() == null || grid.getGroupColumns().equals(""))
				buf.append("null");
			else
				buf.append("'" + grid.getGroupColumns() + "'");
			buf.append(",")
			   .append(grid.isSortable())
			   .append(",'")
			   .append("")
			   .append("',")
			   .append(grid.isPagenationTop())
			   .append(",")
			   .append(grid.isShowColInfo())
			   .append(",'")
			   .append(grid.getOddType())
			   .append("',");
			if (grid.getGroupColumns() != null && grid.getGroupColumns().length() > 0)   
				buf.append(true);
			else
				buf.append(false);
			buf.append(",")
			   .append(grid.isShowHeader())
			   .append(");\n");
			
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append(getVarShowId() + ".widget=" + widget + ";\n");
			buf.append(widget + ".addComponent(" + getVarShowId() + ");\n");
				
			// 设置行高
			String rowHeight = grid.getRowHeight();
			if(rowHeight != null && rowHeight != "")
				buf.append(getVarShowId() + ".setRowHeight(" + rowHeight + ");\n");
			// 设置表头行高
			String headerRowHeight = grid.getHeaderRowHeight();
			if(headerRowHeight != null && headerRowHeight != "")
				buf.append(getVarShowId() + ".setHeaderRowHeight(" + headerRowHeight + ");\n");
			
			String modelStr = GridModelUtil.generateGridModel(ds, grid, getCurrWidget());
			buf.append(modelStr);
			buf.append(getVarShowId() + ".setModel(model);\n");
			
		}	
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_GRID;
	}
	
	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (GridCellListener.AFTER_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
			buf.append("proxy.addParam('newValue', cellEvent.newValue);\n");
			buf.append("proxy.addParam('oldValue', cellEvent.oldValue);\n");
		}
		else if(GridCellListener.BEFORE_EDIT.equals(eventName)){
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
		}
		else if(GridCellListener.ON_CELL_CLICK.equals(eventName) || GridCellListener.CELL_EDIT.equals(eventName)){
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
		}
	}
	
}

