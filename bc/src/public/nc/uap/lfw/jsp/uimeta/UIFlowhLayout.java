package nc.uap.lfw.jsp.uimeta;

import java.util.Iterator;

public class UIFlowhLayout extends UILayout {
	private static final long serialVersionUID = 2772229643528375039L;
	public static final String AUTO_FILL = "autoFill";
	private static final String GEN_PANEL_PRE = "g_p_";
	private int currentPanelId = 1;
	
	public UIFlowhLayout(){
		setAutoFill(UIConstant.FALSE);
	}
	
	public Integer getAutoFill() {
		return (Integer) getAttribute(AUTO_FILL);
	}

	public void setAutoFill(Integer autoFill) {
		setAttribute(AUTO_FILL, autoFill);
	}
	
	/**
	 * 向当前布局中增加元素，此API将首先添加一个Panel，并将元素添加到对应元素中
	 * @param ele 被添加元素
	 */
	public UIFlowhPanel addElementToPanel(UIElement ele) {
		UIFlowhPanel panel = new UIFlowhPanel();
		panel.setId(GEN_PANEL_PRE + (currentPanelId ++));
		this.addPanel(panel);
		if(ele != null)
			panel.setElement(ele);
		return panel;
	}
	
	/**
	 * 获取当前布局中的元素，此API将遍历所有Panel，并返回ID相等的元素
	 * @param id
	 * @return
	 */
	public UIElement getElementFromPanel(String id) {
		Iterator<UILayoutPanel> it = getPanelList().iterator();
		while(it.hasNext()){
			UIFlowhPanel panel = (UIFlowhPanel) it.next();
			UIElement ele = panel.getElement();
			if(ele == null)
				continue;
			if(id.equals(ele.getId()))
				return ele;
		}
		return null;
	}
}
