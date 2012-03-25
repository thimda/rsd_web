package nc.uap.lfw.core.comp.ctx;

public class OutlookbarContext extends BaseContext {

	private static final long serialVersionUID = 1L;
	
	private int currentIndex = 0;
	private String id;
	private String[] itemIds = null;
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}
	
	
}
