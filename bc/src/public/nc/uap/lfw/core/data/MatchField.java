package nc.uap.lfw.core.data;

import java.io.Serializable;

/**
 * ���ݽ�����Ҫ���ڲ�������ⲿ�࣬��Ҫ���������ļ���matchfieldԪ�ء�
 *
 * create on 2007-3-5 ����03:30:24
 *
 * @author lkp 
 */

public class MatchField implements Serializable,Cloneable{


	private static final long serialVersionUID = 1L;

	/*��ȡ�ֶ�*/
	private String readField = null;

	/*д���ֶ�*/
	private String writeField = null;
	
	private String ismacth;
	private String iscontains;
	
	public String getIsmacth() {
		return ismacth;
	}

	public void setIsmacth(String ismacth) {
		this.ismacth = ismacth;
	}

	public String getIscontains() {
		return iscontains;
	}

	public void setIscontains(String iscontains) {
		this.iscontains = iscontains;
	}

	/**
	 * ȱʡ����
	 *
	 */
	public MatchField() {

	}

	/**
	 * ���ι���
	 * @param read
	 * @param write
	 */
	public MatchField(String read, String write) {

		this.readField = read;
		this.writeField = write;
	}

	/**
	 * ��ȡ��ȡ�ֶ�
	 * @return
	 */
	public String getReadField() {
		return readField;
	}

	/**
	 * ���ö�ȡ�ֶ�
	 * @param readField
	 */
	public void setReadField(String readField) {
		this.readField = readField;
	}

	/**
	 * ��ȡд���ֶ�
	 * @return
	 */
	public String getWriteField() {
		return writeField;
	}

	/**
	 * ����д���ֶ�
	 * @param writeField
	 */
	public void setWriteField(String writeField) {
		this.writeField = writeField;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

