package nc.uap.lfw.core.tags;


import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import nc.uap.lfw.core.model.BasePageModel;
import nc.uap.lfw.core.model.PageModel;

/**
 * PageModel��ӦTag.��Tag����ʼ��PageModel,д��ҳ���ҪƬ��.����PageModel�����PageContext�Թ�
 * �����ؼ����á�
 * ע�⣺��������Lfw��ӦTag��������ڴ�Tag�ڣ���ȡ�ö�Ӧ��������
 * 
 * @author dengjt
 * 2007-1-25 
 */ 
public class PageTag extends SimpleTagWithAttribute {
	/**
	 * Tag��������
	 */
	public void doTag() throws JspException, IOException {
		doRender();
	}
	protected void doRender() throws JspException, IOException {
		ConcurrentMap<String, StringBuffer> bodyScriptMap = null;
		HttpServletRequest request = (HttpServletRequest) ((PageContext)getJspContext()).getRequest();
		BasePageModel model = (BasePageModel) request.getAttribute("pageModel");
		getJspContext().setAttribute("pageModel", model);
		getJspBody().invoke(getJspContext().getOut());
	}
}
