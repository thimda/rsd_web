package nc.uap.lfw.login.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.MarshallException;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.core.servlet.LfwServletBase;
import nc.uap.lfw.login.itf.ILfwSsoQryService;
import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.uap.lfw.servletplus.core.impl.BaseAction;

/**
 * SSO数据交换
 * 
 * @author guoweic
 * 
 */
public class LfwSessionExchangeAction extends LfwServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5488890384050841597L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LfwLogger.debug("===LfwSessionExchangeAction#doGet=== servlet Start." );
		this.doPost(req, resp);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BaseAction ba = new BaseAction();
		ba.fill(req, resp);
		String id = req.getParameter("id");
		String ds = req.getParameter("ds");
		if(StringUtils.isNotBlank(ds))
			LfwRuntimeEnvironment.setDatasource(ds);
		try {
			ba.print(exchange(id));
		} catch (Exception e) {
			LfwLogger.error("===LfwSessionExchangeAction#exchange=== 获取token失败,tokenid:"+ id + "msg:" + e.getMessage(),e);
		}
		ba.fush();
	}


	public String exchange(String id)
			throws LfwBusinessException, MarshallException {
		ILfwSsoQryService ssoQry = NCLocator.getInstance().lookup(ILfwSsoQryService.class);
		LfwTokenVO token = ssoQry.getTokenByID(id);
		return LfwJsonSerializer.getInstance().toJsObject(token);
	}
}
