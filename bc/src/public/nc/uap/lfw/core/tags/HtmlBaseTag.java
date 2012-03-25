package nc.uap.lfw.core.tags;


import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;

/**
 * ��tag��������html base��ǩ,���
 * 
 * @author gd 2007-11-29
 */
public class HtmlBaseTag extends SimpleTagWithAttribute{
	private static final long serialVersionUID = -800842569936458603L;
		
	public void doTag() throws JspException, IOException {
		//��������߿ͻ���ģʽ��ֱ�ӷ���
		if(LfwRuntimeEnvironment.isClientMode())
			return;
		
		PageContext pageContext = getPageContext();
		ServletRequest req = pageContext.getRequest();
		String rootPath = (String)pageContext.getServletContext().getAttribute(nc.uap.lfw.core.common.WebConstant.ROOT_PATH);
		String serverAddr = LfwRuntimeEnvironment.getServerAddr();
		String addr = null;
		if(serverAddr != null && !serverAddr.trim().equals("")){
			addr = "<base href=\"" + serverAddr + rootPath;
		}
		else{
			String baseAddr = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
			addr = "<base href=\"" + baseAddr + rootPath;
		}
		
		if(LfwRuntimeEnvironment.isFromLfw())
			addr += WebConstant.TEMP_FROM_LFW_PATH;
		addr += "/\" />";
		
		pageContext.getOut().write(addr);
	}
}
