package nc.uap.lfw.jsp.uimeta;

import nc.uap.lfw.core.comp.FormElement;

public class UIFormComp extends UIComponent {
	private static final long serialVersionUID = 3977206346723075062L;	
	public static final String LABEL_POSITION = "label_position";
	
	/**
	 * @param ele
	 * ɾ��form�е�Ԫ��
	 */
	public void removeElement(FormElement ele){
		this.notifyChange(DELETE, ele);
	}
	
	/**
	 * @param ele
	 * ���form�е�Ԫ��
	 */
	public void addElement(FormElement ele){
		this.notifyChange(ADD, ele);
	}
	
	/**
	 * @param ele
	 * ����form�е�Ԫ��
	 */
	public void updateElement(FormElement ele){
		this.notifyChange(UPDATE, ele);
	}
	
	
	public void setLabelPosition(String labelPosition){
		setAttribute(LABEL_POSITION, labelPosition);
	}
	
	public String getLabelPosition(){
		return (String) getAttribute(LABEL_POSITION);
	}

}
