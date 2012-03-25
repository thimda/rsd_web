package nc.uap.lfw.login.filter;

import nc.uap.lfw.login.itf.ILoginSsoService;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * NC系统的默认Filter
 * @author dengjt
 *
 */
public class LfwLoginFilter extends AbstractLfwLoginFilter {
	public static final String SYS_TYPE_NC = "nc";
	@Override
	protected ILoginSsoService<? extends LfwSessionBean> getLoginSsoService() {
		ILoginSsoService<? extends LfwSessionBean>	ssoService = (ILoginSsoService<? extends LfwSessionBean>) LfwClassUtil.newInstance("nc.uap.lfw.ncadapter.login.DefaultNcSsoServiceImpl");
		return ssoService;
	}

	@Override
	protected String getSysType() {
		return SYS_TYPE_NC;
	}

}
