package nc.uap.lfw.core.tags;


/**
 * �����Ϳؼ�Tag�ӿ�
 * @author dengjt
 *
 */
public interface IContainerElementTag {
	/**
	 * �����ؼ���ͷ������
	 * @param parent
	 * @return
	 */
	public String generateHead();
	
	/**
	 * �����ؼ����岿��
	 * @return
	 */
	public String generateTail();
	
	/**
	 * �����ؼ�script ͷ���ű�
	 * @return
	 */
	public String generateHeadScript();
	
	/**
	 * �����ؼ�Script β���ű�
	 * @return
	 */
	public String generateTailScript();
}
