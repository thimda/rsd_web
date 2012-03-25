package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * Grid多表头配置类
 * 
 */
public class GridColumnGroup extends WebElement implements IGridColumn {
	private static final long serialVersionUID = 1L;

	private String i18nName;
	private boolean visible = true;
	private String text;
	private GridComp grid;
	// 存储此顶级表头下的所有headers
	private List<IGridColumn> childColumnList = null;
	

	public List<IGridColumn> getChildColumnList() {
		return childColumnList;
	}

	public void setChildColumnList(List<IGridColumn> childColumnList) {
		this.childColumnList = childColumnList;
		if(childColumnList != null && childColumnList.size() > 0){
			Iterator<IGridColumn> it = childColumnList.iterator();
			while(it.hasNext()){
				IGridColumn col = it.next();
				col.setGridComp(grid);
			}
		}
	}

	public void addColumn(IGridColumn column) {
		if (childColumnList == null)
			childColumnList = new ArrayList<IGridColumn>();
		childColumnList.add(column);
		column.setGridComp(grid);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	public void removeColumn(String fieldId)
	{
		if(fieldId == null)
			return;
		if(this.childColumnList != null)
		{
			for(int i = 0; i < this.childColumnList.size(); i++)
			{
				if(this.childColumnList.get(i) instanceof GridColumn)
				{
					GridColumn colum = (GridColumn)this.childColumnList.get(i);
					if(colum.getId() != null && colum.getId().equals(fieldId))
						this.childColumnList.remove(i);
				}
			}
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String showName) {
		this.i18nName = showName;
	}

	public Object clone() {

		GridColumnGroup group = (GridColumnGroup) super.clone();
		if (this.childColumnList != null) {
			group.childColumnList = new ArrayList<IGridColumn>();
			Iterator<IGridColumn> it = this.childColumnList.iterator();
			while (it.hasNext()) {
				group.addColumn((IGridColumn) it.next().clone());
			}
		}
		return group;

	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	public GridComp getGridComp() {
		return this.grid;
	}

	public void setGridComp(GridComp grid) {
		this.grid = grid;
	}
}