package nc.uap.lfw.core.tags;


import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;

/**
 * 此tag负责生成html base标签,如果
 * 
 * @author gd 2007-11-29
 */
public class HtmlBaseTag extends SimpleTagWithAttribute{
	private static final long serialVersionUID = -800842569936458603L;
		
	public void doTag() throws JspException, IOException {
		//如果是离线客户端模式，直接返回
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
