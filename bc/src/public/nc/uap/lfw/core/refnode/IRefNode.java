package nc.uap.lfw.core.refnode;

import nc.uap.lfw.core.page.LfwWidget;

public interface IRefNode extends Cloneable{
	public void setWidget(LfwWidget widget);

	public String getId();
	
	public void setId(String id);

	public Object clone();

	public void setRendered(boolean b);

	public void setWindowType(String winTypeApp);
}
