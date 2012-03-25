package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * Excel多表头配置类
 * 
 * @author guoweic
 * 
 */
public class ExcelColumnGroup extends WebElement implements IExcelColumn {
	private static final long serialVersionUID = 1L;

	private String i18nName;
	private boolean visible = true;
	private String text;

	// 存储此顶级表头下的所有headers
	private List<IExcelColumn> childColumnList = null;

	// /*用于存放字段的属性信息*/
	// private Map<String, Property> columnProperty;

	public List<IExcelColumn> getChildColumnList() {
		return childColumnList;
	}

	public void setChildColumnList(List<IExcelColumn> childColumnList) {
		this.childColumnList = childColumnList;
	}

	public void addColumn(IExcelColumn column) {
		if (childColumnList == null)
			childColumnList = new ArrayList<IExcelColumn>();
		childColumnList.add(column);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void removeColumn(String fieldId) {
		if (fieldId == null)
			return;
		if (this.childColumnList != null) {
			for (int i = 0; i < this.childColumnList.size(); i++) {
				if (this.childColumnList.get(i) instanceof ExcelColumn) {
					ExcelColumn colum = (ExcelColumn) this.childColumnList
							.get(i);
					if (colum.getId() != null && colum.getId().equals(fieldId))
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

		ExcelColumnGroup group = (ExcelColumnGroup) super.clone();
		if (this.childColumnList != null) {
			group.childColumnList = new ArrayList<IExcelColumn>();
			Iterator<IExcelColumn> it = this.childColumnList.iterator();
			while (it.hasNext()) {
				group.addColumn((IExcelColumn) it.next().clone());
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

	// public void addProperty(Property p)
	// {
	// if(p == null)
	// return ;
	// if(columnProperty == null)
	// columnProperty = new HashMap<String, Property>();
	// columnProperty.put(p.getName(), p);
	// }
	//	
	// public String getPropertyValueByName(String name)
	// {
	// Property p = getProperty(name);
	// if(p == null)
	// return null;
	// return p.getValue();
	// }
	//	
	// public Property getProperty(String name)
	// {
	// if(name == null || name.trim().equals(""))
	// return null;
	// if(columnProperty == null)
	// return null;
	// return columnProperty.get(name);
	// }

	// public Property[] getProperties()
	// {
	// if(columnProperty == null)
	// return new Property[0];
	// return columnProperty.values().toArray(new Property[0]);
	// }
	//	
	// public void removeProperty(String name)
	// {
	// if(columnProperty == null || name == null || name.trim().equals(""))
	// return ;
	// columnProperty.remove(name);
	// }
}