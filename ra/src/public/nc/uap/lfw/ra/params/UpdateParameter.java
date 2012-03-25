package nc.uap.lfw.ra.params;

public class UpdateParameter {
	//修改组件的ID
	private String compId;
	//父节点ID
	private String prtId;
	//修改组件的类型
	private String compType;
	//修改组件属性名称
	private String attr;
	//修改组件的属性类型
	private String attrType;
	//修改之前的值
	private String oldValue;
	//修改之后的值
	private String newValue;
	
	private String viewId;
	
	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public String getCompType() {
		return compType;
	}
	public void setCompType(String compType) {
		this.compType = compType;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getAttrType() {
		return attrType;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getPrtId() {
		return prtId;
	}
	public void setPrtId(String prtId) {
		this.prtId = prtId;
	}

}
