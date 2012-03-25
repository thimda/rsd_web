package nc.uap.lfw.login.itf;

import java.util.Map;

import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.vo.LfwFunNodeVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.login.vo.LfwTreeFunNodeVO;

/**
 * LFW��½����
 * 
 * @author licza
 * 
 */
public abstract class AbstractLfwIntegrateProvider {

	/**
	 * ��õ�½������
	 * 
	 * @return
	 */
	public abstract LoginHelper<? extends LfwSessionBean> getLoginHelper();

	/**
	 * �������ע�Ṧ�ܽڵ㣨���Σ�
	 * 
	 * @return
	 */
	public abstract LfwTreeFunNodeVO[] getFunNodes();

	/**
	 * ����û���ע�Ṧ�ܽڵ�
	 * 
	 * @param userid
	 * @return
	 */
	public abstract LfwFunNodeVO[] getFunNodes(Map<String,String> param);

	/**
	 * �����֤��Ϣ�ֶ�
	 * 
	 * @return
	 */
	public abstract ExtAuthField[] getAuthFields();

}
