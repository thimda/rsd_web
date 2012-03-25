package nc.uap.lfw.core.cmd.base;

import java.io.Serializable;

/**
 * From-Where SQL����
 * 
 * 
 * �������ڣ�2010-9-21
 */
public interface FromWhereSQL extends Serializable {

	/** Ĭ������·�� */
	public static final String DEFAULT_KEY = ".";
	
	/** Ĭ������·����������ʵ��������Key */
	public static final String DEFAULT_ATTRPATH = DEFAULT_KEY;
	
	/**
	 * ����From���
	 */
	public String getFrom();

	/**
	 * ����Where���
	 */
	public String getWhere();

	/**
	 * ����Ԫ��������·���������Զ�Ӧ��ı���</br>
	 * <p><strong>������ͨ����(������չ����)</strong>
	 * </br>ʾ�����£�
	 * </br>bean(���� order)
	 * </br>|
	 * </br>|- code(��ͨ���ԣ����� order)
	 * </br>|
	 * </br>|- ref(�������ԣ����� table_ref)</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |- code(��ͨ���ԣ����� table_ref)
	 * </br>         
	 * </br>|- collection(�ۺ����ԣ����� detail)</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |- name(��ͨ���ԣ����� detail)
	 * <li>
	 * �����ȡ��ʵ��order��ı��������Ҫ�����attrpathΪFromWhereSQL.DEFAULT_ATTRPATH��
	 * <li>
	 * �����ȡ��ʵ��detail��ı��������Ҫ�����attrpathΪcollection��
	 * <li>
	 * �����ȡ����ʵ��table_ref��ı��������Ҫ�����attrpathΪref��
	 * <p></br><strong>������չ���ԣ�attrpath��Ҫ��ѭ�淶��</br>
	 * ����չ�������滻����չ����(����ǰ׺��ǰ׺���ֲ���)��</strong>
	 * </br>ʾ�����£�
	 * </br>bean(���� order)
	 * </br>|
	 * </br>|- ext_code(��չ���ԣ����� ext_order)
	 * </br>|
	 * </br>|- collection(�ۺ����ԣ����� detail)</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |- ext_name(��չ���ԣ����� ext_detail)
	 * <li>
	 * �����ȡext_order��ı��������չ淶������չ������ext_code�滻Ϊ��չ����ext_order������ext_order���ɡ�
	 * <li>
	 * ����õ�ext_detail��ı��������չ淶������չ������ext_name�滻Ϊ��չ����ext_detail������collection.ext_detail���ɡ�
	 * 
	 * @param attrpath
	 *            Ԫ��������·��(�磺dept.code)
	 */
	public String getTableAliasByAttrpath(String attrpath);
}