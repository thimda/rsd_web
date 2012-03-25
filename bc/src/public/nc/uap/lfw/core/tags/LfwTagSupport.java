package nc.uap.lfw.core.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
/**
 * WebFrame Tag基类。提供非简单Tag所具有的便利性，比如pageContext
 * @author dengjt
 *
 */
public class LfwTagSupport extends SimpleTagSupport {
	protected PageContext getPageContext(){
		return (PageContext) getJspContext();
	}
}