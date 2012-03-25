package nc.uap.lfw.login.itf;

import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.bd.format.FormatDocVO;

/**
 * 处理Masker信息
 * 
 * @author licza
 * 
 */
public interface IMaskerHandler<T extends LfwSessionBean> {
	/**
	 * 根据SessionBean 获取Masker信息
	 * @param loginBean
	 * @return
	 */
	FormatDocVO getMaskerInfo(T loginBean);
}
