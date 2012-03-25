package nc.uap.lfw.jsp.uimeta;



public class UITabComp extends UILayout {
	private static final long serialVersionUID = 9197424119017867646L;
	public static final Integer ONETAB_HIDE_HIDE = 1;
	public static final Integer ONETAB_HIDE_SHOW = 0;
	public static final String CURRENT_ITEM = "currentItem";
	public static final String ONETAB_HIDE = "oneTabHide";
	public static final String TAB_TYPE = "tabType";
	private UITabRightPanel rightPanel;
	
	public UITabRightPanel getRightPanel() {
		return rightPanel;
	}
	public void setRightPanel(UITabRightPanel rightPanel) {
		rightPanel.setLayout(this);
		this.rightPanel = rightPanel;
	}
	public String getId() {
		return (String) getAttribute(ID);
	}
	public void setId(String id) {
		setAttribute(ID, id);
	}
	
	public String getCurrentItem() {
		return (String) getAttribute(CURRENT_ITEM);
	}
	
	public void setCurrentItem(String value){
		setAttribute(CURRENT_ITEM, value);
		UpdatePair pair = new UpdatePair(CURRENT_ITEM, value);
		notifyChange(UPDATE,pair);
	}
	
	public String getWidgetId() {
		return (String) getAttribute(WIDGET_ID);
	}

	public void setWidgetId(String widgetId) {
		setAttribute(WIDGET_ID, widgetId);
	}
	
	public void setOneTabHide(Integer oneTabHide){
		setAttribute(ONETAB_HIDE, oneTabHide);
		UpdatePair pair = new UpdatePair(ONETAB_HIDE, oneTabHide.toString());
		notifyChange(UPDATE,pair);
	}
	
	public Integer getOneTabHide(){
		return (Integer) getAttribute(ONETAB_HIDE);
	}
	
	public void addPanel(UILayoutPanel panel){
		if(panel == null)
			return;
		if(panel instanceof UITabRightPanel)
			this.setRightPanel((UITabRightPanel) panel);
		else
			super.addPanel(panel);
	}
	
	public String getTabType() {
		return (String) getAttribute(TAB_TYPE);
	}

	public void setTabType(String tabType) {
		setAttribute(TAB_TYPE, tabType);
	}
	
	@Override
	public UITabComp doClone() {
		UITabComp tabComp = (UITabComp) super.doClone();
		if(this.rightPanel != null){
			tabComp.rightPanel = (UITabRightPanel) this.rightPanel.doClone();;
		}
		return tabComp;
	}
	
	
}
