package nc.uap.lfw.ra.params;

public class UpdateParameter {
	//�޸������ID
	private String compId;
	//���ڵ�ID
	private String prtId;
	//�޸����������
	private String compType;
	//�޸������������
	private String attr;
	//�޸��������������
	private String attrType;
	//�޸�֮ǰ��ֵ
	private String oldValue;
	//�޸�֮���ֵ
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
