package nc.uap.lfw.core.event.ctx;

import java.io.Serializable;

import nc.uap.lfw.core.page.LfwWidget;

public class WidgetContext implements Serializable{
	private static final String $WS_PRE = "$ws";
	private static final long serialVersionUID = -3742250714460813845L;
	private String id;
	private LfwWidget widget;
//	private Map<String, TabLayout> tabMap = new HashMap<String, TabLayout>();
//	private Map<String, CardLayout> cardMap = new HashMap<String, CardLayout>();
//	private Map<String, PanelLayout> panelMap = new HashMap<String, PanelLayout>();
	private LfwPageContext pageCtx;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LfwWidget getWidget() {
		return widget;
	}
	public void setWidget(LfwWidget widget) {
		this.widget = widget;
	}
	
//	public TabLayout[] getTabs(){
//		return tabMap.values().toArray(new TabLayout[0]);
//	}
//	
//	public TabLayout getTab(String id){
//		return tabMap.get(id);
//	}
//	
//	public void addTab(TabLayout tab){
//		tabMap.put(tab.getId(), tab);
//	}
//	
//	public CardLayout[] getCards() {
//		return cardMap.values().toArray(new CardLayout[0]);
//	}
//	
//	public CardLayout getCard(String id){
//		return cardMap.get(id);
//	}
//	
//	public void addCard(CardLayout card){
//		cardMap.put(card.getId(), card);
//	}
//	
//	public PanelLayout[] getPanels() {
//		return panelMap.values().toArray(new PanelLayout[0]);
//	}
//	
//	public PanelLayout getPanel(String id){
//		return panelMap.get(id);
//	}
//	
//	public void addPanel(PanelLayout panel){
//		panelMap.put(panel.getId(), panel);
//	}
//	
//	/**
//	 * 此处获取的web会话仅仅是作为内存缓存使用，使用内存缓存
//	 * @return
//	 */
//	public WebSession getWebSession(){
//		String widgetWsKey = $WS_PRE + id;
//		WebSession ws = (WebSession) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(widgetWsKey);
//		if(ws == null){
//			ws = new DbWebSession();
//			LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(widgetWsKey, ws);
//		}
//		return ws;
//	}
	public LfwPageContext getPageContext() {
		return pageCtx;
	}
	public void setPageContext(LfwPageContext pageCtx) {
		this.pageCtx = pageCtx;
	}

}
