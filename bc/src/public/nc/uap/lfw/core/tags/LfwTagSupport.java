package nc.uap.lfw.core.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
/**
 * WebFrame Tag���ࡣ�ṩ�Ǽ�Tag�����еı����ԣ�����pageContext
 * @author dengjt
 *
 */
public class LfwTagSupport extends SimpleTagSupport {
	protected PageContext getPageContext(){
		return (PageContext) getJspContext();
	}
}