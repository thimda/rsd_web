package nc.uap.lfw.core.comp.ctx;

public class PageUIContext extends BaseContext {
	private static final long serialVersionUID = -6042018326500451433L;
	private String currentPageState;
	private String currentOperateState;
	private String currentBusiState;
	private String currentUserState;
	private Boolean hasChanged;
	
	public String getCurrentPageState() {
		return currentPageState;
	}
	public void setCurrentPageState(String currentPageState) {
		this.currentPageState = currentPageState;
	}
	public String getCurrentOperateState() {
		return currentOperateState;
	}
	public void setCurrentOperateState(String currentOperateState) {
		this.currentOperateState = currentOperateState;
	}
	public String getCurrentBusiState() {
		return currentBusiState;
	}
	public void setCurrentBusiState(String currentBusiState) {
		this.currentBusiState = currentBusiState;
	}
	public String getCurrentUserState() {
		return currentUserState;
	}
	public void setCurrentUserState(String currentUserState) {
		this.currentUserState = currentUserState;
	}
	public Boolean getHasChanged() {
		return hasChanged;
	}
	public void setHasChanged(Boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
	
}
