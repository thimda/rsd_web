package nc.uap.lfw.core.comp.ctx;


public class CardContext extends BaseContext{
	private static final long serialVersionUID = 7450661276509612765L;
	private int currentIndex = 0;
	private String[] pageIds;
	private String id;
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	public String[] getPageIds() {
		return pageIds;
	}
	public void setPageIds(String[] pageIds) {
		this.pageIds = pageIds;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
