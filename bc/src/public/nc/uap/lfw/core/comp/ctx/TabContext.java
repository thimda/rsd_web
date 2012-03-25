package nc.uap.lfw.core.comp.ctx;

public class TabContext extends BaseContext {
	private static final long serialVersionUID = -8911472470511360043L;
	private String[] tabItemIds;
	private String[] tabItemTexts;
	private boolean[] tabItemEnables;
	private boolean[] tabItemVisibles;
	private boolean[] showCloseIcons;
	private int currentIndex = 0;
	private boolean oneTabHide;
	private String id;
	public String[] getTabItemIds() {
		return tabItemIds;
	}
	public void setTabItemIds(String[] tabItemIds) {
		this.tabItemIds = tabItemIds;
	}
	public String[] getTabItemTexts() {
		return tabItemTexts;
	}
	public void setTabItemTexts(String[] tabItemTexts) {
		this.tabItemTexts = tabItemTexts;
	}
	public boolean[] getTabItemEnables() {
		return tabItemEnables;
	}
	public void setTabItemEnables(boolean[] tabItemEnables) {
		this.tabItemEnables = tabItemEnables;
	}
	public boolean[] getTabItemVisibles() {
		return tabItemVisibles;
	}
	public void setTabItemVisibles(boolean[] tabItemVisibles) {
		this.tabItemVisibles = tabItemVisibles;
	}
	public boolean[] getShowCloseIcons() {
		return showCloseIcons;
	}
	public void setShowCloseIcons(boolean[] showCloseIcons) {
		this.showCloseIcons = showCloseIcons;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	public boolean isOneTabHide() {
		return oneTabHide;
	}
	public void setOneTabHide(boolean oneTabHide) {
		this.oneTabHide = oneTabHide;
	}
}
