package nc.uap.lfw.login.itf;

import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.bd.format.FormatDocVO;

/**
 * ����Masker��Ϣ
 * 
 * @author licza
 * 
 */
public interface IMaskerHandler<T extends LfwSessionBean> {
	/**
	 * ����SessionBean ��ȡMasker��Ϣ
	 * @param loginBean
	 * @return
	 */
	FormatDocVO getMaskerInfo(T loginBean);
}
