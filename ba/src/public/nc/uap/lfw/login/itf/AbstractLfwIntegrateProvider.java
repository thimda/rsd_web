package nc.uap.lfw.login.itf;

import java.util.Map;

import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.vo.LfwFunNodeVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.login.vo.LfwTreeFunNodeVO;

/**
 * LFW登陆配置
 * 
 * @author licza
 * 
 */
public abstract class AbstractLfwIntegrateProvider {

	/**
	 * 获得登陆帮助类
	 * 
	 * @return
	 */
	public abstract LoginHelper<? extends LfwSessionBean> getLoginHelper();

	/**
	 * 获得所有注册功能节点（树形）
	 * 
	 * @return
	 */
	public abstract LfwTreeFunNodeVO[] getFunNodes();

	/**
	 * 获得用户的注册功能节点
	 * 
	 * @param userid
	 * @return
	 */
	public abstract LfwFunNodeVO[] getFunNodes(Map<String,String> param);

	/**
	 * 获得验证信息字段
	 * 
	 * @return
	 */
	public abstract ExtAuthField[] getAuthFields();

}
