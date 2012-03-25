package nc.uap.lfw.login.login;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.CookieConstant;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.util.CookieUtil;

public class LoginPageModel extends PageModel {

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		
		String sysId = "" + LfwRuntimeEnvironment.getSysId();
		// 从Cookie中获取默认值
		String userId = CookieUtil.get(request.getCookies(), CookieConstant.USER_CODE + sysId);
		if (null == userId)
			userId = "";
		String accountName = CookieUtil.get(request.getCookies(), CookieConstant.ACCOUNT_CODE + sysId);
//		String language = CookieUtil.get(request.getCookies(), CookieConstant.LANG_CODE);
//		String theme = CookieUtil.get(request.getCookies(), CookieConstant.THEME_CODE);
//		String maxWindow = CookieUtil.get(request.getCookies(), CookieConstant.MAX_WIN);
//		boolean isMaxWindow = false;
//		if (null != maxWindow && "true".equals(maxWindow))
//			isMaxWindow = true;
		
		// 加载默认输入内容
		LfwWidget widget = getPageMeta().getWidget("main");
		((TextComp) widget.getViewComponents().getComponent("userid")).setValue(userId);
		
		ComboBoxComp accountCombo = (ComboBoxComp) widget.getViewComponents().getComponent("accountcode");
		if(accountCombo != null){
			if(accountName != null)
				accountCombo.setValue(accountName);
			else{
				
			}
		}
		
//		ComboBoxComp languageCombo = (ComboBoxComp) widget.getViewComponents().getComponent("multiLanguageCombo");
//		languageCombo.setValue(language);
//		
//		ComboBoxComp themeCombo = (ComboBoxComp) widget.getViewComponents().getComponent("themeCombo");
//		themeCombo.setValue(theme);
		
//		CheckBoxComp maxWindowCheckBox = (CheckBoxComp) widget.getViewComponents().getComponent("maxWindowCheckBox");
//		if (isMaxWindow)
//			maxWindowCheckBox.setChecked(true);
	}

//	@Override
//	public String createEtag() {
//		String userId = CookieUtil.get(LfwRuntimeEnvironment.getWebContext().getRequest().getCookies(), CookieConstant.USER_CODE);
//		String etag = super.createEtag();
//		if(userId == null)
//			return etag;
//		return userId + etag;
//	}
	
}

