package nc.uap.lfw.reference.base;

public abstract class GridRefModel extends BaseRefModel {
	public Integer getRefType(){
		return REFTYPE_GRID;
	}
	
	/**
	 * 获取参照显示列的字段的名称
	 * @return
	 */
	public abstract String[] getVisibleFieldNames();

	/**
	 * 获取参照隐藏列的字段的名称
	 * @return
	 */
	public abstract String[] getHiddenFieldNames();
	
	public int getPageSize() {
		return 20;
	}
}
