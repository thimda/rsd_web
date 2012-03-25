package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
/**
 * Lfw风格单据页面头定义.缓存设置为始终不缓存。页面类型为html。编码为UTF-8
 * @author dengjt
 *
 */
public class LfwHeadTag extends LfwTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		StringBuffer buf = new StringBuffer();
		buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n")
			.append("<meta http-equiv=\"Cache-Control\" content=\"no-cache, must-revalidate\"/>\n")
			.append("<meta http-equiv=\"Expires\" content=\"0\"/>\n")
			.append("<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\n");
		HttpServletResponse res = LfwRuntimeEnvironment.getWebContext().getResponse();
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		getJspContext().getOut().write(buf.toString());
	}

}
