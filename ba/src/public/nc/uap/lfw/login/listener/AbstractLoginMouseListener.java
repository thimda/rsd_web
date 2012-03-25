package nc.uap.lfw.login.listener;

import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.itf.LoginInterruptedException;
import nc.uap.lfw.login.util.LfwLoginFetcher;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.sso.NcSsoConstant;

/**
 * Ĭ�ϵ�¼��Ӧʵ�֡�
 *
 */
public abstract class AbstractLoginMouseListener<T extends WebElement> extends MouseServerListener<T> {
	public AbstractLoginMouseListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}
	
	@Override
	public void onclick(MouseEvent<T> event) {
		try {
			// ���ҳ�������Ϣ
			showErrorToClient("");
			AuthenticationUserVO userVO = getLoginHelper().processLogin();
			if(userVO.getUserID().equals(userVO.getPassword())){
				LfwWidget widget = getGlobalContext().getPageMeta().getWidget("password");
				widget.setVisible(true);
			}
			else
				openTargetUrl(userVO);
		} 
		catch (Exception e) {
			if(e instanceof LfwInteractionException)
				throw (LfwInteractionException)e;
			if(!(e instanceof LoginInterruptedException))
				Logger.error(e.getMessage(), e);
			showErrorToClient(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected void openTargetUrl(AuthenticationUserVO userVO) {
		// ��ҳ��ַ
		String mainPageUrl = getTargetUrl();

		Map<String, String> extMap = (Map<String, String>) userVO.getExtInfo();
		String maxwin = extMap.get(NcSsoConstant.MAXWIN);
		// ��󻯴򿪴���
		if (maxwin != null && maxwin.equals("Y")) {
			EventRequestContext.getLfwPageContext().openMaxWindow(mainPageUrl, "", true);
		} 
		// ��ԭ��ҳ���
		else {
			getGlobalContext().sendRedirect(mainPageUrl);
		}
	}
	
	protected String getTargetUrl() {
		String mainPageUrl = LfwRuntimeEnvironment.getModelServerConfig().getMainUrl();
		if(mainPageUrl == null || mainPageUrl.equals(""))
			mainPageUrl = "/main.jsp?pageId=main";
		mainPageUrl = LfwRuntimeEnvironment.getCorePath() + mainPageUrl + "&randId=" + ((Math.random() * 10000) + "").substring(0, 4);
		return mainPageUrl;
	}

	protected LoginHelper<? extends LfwSessionBean> getLoginHelper(){
		return LfwLoginFetcher.getGeneralInstance().getLoginHelper();
	}
	/**
	 * ��ͻ�����ʾ������Ϣ
	 * @param errorMsg
	 */
	protected abstract void showErrorToClient(String errorMsg);
}
