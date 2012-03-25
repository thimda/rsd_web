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
 * �޸�����
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
				//throw new LfwRuntimeException("�û����벻��Ϊ��!");
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "lfw_psw005"));
			if (confirmNewPass == null || confirmNewPass.trim().equals(""))
				//throw new LfwRuntimeException("�û����벻��Ϊ��!");
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "lfw_psw005"));
			if(!newPassword.equals(confirmNewPass))
					//throw new LfwRuntimeException("��������ȷ�����벻ͬ,����������!");
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "lfw_psw001"));
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
			
			IUserManageQuery_C userManageQuery_C = NCLocator.getInstance().lookup(
					IUserManageQuery_C.class);
			UserVO user = null;
			try {
				//���ҵ�½�û�
				user = userManageQuery_C.getUser(pk_user);
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				LfwLogger.error(e2.getMessage(), e2);
				throw new LfwRuntimeException("��½�û�������");
			}
			String oldPassword = user.getUser_password();
			// �����û�����
			user.setUser_password(encode.encode(newPassword));
			// У������
			PasswordSecurityLevelVO pwdLevel = PasswordSecurityLevelFinder.getPWDLV(user);
				if(pwdLevel != null)
					verifyPassord(newPassword, pwdLevel, user);
				try {
					SFServiceFacility.getIUserManage().updateUser(user);
				} catch (Exception e1) {
					LfwLogger.error(e1.getMessage(), e1);
					user.setUser_password(oldPassword);
					throw new LfwRuntimeException("�����û���Ϣʱ����");
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
			throw new LfwRuntimeException("�Ҳ��������û�ID������ϵϵͳ����Ա!");
		}
		if (pwdLevel == null) {
			throw new LfwRuntimeException("�������뼶��δ���壬����ϵ����Ա!");
		} else {
//			IUserPasswordChecker upchecher = (IUserPasswordChecker) NCLocator
//					.getInstance().lookup(IUserPasswordChecker.class.getName());
//			try {
//				// �޸�����ʱ��������ȼ�Ҫ��У��������
//				upchecher.checkNewpassword(user.getPrimaryKey(), sPassword,
//						pwdLevel, user.getUser_type());
//			} catch (BusinessException be) {
//				LfwLogger.error(be.getMessage(), be);
//			}
		}
	}
	

}
