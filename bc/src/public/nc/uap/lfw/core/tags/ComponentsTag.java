package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;


public class ComponentsTag  extends SimpleTagWithAttribute{

	private String componentType = null;
	private String var = null;
	@Override
	public void doTag() throws JspException, IOException {
//		PageModel pm = (PageModel) this.getJspContext().getAttribute("pageModel");
//		Class c = LfwClassUtil.forName(componentType);
//		WebComponent[] components = pm.getPageMeta().getViewComponents().getComponentByType(c);
//		this.getJspContext().setAttribute(var, components);
	}
	
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
	
}
