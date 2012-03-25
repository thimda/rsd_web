package nc.uap.lfw.login.listener;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.rbac.IUserManageQuery_C;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.vo.framework.rsa.Encode;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.userpassword.PasswordSecurityLevelFinder;
import nc.vo.uap.rbac.userpassword.PasswordSecurityLevelVO;

/**
 * 修改密码
 * @author zhangxya
 *
 */
public class PasswordListener extends MouseServerListener {
	
	
	private Encode encode = new Encode();

	public PasswordListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onclick(MouseEvent e) {
		ButtonComp button = (ButtonComp)e.getSource();
		String buttonID = button.getId();
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget("password");
		if("passcancelbutton".equals(buttonID)){
			widget.setVisible(false);
			
		}
		else if("passokbutton".equals(buttonID)){
			TextComp newTextComp = (TextComp) widget.getViewComponents().getComponent("newpassword");
			TextComp confirmnewTextComp = (TextComp) widget.getViewComponents().getComponent("confirmnewpassword");
			String newPassword = newTextComp.getValue();
			String confirmNewPass = confirmnewTextComp.getValue();
			if (newPassword == null || newPassword.trim().equals(""))
				//throw new LfwRuntimeException("用户密码不能为空!");
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "lfw_psw005"));
			if (confirmNewPass == null || confirmNewPass.trim().equals(""))
				//throw new LfwRuntimeException("用户密码不能为空!");
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "lfw_psw005"));
			if(!newPassword.equals(confirmNewPass))
					//throw new LfwRuntimeException("新密码与确认密码不同,请重新输入!");
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "lfw_psw001"));
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
			
			IUserManageQuery_C userManageQuery_C = NCLocator.getInstance().lookup(
					IUserManageQuery_C.class);
			UserVO user = null;
			try {
				//查找登陆用户
				user = userManageQuery_C.getUser(pk_user);
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				LfwLogger.error(e2.getMessage(), e2);
				throw new LfwRuntimeException("登陆用户不存在");
			}
			String oldPassword = user.getUser_password();
			// 设置用户密码
			user.setUser_password(encode.encode(newPassword));
			// 校验密码
			PasswordSecurityLevelVO pwdLevel = PasswordSecurityLevelFinder.getPWDLV(user);
				if(pwdLevel != null)
					verifyPassord(newPassword, pwdLevel, user);
				try {
					SFServiceFacility.getIUserManage().updateUser(user);
				} catch (Exception e1) {
					LfwLogger.error(e1.getMessage(), e1);
					user.setUser_password(oldPassword);
					throw new LfwRuntimeException("更新用户信息时出错");
				}
			widget.setVisible(false);
		}
		String mainPageUrl = LfwRuntimeEnvironment.getModelServerConfig().getMainUrl();
		if(mainPageUrl == null || mainPageUrl.equals(""))
			mainPageUrl = "/main.jsp?pageId=main";
		mainPageUrl = LfwRuntimeEnvironment.getCorePath() + mainPageUrl + "&randId=" + ((Math.random() * 10000) + "").substring(0, 4);
		getGlobalContext().sendRedirect(mainPageUrl);
	}
	
	private void verifyPassord(String sPassword,
			PasswordSecurityLevelVO pwdLevel, nc.vo.sm.UserVO user) {
		if (user == null) {
			throw new LfwRuntimeException("找不到您的用户ID，请联系系统管理员!");
		}
		if (pwdLevel == null) {
			throw new LfwRuntimeException("您的密码级别未定义，请联系管理员!");
		} else {
//			IUserPasswordChecker upchecher = (IUserPasswordChecker) NCLocator
//					.getInstance().lookup(IUserPasswordChecker.class.getName());
//			try {
//				// 修改密码时按照密码等级要求校验新密码
//				upchecher.checkNewpassword(user.getPrimaryKey(), sPassword,
//						pwdLevel, user.getUser_type());
//			} catch (BusinessException be) {
//				LfwLogger.error(be.getMessage(), be);
//			}
		}
	}
	

}
