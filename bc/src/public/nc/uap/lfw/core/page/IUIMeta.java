package nc.uap.lfw.core.page;

import java.io.Serializable;

/**
 * UIMeta ��ʶ�Խӿ�
 * @author dengjt
 *
 */
public interface IUIMeta extends Serializable{
	/**
	 * ����clone����
	 * @return
	 */
	public IUIMeta doClone();
	/**
	 * ����ʱ�ڲ�����
	 */
	public void adjustForRuntime();
}
