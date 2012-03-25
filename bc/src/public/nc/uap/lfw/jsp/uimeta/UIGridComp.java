package nc.uap.lfw.jsp.uimeta;

import nc.uap.lfw.core.comp.GridColumn;


public class UIGridComp extends UIComponent {

	private static final long serialVersionUID = -881316663820028640L;
	
	public static final String AUTOEXPAND = "autoExpand";
	
	public UIGridComp() {
		setAutoExpand(UIConstant.TRUE);
	}
	
	public Integer getAutoExpand(){
		return (Integer) getAttribute(AUTOEXPAND);
	}
	
	public void setAutoExpand(Integer autoExpand){
		setAttribute(AUTOEXPAND, autoExpand);
	}
	
	/**
	 * @param ele
	 * ɾ����ͷ��
	 */
	public void removeElement(GridColumn ele){
		this.notifyChange(DELETE, ele);
	}
	
	/**
	 * @param ele
	 * ��ӱ�ͷ��
	 */
	public void addElement(GridColumn ele){
		this.notifyChange(ADD, ele);
	}
	
	/**
	 * @param ele
	 * ���±�ͷ��
	 */
	public void updateElement(GridColumn ele){
		this.notifyChange(UPDATE, ele);
	}

}
