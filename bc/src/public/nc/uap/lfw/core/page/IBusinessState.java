package nc.uap.lfw.core.page;

/**
 * ���ݵ�ҵ��״̬
 * @author gd 2010-5-24
 * @version NC6.0
 */
public interface IBusinessState {
	//������ͨ��
	public static final String NOPASS = "0";
	//����ͨ��
	public static final String CHECKPASS = "1";
	//����������
	public static final String CHECKGOING = "2";
	//�ύ̬
	public static final String COMMIT = "3";
	//����̬
	public static final String FREE = "-1";
	
	//����̬
	public static final String DELETE = "4";
	//����̬
	public static final String CX = "5";
	//��ֹ(���㣩̬
	public static final String ENDED = "6";
	//����״̬
	public static final String FREEZE = "7";

	public static final String ALL = "30";
}
