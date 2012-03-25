package nc.uap.lfw.core.tags;


import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import nc.uap.lfw.core.model.PageModel;

/**
 * PageModel对应Tag.此Tag将初始化PageModel,写入页面必要片段.并将PageModel存放在PageContext以供
 * 后续控件调用。
 * 注意：所有其它Lfw对应Tag必须包含在此Tag内，以取得对应的上下文
 * 
 * @author dengjt
 * 2007-1-25 
 */ 
public class PageTag extends SimpleTagWithAttribute {
	/**
	 * Tag类调用入口
	 */
	public void doTag() throws JspException, IOException {
		doRender();
	}
	protected void doRender() throws JspException, IOException {
		ConcurrentMap<String, StringBuffer> bodyScriptMap = null;
		HttpServletRequest request = (HttpServletRequest) ((PageContext)getJspContext()).getRequest();
		PageModel model = (PageModel) request.getAttribute("pageModel");
		getJspContext().setAttribute("pageModel", model);
		getJspBody().invoke(getJspContext().getOut());
	}
}
