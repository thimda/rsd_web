package nc.uap.lfw.core.combodata;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * ��̬�����б�ؼ������ý�ģ�࣬�����ý���ʱʹ�ø����������״̬��
 * ���⣬������Ϊ�������û����õĶ�̬�����б�������ʵ������
 *
 */
public class DynamicComboDataConf extends DynamicComboData {

	private static final long serialVersionUID = -1023276292907278981L;
	
	private String className = null;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public CombItem[] createCombItems() {
		
		if(this.className == null)
			return null;
		
		try {
			DynamicComboData dynamic = (DynamicComboData)Class.forName(className).newInstance();
			return dynamic.getAllCombItems();
			
		} catch (Exception e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("��̬��������ID����Ϊ��!\r\n");
		}
		if(this.getClassName() == null || this.getClassName().equals("")){
			buffer.append("��̬�������ݵ�ʵ���಻��Ϊ��!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}	
}
