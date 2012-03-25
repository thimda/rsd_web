package nc.uap.lfw.reference.base;

public abstract class GridRefModel extends BaseRefModel {
	public Integer getRefType(){
		return REFTYPE_GRID;
	}
	
	/**
	 * ��ȡ������ʾ�е��ֶε�����
	 * @return
	 */
	public abstract String[] getVisibleFieldNames();

	/**
	 * ��ȡ���������е��ֶε�����
	 * @return
	 */
	public abstract String[] getHiddenFieldNames();
	
	public int getPageSize() {
		return 20;
	}
}
