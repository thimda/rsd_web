package nc.uap.lfw.core.tags;


/**
 * ��ͨ�ؼ��ӿ�
 * @author dengjt
 *
 */
public interface INormalComponentTag {
	public String generateBody();
	public String generateBodyScript();
	/**
	 * ����ؼ���ds����Ҫ������дsetDataset�ű���֮���Բ�����generateBodyScript��һ��
	 * �㶨������Ϊ������Ҫ���¼���֮���ds
	 * @return
	 */
	public String generateDsBindingScript();
}
