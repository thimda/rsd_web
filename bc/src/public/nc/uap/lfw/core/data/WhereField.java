package nc.uap.lfw.core.data;

import java.io.Serializable;

/**
* ���ݽ�����Ҫ���ڲ�������ⲿ�࣬��Ҫ���������ļ���wherefieldԪ�ء�
 *
 * create on 2007-3-5 ����03:31:07
 *
 * @author lkp 
  */

public class WhereField implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 1L;

	/*�����ֶ�����*/
	private String key = null;

	/*�����ֶ�ȡֵ��Դ�ֶ�����*/
	private String value = null;

	/**
	 * ȱʡ����
	 *
	 */
	public WhereField() {
	}

	/**
	 * ���ι���
	 * @param key
	 * @param value
	 */
	public WhereField(String key, String value) {

		this.key = key;
		this.value = value;
	}

	/**
	 * ��ȡ�����ֶ�����
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * ���������ֶ�����
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * ��ȡ�����ֶ�ȡֵ��Դ�ֶ�����
	 * @return
	 */
	public String getValue() {
		return value;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * ���������ֶ�ȡֵ��Դ�ֶ�����
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}

